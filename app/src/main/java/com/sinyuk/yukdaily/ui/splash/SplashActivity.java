package com.sinyuk.yukdaily.ui.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.sinyuk.myutils.system.NetWorkUtils;
import com.sinyuk.myutils.system.ScreenUtils;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.Sinyuk;
import com.sinyuk.yukdaily.api.NewsApi;
import com.sinyuk.yukdaily.base.BaseActivity;
import com.sinyuk.yukdaily.data.gank.GankRepository;
import com.sinyuk.yukdaily.data.news.NewsRepository;
import com.sinyuk.yukdaily.ui.home.HomeActivity;
import com.sinyuk.yukdaily.utils.rx.SchedulerTransformer;

import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observable;
import rx.Observer;

/**
 * Created by Sinyuk on 16/8/16.
 * 之后我们可以在闪屏页中初始化一个库 whatever
 * 发起一个网络请求 在这里加载用户数据( UserManager ) 第一页列表( ShotRepository ) 等等
 * 或者做一些复杂的处理。
 */
public class SplashActivity extends BaseActivity {
    @Inject
    RxSharedPreferences preferences;

    @Inject
    Lazy<NewsRepository> newsRepositoryLazy;

    @Inject
    Lazy<GankRepository> gankRepositoryLazy;


    private Preference<String> path;
    private View footer;

    private void startMainActivity() {
        Intent starter = new Intent(SplashActivity.this, HomeActivity.class);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(starter);
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.get(this).getAppComponent().plus().inject(this);


        ScreenUtils.hideSystemyBar(this);

        setContentView(R.layout.activity_splash);


        footer = findViewById(R.id.footer);


        path = preferences.getString(Sinyuk.KEY_SPLASH_BACKDROP_PATH);

        if (!TextUtils.isEmpty(path.get())) {
            loadFromCache();
        } else {
            downloadAndCache();
        }

        prepare();

    }

    private void prepare() {

        // 预加载第一页的新闻
        if (!NetWorkUtils.isNetworkConnection(this)) { return; }

        addSubscription(newsRepositoryLazy.get().getLatestNews()
                .doOnError(Throwable::printStackTrace)
                .subscribe(stories -> {
                    Log.d(TAG, "prepare stories");
                }));
        // 预加载Gank的历史
        addSubscription(gankRepositoryLazy.get().getHistory()
                .doOnError(Throwable::printStackTrace)
                .subscribe(history -> {
                    Log.d(TAG, "prepare history");
                }));

        addSubscription(newsRepositoryLazy.get().getThemes()
                .doOnError(Throwable::printStackTrace)
                .subscribe(response -> {
                    Log.d(TAG, "prepare themes");
                }));
    }

    private void animateIn() {
        if (footer == null) { return; }
        footer.setTranslationY(getResources().getDimensionPixelOffset(R.dimen.toolbar_height));
        footer.setAlpha(0);
        footer.setVisibility(View.VISIBLE);
        footer.animate().translationY(0)
                .alpha(1)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(800)
                .setStartDelay(600)  // avoid flash
                .withLayer()
                .withEndAction(() -> footer.postDelayed(this::startMainActivity, 1000))
                .start();
    }

    private void loadFromCache() {
        Log.d(TAG, "loadFromCache: " + path.get());
        Bitmap backdrop = BitmapFactory.decodeFile(path.get());

        if (backdrop != null) {
            getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(), backdrop));
            loadSucceed();
        } else {
            Log.d(TAG, "loadFromCache: Failed");
            clearCache();
            downloadAndCache();
        }
    }

    private void loadSucceed() {
        animateIn();
    }

    private File downloadOnly() throws ExecutionException, InterruptedException {
        return Glide.with(this)
                .load(NewsApi.SPLASH_BACKDROP_URL)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
    }


    private void downloadAndCache() {
        addSubscription(Observable.fromCallable(this::downloadOnly)
                .map(File::getPath)
                .compose(new SchedulerTransformer<>())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        loadFromCache();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        clearCache();
                        downloadAndDraw();
                    }

                    @Override
                    public void onNext(String localPath) {
                        Log.d(TAG, "onNext: " + localPath);
                        updateCache(localPath);
                    }
                }));
    }

    private void downloadAndDraw() {
        Glide.with(this)
                .load(NewsApi.SPLASH_BACKDROP_URL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        getWindow().setBackgroundDrawable(resource);
                        loadSucceed();
                        return false;
                    }
                })
                .preload();
    }


    private void updateCache(String path) {
        this.path.set(path);
    }


    private void clearCache() {
        this.path.delete();
    }


}
