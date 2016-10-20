package com.sinyuk.yukdaily.ui.news;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sinyuk.yukdaily.BR;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.NewsHeaderItemBinding;
import com.sinyuk.yukdaily.entity.news.Story;
import com.sinyuk.yukdaily.utils.cardviewpager.CardAdapter;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    public static final String TAG = "CardPagerAdapter";
    private List<CardView> mViews = new ArrayList<>();
    private float mBaseElevation;
    private List<Story> topStories = new ArrayList<>();


    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade(300)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.size() > position ? mViews.get(position) : null;
    }

    @Override
    public int getCardCount() {
        return getCount();
    }


    @Override
    public int getCount() {
        return topStories == null ? 0 : topStories.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final NewsHeaderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),
                R.layout.news_header_item, container, false);

        binding.setVariable(BR.story, topStories.get(position));

        container.addView(binding.getRoot());

        if (mBaseElevation == 0) {
            mBaseElevation = binding.cardView.getCardElevation();
        }
        binding.cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, binding.cardView);
        return binding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    public void setData(List<Story> stories) {
        Log.d(TAG, "setData: " + stories.toString());
        topStories.clear();
        topStories.addAll(stories);
        mViews.clear();
        for (int i = 0; i < stories.size(); i++) {
            mViews.add(null);
        }
        notifyDataSetChanged();
    }
}
