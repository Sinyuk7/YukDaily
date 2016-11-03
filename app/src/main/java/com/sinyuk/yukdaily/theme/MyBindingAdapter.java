package com.sinyuk.yukdaily.theme;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

public abstract class MyBindingAdapter {

    @BindingAdapter("android:textColor")
    public abstract void setTextColor(TextView view, @ColorInt int color);


    @BindingAdapter("background")
    public abstract void setBackgroundColor(View view, @ColorInt int color);

    protected int getColor(View view, int color) {
        return ContextCompat.getColor(view.getContext(), color);
    }

}