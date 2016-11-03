package com.sinyuk.yukdaily.theme;

/**
 * Created by Sinyuk on 16.11.3.
 */
public class LightThemeComponent implements android.databinding.DataBindingComponent {

    private MyBindingAdapter adapter = new LightThemeAdapter();

    @Override
    public MyBindingAdapter getMyBindingAdapter() {
        return adapter;
    }

}
