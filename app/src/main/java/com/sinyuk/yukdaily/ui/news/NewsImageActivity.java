package com.sinyuk.yukdaily.ui.news;

import android.content.Context;
import android.content.Intent;

import com.sinyuk.yukdaily.base.BaseActivity;

/**
 * Created by Sinyuk on 16.10.21.
 */

public class NewsImageActivity extends BaseActivity{
    private static final String KEY_URL = "URL";

    public static void start(Context context,String url) {
        Intent starter = new Intent(context, NewsImageActivity.class);
        starter.putExtra(KEY_URL,url);
        context.startActivity(starter);
    }
}
