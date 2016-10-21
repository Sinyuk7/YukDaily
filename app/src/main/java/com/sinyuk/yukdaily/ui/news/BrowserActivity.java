package com.sinyuk.yukdaily.ui.news;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinyuk.myutils.system.NetWorkUtils;
import com.sinyuk.myutils.system.ToastUtils;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.BaseActivity;
import com.sinyuk.yukdaily.data.news.NewsRepository;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;
import com.sinyuk.yukdaily.databinding.ActivityBrowserBinding;
import com.sinyuk.yukdaily.entity.news.News;

import java.io.File;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by Sinyuk on 16.10.21.
 */

public class BrowserActivity extends BaseActivity {
    public static final String KEY_NEWS_ID = "NEWS_ID";
    @Inject
    File mCacheFile;
    @Inject
    ToastUtils toastUtils;
    @Inject
    NewsRepository newsRepository;
    private ActivityBrowserBinding binding;
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
        }
    };


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

        try {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        } catch (Exception e) {
            e.printStackTrace();
        }


        binding = DataBindingUtil.setContentView(this, R.layout.activity_browser);

        initWebViewSettings();

        final int id = getIntent().getIntExtra(KEY_NEWS_ID, -1);
        if (id != -1) {
            addSubscription(newsRepository.getNews(id).subscribe(observer));
        }
    }

    private void initWebViewSettings() {
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.setWebChromeClient(new MyWebChromeClient());

        // 注入一个Cache path 跟Okhttp一起
        WebSettings webSetting = binding.webView.getSettings();
        webSetting.setAllowFileAccess(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        }
        webSetting.setAllowContentAccess(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        // Zoom
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setDisplayZoomControls(false);

        // 设置缓存
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCachePath(mCacheFile.getPath());
        if (!NetWorkUtils.isNetworkConnection(this)) {
            webSetting.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }

        // 设置数据库
        webSetting.setDatabaseEnabled(false);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);

        webSetting.setGeolocationEnabled(false);

    }

    public void onClose(View v) {
        onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || binding.webView == null || intent.getData() == null) {
            return;
        }
        binding.webView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (binding.webView != null) {
            binding.webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (binding.webView != null) {
            binding.webView.onPause();
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (binding.webView != null && binding.webView.canGoBack()) {
                binding.webView.goBack();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        clearWebView();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        clearWebView();
        super.onDestroy();
    }

    private void clearWebView() {
        if (binding.webView != null) {
            binding.webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            binding.webView.clearHistory();
            binding.webView.removeAllViews();
            ((ViewGroup) binding.getRoot()).removeView(binding.webView);
            binding.webView.destroy();
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                onReceivedErrors(error.getErrorCode());
            } else {
//                onReceivedErrors(-1);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            if (favicon != null) {
//                mFavicon.setImageBitmap(favicon);
//            }
//            mProgressBar.setVisibility(View.VISIBLE);
//            mProgressBar.progressiveStart();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            mProgressBar.setVisibility(View.GONE);
//            mProgressBar.progressiveStop();
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            }
            return super.shouldInterceptRequest(view, request);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {

        }
    }
}
