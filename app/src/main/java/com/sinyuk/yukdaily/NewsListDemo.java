package com.sinyuk.yukdaily;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.sinyuk.myutils.MathUtils;
import com.sinyuk.yukdaily.base.BaseActivity;
import com.sinyuk.yukdaily.databinding.ActivityHomeBinding;
import com.sinyuk.yukdaily.events.HomepageLoadingEvent;
import com.sinyuk.yukdaily.ui.gank.GankFragment;
import com.sinyuk.yukdaily.ui.news.NewsFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Sinyuk on 2016/10/13.
 */

public class NewsListDemo extends BaseActivity implements ViewPager.OnPageChangeListener, SlidingPaneLayout.PanelSlideListener {
    ActivityHomeBinding binding;
    private Handler myHandler = new Handler();
    private Runnable mLoadingRunnable = () -> {
        EventBus.getDefault().post(new HomepageLoadingEvent());
    };
    private PopupWindow popupWindow;
    private View gankDrawer;
    private View newsDrawer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        binding.slidingPaneLayout.setPanelSlideListener(this);


        initToolbar();

        if (savedInstanceState == null) {
            switchToolbarTitle(0);
        }

        setupViewPager();

//          第三种写法:优化的DelayLoad
        getWindow().getDecorView().post(() -> myHandler.postDelayed(mLoadingRunnable, 500));

    }

    private void initPopupMenu() {
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.shadow_filled));
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
    }

    private void initGankDrawer() {
        gankDrawer = LayoutInflater.from(this).inflate(R.layout.layout_drawer_gank, null);
        gankDrawer.setFocusable(true);
        gankDrawer.setFocusableInTouchMode(true);
        gankDrawer.setOnKeyListener((v1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                popupWindow.dismiss();
                return true;
            }
            return false;
        });
    }

    private void initNewsDrawer() {
        newsDrawer = LayoutInflater.from(this).inflate(R.layout.layout_drawer_news, null);
        newsDrawer.setFocusable(true);
        newsDrawer.setFocusableInTouchMode(true);
        newsDrawer.setOnKeyListener((v1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                popupWindow.dismiss();
                return true;
            }
            return false;
        });
    }

    private void initToolbar() {

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
        Log.d(TAG, "onPageScrolled: position " + position);
        Log.d(TAG, "onPageScrolled: positionOffset " + positionOffset);
        Log.d(TAG, "onPageScrolled: positionOffsetPixels " + positionOffsetPixels);
    }

    public void onToggleDrawer(View v) {

        if (popupWindow == null) {
            initPopupMenu();
        } else if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }

        if (binding.viewPager.getCurrentItem() == 1) {
            if (gankDrawer == null) {
                initGankDrawer();
            }
            popupWindow.setContentView(gankDrawer);
        } else if (binding.viewPager.getCurrentItem() == 0) {
            if (newsDrawer == null) {
                initNewsDrawer();
            }
            popupWindow.setContentView(newsDrawer);
        } else {
            return;
        }
        popupWindow.showAtLocation(v, Gravity.TOP | Gravity.END, 0, 0);
    }


    public void onClickSearch(View v) {

    }

    public void onDrawerItemSelected(View v) {
        switch (v.getId()) {

        }
        Log.d(TAG, "onDrawerItemSelected: " + v.getId());
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: " + position);
//        switch (position) {
//            case 0:
//                binding.backdrop1.setVisibility(View.VISIBLE);
//                binding.backdrop2.setVisibility(View.GONE);
//                return;
//            case 1:
//                binding.backdrop2.setVisibility(View.VISIBLE);
//                binding.backdrop1.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            switchToolbarTitle(binding.viewPager.getCurrentItem());
        }
    }

    private void switchToolbarTitle(int index) {
        switch (index) {
            case 0:
                binding.textSwitcher.setCurrentText(getString(R.string.zhihudaily_slogan));
                return;
            case 1:
                binding.textSwitcher.setCurrentText(getString(R.string.gank_slogan));
        }
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        binding.menu.setAlpha(MathUtils.constrain(0, 1, (1 - slideOffset)));
    }

    public void onTogglePanel(View v) {
        if (binding.slidingPaneLayout.isOpen()) {
            binding.slidingPaneLayout.closePane();
        } else {
            binding.slidingPaneLayout.openPane();
        }
    }

    @Override
    public void onPanelOpened(View panel) {

    }

    @Override
    public void onPanelClosed(View panel) {

    }

    @Override
    public void onBackPressed() {
        if (binding.slidingPaneLayout != null && binding.slidingPaneLayout.isOpen()) {
            binding.slidingPaneLayout.closePane();
        } else {
            super.onBackPressed();
        }
    }
}
