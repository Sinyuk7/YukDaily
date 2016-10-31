package com.sinyuk.yukdaily.ui.theme;

import android.content.Context;
import android.util.Log;

import com.sinyuk.myutils.system.ToastUtils;
import com.sinyuk.yukdaily.App;
import com.sinyuk.yukdaily.base.ListFragment;
import com.sinyuk.yukdaily.data.news.NewsRepositoryModule;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Sinyuk on 16.10.31.
 */

public class ThemeFragment extends ListFragment {
    public static final String TAG = "ThemeFragment";
    @Inject
    Lazy<ToastUtils> toastUtilsLazy;
    private String theme;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.get(context).getAppComponent().plus(new NewsRepositoryModule()).inject(this);
    }

    @Override
    protected void refreshData() {

    }

    @Override
    protected void fetchData() {

    }

    public void setTheme(String theme, int index) {
        this.theme = theme;
//        toastUtilsLazy.get().toastShort(theme);
        Log.d(TAG, "setTheme: " + theme);
    }
}
