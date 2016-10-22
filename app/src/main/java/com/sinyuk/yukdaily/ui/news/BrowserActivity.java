package com.sinyuk.yukdaily.ui.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinyuk.myutils.system.ToastUtils;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.customtab.CustomTabActivityHelper;
import com.sinyuk.yukdaily.customtab.WebviewActivityFallback;
import com.sinyuk.yukdaily.data.news.NewsRepository;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;
import com.sinyuk.yukdaily.databinding.ActivityBrowserBinding;
import com.sinyuk.yukdaily.entity.news.News;
import com.sinyuk.yukdaily.ui.browser.BaseWebActivity;
import com.sinyuk.yukdaily.utils.AssetsUtils;
import com.sinyuk.yukdaily.widgets.ElasticDragDismissFrameLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by Sinyuk on 16.10.21.
 */

public class BrowserActivity extends BaseWebActivity implements GestureDetector.OnGestureListener {
    public static final String KEY_NEWS_ID = "NEWS_ID";
    public static final String TAG = "BrowserActivity";
    @Inject
    File mCacheFile;
    @Inject
    ToastUtils toastUtils;
    @Inject
    NewsRepository newsRepository;
    private ActivityBrowserBinding binding;
    NestedScrollView.OnScrollChangeListener onScrollChangeListener = new NestedScrollView.OnScrollChangeListener() {
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            binding.parallaxScrimageView.setOffset(scrollY);
        }
    };

    private GestureDetectorCompat mDetector;
    private Observer<News> observer = new Observer<News>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(News news) {
            binding.setNews(news);
            String html = AssetsUtils.loadText(BrowserActivity.this, "html/template.html");
            if (html != null) {
                html = html.replace("{content}", news.getBody());
                // 不要顶部的图标我们自己显示
                html = html.replace("<div class=\"img-place-holder\">", "");
                final String resultHTML = setImageOnClickListener(html);
                binding.webView.loadDataWithBaseURL(null, resultHTML, "text/html", "UTF-8", null);
            } else {
                // assert error
            }
        }
    };
    private CustomTabActivityHelper customTabActivityHelper;
    private CustomTabsIntent.Builder customTabsBuilder;

    public static void start(Context context, int id) {
        Intent starter = new Intent(context, BrowserActivity.class);
        starter.putExtra(KEY_NEWS_ID, id);
        context.startActivity(starter);
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade(1000)
                .into(imageView);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.get(this).getAppComponent().plus(new NewsRepositoryModule()).inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_browser);

        setupGuesture();

        initWebViewSettings(binding.webView, mCacheFile);

        final int id = getIntent().getIntExtra(KEY_NEWS_ID, -1);

        if (id != -1) {
            addSubscription(newsRepository.getNews(id).subscribe(observer));
        }

        customTabActivityHelper = new CustomTabActivityHelper();

    }

    private void setupGuesture() {
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this, this);


        binding.elasticDragDismissFrameLayout.addListener(new ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            @Override
            public void onDragDismissed() {
                // if we drag dismiss downward then the default reversal of the enter
                // transition would slide content upward which looks weird. So reverse it.
                if (binding.elasticDragDismissFrameLayout.getTranslationY() > 0) {
                }
                ActivityCompat.finishAfterTransition(BrowserActivity.this);
            }
        });

        binding.nestedScrollView.setOnScrollChangeListener(onScrollChangeListener);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleUrl(intent.getDataString());
    }

    @Override
    protected void initWebViewSettings(WebView webView, File cache) {
        super.initWebViewSettings(webView, cache);

        webView.addJavascriptInterface(new JavaScriptObject(this), "injectedObject");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                handleUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { return true; }
                handleUrl(request.getUrl().toString());
                return true;
            }
        });

        WebSettings webSetting = webView.getSettings();

        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setDisplayZoomControls(false);

    }

    private void handleUrl(String url) {
        Log.d(TAG, "handleUrl: getAuthority " + Uri.parse(url).getAuthority());
        if (Uri.parse(url).getAuthority().contains("zhihu.com")) {
            // 如果打开的是知乎的链接 www.zhihu.com/zhuanlan.zhihu.com
            // 判断有没有装知乎
            Intent activityIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            final PackageManager pm = getPackageManager();
            final List<ResolveInfo> resolvedActivityList = pm.queryIntentActivities(
                    activityIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (int i = 0; i < resolvedActivityList.size(); i++) {
                if (resolvedActivityList.get(i).activityInfo.packageName.equals("com.zhihu.android")) {
                    // 装了知乎
                    startActivity(activityIntent);
                    return;
                }
            }
        }

        if (customTabsBuilder == null) {
            initCustomtabs();
        }

        CustomTabsIntent customTabsIntent = customTabsBuilder.build();
        CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(url), new WebviewActivityFallback());
    }

    private void initCustomtabs() {
        customTabsBuilder = new CustomTabsIntent.Builder(customTabActivityHelper.getSession());
        customTabsBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        customTabsBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorAccent));
        customTabsBuilder.addDefaultShareMenuItem();
    }

    /**
     * 替换html中的<img标签的属性
     *
     * @param html
     * @return
     */
    private String setImageOnClickListener(String html) {

        Document doc = Jsoup.parse(html);

        Elements es = doc.getElementsByTag("img");

        for (Element e : es) {
            final String imgUrl = e.attr("src");

            if (!e.attr("class").equals("avatar")) {
                e.attr("onclick", "openImage('" + imgUrl + "')");
            }
        }

        return doc.html();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customTabActivityHelper.setConnectionCallback(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        customTabActivityHelper.bindCustomTabsService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        customTabActivityHelper.unbindCustomTabsService(this);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "onFling: " + e1.toString() + e2.toString());
        binding.parallaxScrimageView.setImmediatePin(true);
        return true;
    }

    private static class JavaScriptObject {

        private final WeakReference<Activity> ref;

        JavaScriptObject(Activity instance) {
            ref = new WeakReference<>(instance);
        }

        @JavascriptInterface
        public void openImage(String url) {
            if (ref.get() != null && !ref.get().isFinishing()) {
//                NewsImageActivity.start(ref.get(), url);
                Toast.makeText(ref.get(), url, Toast.LENGTH_LONG).show();
            }
        }
    }


}
