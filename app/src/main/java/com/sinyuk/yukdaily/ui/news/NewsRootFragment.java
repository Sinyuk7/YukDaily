package com.sinyuk.yukdaily.ui.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.BaseFragment;
import com.sinyuk.yukdaily.events.NewsSwitchEvent;
import com.sinyuk.yukdaily.ui.theme.ThemeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Sinyuk on 16.10.31.
 */

public class NewsRootFragment extends BaseFragment {
    private NewsFragment newsFragment;
    private ThemeFragment themeFragment;
    private FragmentManager manager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getChildFragmentManager();
        newsFragment = new NewsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_root, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!homeAdded()) {
            manager.beginTransaction().add(R.id.root, newsFragment, NewsFragment.TAG).commit();
        }
    }

    private boolean themeAdded() {
        return manager.findFragmentByTag(ThemeFragment.TAG) != null;
    }

    private boolean homeAdded() {
        return manager.findFragmentByTag(NewsFragment.TAG) != null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewsSwitch(NewsSwitchEvent event) {
        Log.d(TAG, "onNewsSwitch: " + event.getIndex());
        if (Integer.MIN_VALUE== (event.getIndex())) {
            showHome();
        } else {

            showTheme();
            themeFragment.setTheme(event.getIndex());
        }
    }

    private void showTheme() {
        if (themeFragment == null) {
            themeFragment = new ThemeFragment();
        }

        if (!themeAdded()) {
            manager.beginTransaction().add(R.id.root, themeFragment, ThemeFragment.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        } else {
            manager.beginTransaction().show(manager.findFragmentByTag(ThemeFragment.TAG))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }

        if (homeAdded()) {
            manager.beginTransaction().hide(manager.findFragmentByTag(NewsFragment.TAG))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
    }

    private void showHome() {
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
        }

        if (!homeAdded()) {
            manager.beginTransaction().add(R.id.root, newsFragment, NewsFragment.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        } else {
            manager.beginTransaction().show(manager.findFragmentByTag(NewsFragment.TAG))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }

        if (themeAdded()) {
            manager.beginTransaction().hide(manager.findFragmentByTag(ThemeFragment.TAG))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
        }
    }
}
