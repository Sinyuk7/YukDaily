package com.sinyuk.yukdaily.ui.browser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sinyuk.yukdaily.base.BaseActivity;

import java.io.File;
import java.util.List;

/**
 * Created by Sinyuk on 16.10.21.
 */

public class BaseWebActivity extends BaseActivity {
    protected static final String KEY_URL = "URL";
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CallSuper
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    protected void initWebViewSettings(final WebView webView, final File cache) {

        mWebView = webView;

        webView.setWebChromeClient(new MyWebChromeClient());

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);


        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        }

        webSetting.setAllowContentAccess(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);


        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);


        // 支持通过js打开新的窗口
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);

        // 开启DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        // 开启database storage API功能
        webSetting.setDatabaseEnabled(true);

        // 设置缓存
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCachePath(cache.getPath());

        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webSetting.setJavaScriptEnabled(true);

        webSetting.setGeolocationEnabled(false);

    }

    protected void setWebViewTitle(String title) {
        // use the title
    }

    protected void hideProgressBar() {

    }

    protected void showProgressBar(int progress) {

    }

    protected void setWebViewIcon(Bitmap icon) {
        // use the favicon
    }

    public void onClose(View v) {
        onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        clearWebView();
        super.onDestroy();
    }

    private void clearWebView() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleUrl(intent.getDataString());
    }

    protected void handleUrl(String url) {
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
                    break;
                }
            }
        } else {
            if (mWebView != null) {
                mWebView.loadUrl(url);
            }
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress > 0) {
                showProgressBar(newProgress);
            } else if (100 == newProgress) {
                hideProgressBar();
            }
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            setWebViewIcon(icon);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            setWebViewTitle(title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            result.cancel();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            return true;
        }
    }

}
