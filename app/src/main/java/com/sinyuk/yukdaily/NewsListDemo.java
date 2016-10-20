package com.sinyuk.yukdaily;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.sinyuk.yukdaily.base.BaseActivity;
import com.sinyuk.yukdaily.databinding.ActivityHomeBinding;
import com.sinyuk.yukdaily.ui.gank.GankFragment;
import com.sinyuk.yukdaily.ui.news.NewsFragment;

/**
 * Created by Sinyuk on 2016/10/13.
 */

public class NewsListDemo extends BaseActivity implements ViewPager.OnPageChangeListener {
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        setupViewPager();

    }

    private void setupViewPager() {
        NewsFragment newsFragment = new NewsFragment();
        GankFragment gankFragment = new GankFragment();

        binding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return newsFragment;
                    case 1:
                        return gankFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        binding.viewPager.addOnPageChangeListener(this);

        binding.viewPager.setOffscreenPageLimit(1);

        binding.navigationTabStrip.setViewPager(binding.viewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
