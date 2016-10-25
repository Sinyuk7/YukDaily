package com.sinyuk.yukdaily.entity.Gank;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinyuk.yukdaily.R;

import java.util.List;

/**
 * Created by Sinyuk on 16.10.25.
 */

public class GankItemLayout extends LinearLayout {
    public static final String TAG = "GankItemLayout";

    public GankItemLayout(Context context) {
        this(context, null);
    }

    public GankItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GankItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @BindingAdapter("dataset")
    public static void bindData(GankItemLayout container, List<GankData> dataList) {
        for (int i = 0; i < dataList.size(); i++) {


            TextView item = container.newItem();
            item.setText(item.getText() + dataList.get(i).getTitle());
            int finalI = i;
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + dataList.get(finalI).getUrl());
                }
            });

            container.addView(item);

        }
    }

    @BindingAdapter("res")
    public static void setRes(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
    }


    private TextView newItem() {
        TextView item = new TextView(getContext());
        LinearLayout.LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        item.setLayoutParams(lps);
        item.setMaxLines(3);
        item.setTextSize(18);
        item.setTextColor(ContextCompat.getColor(getContext(), R.color.text_primary_dark));
        item.setText("丶 ");
        return item;
    }
}
