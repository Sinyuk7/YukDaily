package com.sinyuk.yukdaily.ui.news;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sinyuk.yukdaily.BR;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.NewsItemBinding;
import com.sinyuk.yukdaily.model.Story;
import com.sinyuk.yukdaily.utils.binding.BindingViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sinyuk on 16.1.4.
 * 有header和footer的recycleView
 */
public class NewsAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    public static final String TAG = "NewsAdapter";
    private List<Story> stories = new ArrayList<>();

    @BindingAdapter("imageUrl")
    public static void loadThumbnail(ImageView imageView, List<String> images) {
        if (images == null || images.get(0) == null) return;
        Glide.with(imageView.getContext())
                .load(images.get(0))
                .crossFade(500)
                .into(imageView);
    }


    @Override
    public long getItemId(int position) {
        if (stories.get(position) != null) {
            return stories.get(position).getId();
        }
        return RecyclerView.NO_ID;
    }

    public void setData(List<Story> data) {
        Log.d(TAG, "setData: " + data.toString());
        stories.clear();
        stories.addAll(data);
//        notifyItemRangeInserted(0, data.size());
        notifyDataSetChanged();
    }

    public void appendData(List<Story> data) {
        final int start = stories.size();
        stories.addAll(data);
        notifyItemRangeInserted(start, data.size());
    }


    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.news_item, parent, false);
        return new ItemViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        if (stories.get(position) != null)
            holder.getBinding().setVariable(BR.story, stories.get(position));

    }

    @Override
    public int getItemCount() {
        return stories == null ? 0 : stories.size();
    }

    private class ItemViewHolder extends BindingViewHolder<NewsItemBinding> {
        ItemViewHolder(NewsItemBinding binding) {
            super(binding);
        }
    }
}



