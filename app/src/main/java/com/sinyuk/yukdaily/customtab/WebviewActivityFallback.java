package com.sinyuk.yukdaily.customtab;

import android.app.Activity;
import android.net.Uri;

import com.sinyuk.yukdaily.ui.browser.WebViewActivity;

/**
 * A Fallback that opens a WebviewActivity when Custom Tabs is not available
 */
public class WebviewActivityFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Activity activity, Uri uri) {
        WebViewActivity.open(activity, uri.toString());
    }
}
