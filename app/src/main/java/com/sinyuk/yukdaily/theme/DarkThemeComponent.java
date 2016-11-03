package com.sinyuk.yukdaily.theme;

import dagger.Component;

/**
 * Created by Sinyuk on 16.11.3.
 */
public class DarkThemeComponent implements android.databinding.DataBindingComponent {
    private MyBindingAdapter adapter = new DarkThemeAdapter();

    @Override
    public MyBindingAdapter getMyBindingAdapter() {
        return adapter;
    }
}
