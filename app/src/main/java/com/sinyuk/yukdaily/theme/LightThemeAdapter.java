package com.sinyuk.yukdaily.theme;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.TextView;

import com.sinyuk.yukdaily.R;

/**
 * Created by Sinyuk on 16.11.3.
 */

public class LightThemeAdapter extends MyBindingAdapter{
    @Override
    public void setTextColor(TextView view, @ColorInt int color) {
        if (color == getColor(view, R.color.text_primary_light)) {
            view.setTextColor(getColor(view, R.color.text_primary_dark));
        } else if (color == getColor(view, R.color.text_secondary_light)) {
            view.setTextColor(getColor(view, R.color.text_secondary_dark));
        }
    }

    @Override
    public void setBackgroundColor(View view, @ColorInt int color) {
        view.setBackground(new ColorDrawable(color));
    }
}
