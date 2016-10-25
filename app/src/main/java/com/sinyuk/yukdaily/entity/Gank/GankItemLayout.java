package com.sinyuk.yukdaily.entity.Gank;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.GankItemCellBinding;

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

    @BindingAdapter("res")
    public static void setRes(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
    }

    public void bindData(List<GankData> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) == null) { continue; }
            final GankData data = dataList.get(i);

            GankItemCellBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.gank_item_cell, this, false);

            binding.setData(data);
            binding.authorAndDate.setText(data.getAuthor() + data.getPublishedAt());

            final OnClickListener listener = v -> Log.d(TAG, "onClick: " + data.getUrl());
            binding.title.setOnClickListener(listener);
            if (data.getImages() != null && !data.getImages().isEmpty()) {
                binding.image.setVisibility(VISIBLE);
                binding.image.setOnClickListener(listener);
                Glide.with(getContext()).load(data.getImages().get(0)).into(binding.image);
            }

            LinearLayout.LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (i < dataList.size() - 1) {
                lps.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.content_space_8);
            }
            if (i == 0) {
                lps.topMargin = getResources().getDimensionPixelOffset(R.dimen.content_space_8);
            }
            lps.rightMargin = getResources().getDimensionPixelOffset(R.dimen.content_space_8);
            lps.leftMargin = getResources().getDimensionPixelOffset(R.dimen.content_space_8);
            addView(binding.getRoot(), lps);
        }
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
