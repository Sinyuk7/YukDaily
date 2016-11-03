package com.sinyuk.yukdaily.ui;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinyuk.myutils.text.DateUtils;
import com.sinyuk.yukdaily.ui.news.CardPagerAdapter;
import com.sinyuk.yukdaily.utils.cardviewpager.ShadowTransformer;
import com.sinyuk.yukdaily.widgets.CircleIndicator;

import java.util.Date;
import java.util.List;

/**
 * Created by Sinyuk on 16.11.3.
 */

public final class BindMethods {
    @BindingAdapter("imageUrl")
    public static void loadImage(android.databinding.DataBindingComponent component, ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade(300)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }


    @BindingAdapter("imageUrl")
    public static void loadThumbnail(android.databinding.DataBindingComponent component, ImageView imageView, List<String> images) {
        if (images == null || images.get(0) == null) {
            return;
        }

        Glide.with(imageView.getContext())
                .load(images.get(0))
                .crossFade(300)
                .into(imageView);
    }

    @BindingAdapter("date")
    public static void setDate(android.databinding.DataBindingComponent component, TextView textView, long time) {
        time = time * 1000;
        textView.setText(DateUtils.getTimeAgo(textView.getContext(), new Date(time)));
    }


    // news fragment
    @BindingAdapter({"adapter", "indicator"})
    public static void setAdapter(android.databinding.DataBindingComponent component, ViewPager vp, CardPagerAdapter adapter, CircleIndicator indicator) {
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(adapter);
        indicator.setViewPager(vp);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        final ShadowTransformer transformer = new ShadowTransformer(vp, adapter);
        vp.setPageTransformer(false, transformer);
        transformer.enableScaling(true);
    }
}
