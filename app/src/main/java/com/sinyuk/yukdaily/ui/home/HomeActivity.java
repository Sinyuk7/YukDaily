package com.sinyuk.yukdaily.ui.home;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.sinyuk.myutils.MathUtils;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.BaseActivity;
import com.sinyuk.yukdaily.data.news.NewsRepository;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;
import com.sinyuk.yukdaily.databinding.ActivityHomeBinding;
import com.sinyuk.yukdaily.databinding.LayoutDrawerGankBinding;
import com.sinyuk.yukdaily.databinding.LayoutDrawerNewsBinding;
import com.sinyuk.yukdaily.entity.news.Theme;
import com.sinyuk.yukdaily.events.GankSwitchEvent;
import com.sinyuk.yukdaily.events.HomepageLoadingEvent;
import com.sinyuk.yukdaily.events.ToolbarTitleChangeEvent;
import com.sinyuk.yukdaily.ui.gank.GankFragment;
import com.sinyuk.yukdaily.ui.news.NewsFragment;
import com.sinyuk.yukdaily.ui.search.SearchActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by Sinyuk on 2016/10/13.
 */

public class HomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener, SlidingPaneLayout.PanelSlideListener {
    public final ObservableField<String> gankType = new ObservableField<>();
    public final ObservableField<String> newsType = new ObservableField<>();
    ActivityHomeBinding binding;
    @Inject
    NewsRepository newsRepository;
    private Handler myHandler = new Handler();
    private Runnable mLoadingRunnable = () -> {
        EventBus.getDefault().post(new HomepageLoadingEvent());
    };
    private PopupWindow popupWindow;
    private LayoutDrawerGankBinding drawerGankBinding;
    private LayoutDrawerNewsBinding newsDrawerBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).getAppComponent().plus(new NewsRepositoryModule()).inject(this);
        EventBus.getDefault().register(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        binding.slidingPaneLayout.setPanelSlideListener(this);


        initToolbar();

        if (savedInstanceState == null) {
            EventBus.getDefault().post(new ToolbarTitleChangeEvent(getString(R.string.zhihudaily_slogan)));
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
        drawerGankBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_drawer_gank, null, false);
        drawerGankBinding.setActivity(this);

        drawerGankBinding.getRoot().setFocusable(true);
        drawerGankBinding.getRoot().setFocusableInTouchMode(true);
        drawerGankBinding.getRoot().setOnKeyListener((v1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                popupWindow.dismiss();
                return true;
            }
            return false;
        });

    }

    private void initNewsDrawer() {
        newsDrawerBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_drawer_news, null, false);
        newsDrawerBinding.setActivity(this);
        newsDrawerBinding.getRoot().setFocusable(true);
        newsDrawerBinding.getRoot().setFocusableInTouchMode(true);
        newsDrawerBinding.getRoot().setOnKeyListener((v1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                popupWindow.dismiss();
                return true;
            }
            return false;
        });

        addSubscription(newsRepository.getOtherThemes().subscribe(new Observer<List<Theme>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "getOtherThemes: ");
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Theme> themes) {
                newsDrawerBinding.radioGroup.bind(HomeActivity.this, themes);
            }
        }));
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
    }

    public void onToggleDrawer(View v) {

        if (popupWindow == null) {
            initPopupMenu();
        } else if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }

        if (binding.viewPager.getCurrentItem() == 1) {
            if (drawerGankBinding == null) {
                initGankDrawer();
            }
            popupWindow.setContentView(drawerGankBinding.getRoot());
        } else if (binding.viewPager.getCurrentItem() == 0) {
            if (newsDrawerBinding == null) {
                initNewsDrawer();
            }
            popupWindow.setContentView(newsDrawerBinding.getRoot());
        } else {
            return;
        }
        popupWindow.showAtLocation(v, Gravity.TOP | Gravity.END, 0, 0);
    }


    public void onClickSearch(View v) {
        if (binding.viewPager.getCurrentItem() == 0) {
            SearchActivity.start(this, SearchActivity.TYPE_ZHIHU);
        } else if (binding.viewPager.getCurrentItem() == 1) {
            SearchActivity.start(this, SearchActivity.TYPE_GANK);
        }
    }

    public void onDrawerItemSelected(View v) {
        switch (v.getId()) {
            case R.id.item_anzhuo:
                EventBus.getDefault().post(new GankSwitchEvent(getString(R.string.item_anzhuo)));
                break;
            case R.id.item_ios:
                EventBus.getDefault().post(new GankSwitchEvent(getString(R.string.item_ios)));
                break;
            case R.id.item_frontend:
                EventBus.getDefault().post(new GankSwitchEvent(getString(R.string.item_frontend)));
                break;
            case R.id.item_plus:
                EventBus.getDefault().post(new GankSwitchEvent(getString(R.string.item_plus)));
                break;
            case R.id.item_fuli:
                EventBus.getDefault().post(new GankSwitchEvent(getString(R.string.item_fuli)));
                break;
        }
        Log.d(TAG, "onDrawerItemSelected: " + ((AppCompatRadioButton) v).getText());

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
            if (binding.viewPager.getCurrentItem() == 0) {
                if (TextUtils.isEmpty(newsType.get())) {
                    EventBus.getDefault().post(new ToolbarTitleChangeEvent(getString(R.string.zhihudaily_slogan)));
                } else {
                    EventBus.getDefault().post(new ToolbarTitleChangeEvent(newsType.get()));
                }

            } else if (binding.viewPager.getCurrentItem() == 1) {
                if (TextUtils.isEmpty(gankType.get())) {
                    EventBus.getDefault().post(new ToolbarTitleChangeEvent(getString(R.string.gank_slogan)));
                } else {
                    EventBus.getDefault().post(new ToolbarTitleChangeEvent(gankType.get()));
                }
            }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToolbarTitleChange(ToolbarTitleChangeEvent event) {
        if (!TextUtils.isEmpty(event.getTitle())) {
            binding.textSwitcher.setCurrentText(event.getTitle());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGankSwitch(GankSwitchEvent event) {
        if (!TextUtils.isEmpty(event.getType()) && !event.getType().equals(gankType.get())) {
            gankType.set(event.getType());
        }
    }
}
