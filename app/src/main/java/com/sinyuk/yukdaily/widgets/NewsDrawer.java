package com.sinyuk.yukdaily.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.sinyuk.myutils.system.ScreenUtils;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.entity.news.Theme;
import com.sinyuk.yukdaily.ui.home.HomeActivity;

import java.util.List;

/**
 * Created by Sinyuk on 16.10.31.
 */

public class NewsDrawer extends RadioGroup {
    private final int dp16;
    private final LinearLayout.LayoutParams lps;
    private final int textcolor;


    public NewsDrawer(Context context) {
        this(context, null);
    }

    public NewsDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        dp16 = ScreenUtils.dpToPxInt(context, 16);
        lps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lps.height = ScreenUtils.dpToPxInt(context, 40);
        lps.leftMargin = dp16 / 2;
        lps.rightMargin = dp16;
        textcolor = ContextCompat.getColor(context, R.color.text_secondary_dark);
    }

    public void bind(HomeActivity activity,List<Theme> themes) {
        for (int i = 0; i < themes.size(); i++) {
            if (i == 0) {
                AppCompatRadioButton button = new AppCompatRadioButton(getContext());
                decorate(activity,button);
                button.setText(getContext().getString(R.string.item_news_index));
                button.setOnClickListener(v -> activity.onThemeItemSelected(button, Integer.MIN_VALUE));
                addView(button, lps);
            }
            AppCompatRadioButton button = new AppCompatRadioButton(getContext());
            decorate(activity,button);
            button.setText(themes.get(i).getName());
            int finalI = i;
            button.setOnClickListener(v -> activity.onThemeItemSelected(button, themes.get(finalI).getId()));
            addView(button, lps);
        }
    }

    private void decorate(HomeActivity activity,AppCompatRadioButton button) {
        button.setTextColor(textcolor);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        button.setPadding(dp16 / 2, 0, dp16, 0);
        button.setGravity(GravityCompat.START | Gravity.CENTER_VERTICAL);

    }
}
