package com.sinyuk.yukdaily.ui.news;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sinyuk.myutils.system.ScreenUtils;
import com.sinyuk.yukdaily.BR;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.NewsHeaderBinding;
import com.sinyuk.yukdaily.databinding.NewsItemBinding;
import com.sinyuk.yukdaily.model.Story;
import com.sinyuk.yukdaily.utils.binding.BindingViewHolder;
import com.sinyuk.yukdaily.utils.cardviewpager.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sinyuk on 16.1.4.
 * 有header和footer的recycleView
 */
public class NewsAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    public static final String TAG = "NewsAdapter";
    private static final int HEADER_ID = Integer.MAX_VALUE;
    private static final int ITEM_TYPE_HEADER = Integer.MAX_VALUE;
    private final Context context;
    private List<Story> stories = new ArrayList<>();
    private List<Story> topStories = new ArrayList<>();


    public NewsAdapter(Context context) {
        this.context = context;
    }

    @BindingAdapter("data")
    public static void setHeaderPages(ViewPager viewPager, List<Story> data) {
        if (viewPager.getAdapter() != null) {

            ((CardPagerAdapter) viewPager.getAdapter()).setData(data);
            //
            if (viewPager.getChildCount() >= 3) {
                viewPager.setCurrentItem(1);
            }

            final ShadowTransformer transformer = new ShadowTransformer(viewPager, (CardPagerAdapter) viewPager.getAdapter());
            viewPager.setPageTransformer(false, transformer);

            transformer.enableScaling(true);
        }
    }

    @BindingAdapter("imageUrl")
    public static void loadThumbnail(ImageView imageView, List<String> images) {
        if (images == null || images.get(0) == null) return;
        Glide.with(imageView.getContext())
                .load(images.get(0))
                .crossFade(500)
                .into(imageView);
    }

    private int getDataItemCount() {
        return stories == null ? 0 : stories.size();
    }

    private boolean hasHeader() {
        return !topStories.isEmpty();
    }

    public void addHeader(List<Story> data) {
        if (!topStories.isEmpty()) {
            topStories.clear();
        }
        topStories.addAll(data);
//        notifyItemRangeInserted(0, getItemCount());
        notifyDataSetChanged();
    }

    public void removeHeader() {
        topStories.clear();
        notifyItemRemoved(0);
    }

    @Override
    public long getItemId(int position) {
        if (position == 0 && hasHeader()) {
            return HEADER_ID;
        }
        if (stories.get(itemPositionInData(position)) != null) {
            return stories.get(itemPositionInData(position)).getId();
        }
        return RecyclerView.NO_ID;
    }

    public void setData(List<Story> data) {
        stories.clear();
        stories.addAll(data);
        notifyItemRangeInserted(itemPositionInData(0), data.size());
    }

    public void appendData(List<Story> data) {
        final int start = stories.size();
        stories.addAll(data);
        notifyItemRangeInserted(itemPositionInData(start), data.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && hasHeader()) {
            return ITEM_TYPE_HEADER;
        }
        return super.getItemViewType(itemPositionInData(position));
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            NewsHeaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.news_header, parent, false);
            return new HeaderViewHolder(binding);
        } else {
            NewsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.news_item, parent, false);
            return new ItemViewHolder(binding);
        }
    }

    private int itemPositionInData(int rvPosition) {
        return rvPosition - (hasHeader() ? 1 : 0);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_TYPE_HEADER) {
            holder.getBinding().setVariable(BR.topStories, topStories);
        } else {
            if (stories.get(itemPositionInData(position)) != null)
                holder.getBinding().setVariable(BR.story, stories.get(itemPositionInData(position)));
        }
    }

    @Override
    public int getItemCount() {
        return hasHeader() ? getDataItemCount() + 1 : getDataItemCount();
    }

    private class ItemViewHolder extends BindingViewHolder<NewsItemBinding> {
        ItemViewHolder(NewsItemBinding binding) {
            super(binding);
        }
    }

    private class HeaderViewHolder extends BindingViewHolder<NewsHeaderBinding> {

        HeaderViewHolder(NewsHeaderBinding binding) {
            super(binding);
            final CardPagerAdapter adapter = new CardPagerAdapter();
            mBinding.viewPager.setAdapter(adapter);
            mBinding.viewPager.setOffscreenPageLimit(3);
            mBinding.viewPager.setPageMargin(ScreenUtils.dpToPxInt(mBinding.viewPager.getContext(), 16));
        }
    }


}



