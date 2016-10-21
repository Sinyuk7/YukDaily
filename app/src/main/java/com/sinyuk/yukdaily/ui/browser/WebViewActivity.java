package com.sinyuk.yukdaily.ui.browser;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.ActivityWebViewBinding;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by Sinyuk on 16.10.21.
 */

public class WebViewActivity extends BaseWebActivity {
    @Inject
    File mCacheFile;
    private ActivityWebViewBinding binding;

    public static void open(Context context, String url) {
        Intent starter = new Intent(context, WebViewActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).getAppComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);

        initWebViewSettings(binding.webView, mCacheFile);

        loadUrlFromIntent();
    }

    @Override
    protected void initWebViewSettings(WebView webView, File cache) {
        super.initWebViewSettings(webView, cache);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    return false;
                }
                view.loadUrl(request.getUrl().toString());
                return true;

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.getDataString();
        if (!TextUtils.isDigitsOnly(url)) {
            if (binding.webView != null) {
                binding.webView.loadUrl(url);
            }
        }
    }
}
