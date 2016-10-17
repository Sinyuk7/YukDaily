package com.sinyuk.yukdaily;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sinyuk.myutils.intents.ActivityUtils;
import com.sinyuk.yukdaily.base.BaseActivity;
import com.sinyuk.yukdaily.ui.news.NewsFragment;

/**
 * Created by Sinyuk on 2016/10/13.
 */

public class NewsListDemo extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        NewsFragment fragment = new NewsFragment();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.root);
    }
}
