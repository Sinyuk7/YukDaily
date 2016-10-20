package com.sinyuk.yukdaily.ui.splash;

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.myutils.system.ScreenUtils;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.Sinyuk;
import com.sinyuk.yukdaily.api.NewsService;
import com.sinyuk.yukdaily.base.BaseActivity;
import com.sinyuk.yukdaily.databinding.ActivitySplashBinding;
import com.sinyuk.yukdaily.entity.news.StartImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sinyuk on 16/8/16.
 * 之后我们可以在闪屏页中初始化一个库 whatever
 * 发起一个网络请求 在这里加载用户数据( UserManager ) 第一页列表( ShotRepository ) 等等
 * 或者做一些复杂的处理。
 */
public class SplashActivity extends BaseActivity {
    protected Handler myHandler = new Handler();
    ActivitySplashBinding binding;
    @Inject
    NewsService newsService;

    @Inject
    Application application;

    @Inject
    RxSharedPreferences preferences;

    private Runnable mLazyLoadRunnable;
    private StartImage startImage = new StartImage();
    private Preference<String> path;
    private Preference<String> url;


    private final Observer<String> pathObserver = new Observer<String>() {
        @Override
        public void onCompleted() {
            updateBackdropUrl(startImage.getImageUrl());
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            clear();
        }

        @Override
        public void onNext(String path) {
            updateBackdropPath(path);
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).getAppComponent().plus(new SplashModule(this)).inject(this);

        ScreenUtils.hideSystemyBar(this);

        final int height = ScreenUtils.getScreenHeight(this);
        final int width = ScreenUtils.getScreenWidth(this);
        final String resolution = width + "*" + height;

        url = preferences.getString(Sinyuk.KEY_SPLASH_BACKDROP_URL);
        path = preferences.getString(Sinyuk.KEY_SPLASH_BACKDROP_PATH);

        addSubscription(newsService.getStartImage(resolution)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    loadBackDropFromCache(data.getImageUrl(), width, height);
                    //
                    startImage.setAuthor(data.getAuthor());
                    startImage.setImageUrl(data.getImageUrl());
                }));


        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        binding.setStartImage(startImage);
//        App.get(this).getAppComponent().plus(new SplashModule(this)).inject(this);
//        mLazyLoadRunnable = this::startMainActivity;
//        if (savedInstanceState == null) {
//            getWindow().getDecorView().post(() -> myHandler.postDelayed(mLazyLoadRunnable, 0));
//        }
    }

    private void loadBackDropFromCache(final String imageUrl, final int width, final int height) {
        Log.d(TAG, "loadBackDropFromCache: url" + imageUrl);
        if (url.isSet() && path.isSet() && imageUrl.equals(url.get())) {
            Log.d(TAG, "loadBackDropFromCache: url" + url.get());
            Log.d(TAG, "loadBackDropFromCache: path" + path.get());
            Glide.with(this)
                    .load(Uri.parse(path.get()))
                    .listener(new RequestListener<Uri, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            clear();
                            loadBackDropFromWeb(imageUrl, width, height);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            getWindow().setBackgroundDrawable(resource);
                            return false;
                        }
                    }).preload(width, height);
        } else {
            loadBackDropFromWeb(imageUrl, width, height);
        }
    }

    private void loadBackDropFromWeb(final String imageUrl, final int width, final int height) {
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(), resource));
                        addSubscription(Observable.just(resource)
                                .map(bitmap -> saveBitmapInCache(bitmap))
                                .map(Uri::toString)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(pathObserver));
                        return false;
                    }
                }).preload(width, height);
    }

    private void updateBackdropPath(String path) {
        this.path.set(path);
    }

    private void updateBackdropUrl(String url) {
        this.url.set(url);
    }

    private void clear() {
        this.path.delete();
        this.url.delete();
    }

    private Uri saveBitmapInCache(Bitmap bitmap) {
        try {
            File e = new File(getExternalCacheDir(), Sinyuk.SPLASH_BACKDROP_FILE_NAME);
            FileOutputStream fOut = new FileOutputStream(e);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fOut);
            fOut.flush();
            fOut.close();
            e.setReadable(true, true);
            return Uri.fromFile(e);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void startMainActivity() {
//        Timber.d("Splash Finish");
//        Intent starter = new Intent(SplashActivity.this, ShotsListDemo.class);
//        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        starter.setData(Uri.parse("http://weibo.com/163music?refer_flag=0000015010_&from=feed&loc=nickname"));
//        startActivity(starter);
//        finish();
    }

}
