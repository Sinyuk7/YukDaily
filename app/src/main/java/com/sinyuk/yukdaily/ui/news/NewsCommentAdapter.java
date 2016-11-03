package com.sinyuk.yukdaily.ui.news;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sinyuk.myutils.text.DateUtils;
import com.sinyuk.yukdaily.BR;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.NewCommentItemBinding;
import com.sinyuk.yukdaily.entity.news.NewsComment;
import com.sinyuk.yukdaily.utils.binding.BindingViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sinyuk on 16.10.24.
 */

public class NewsCommentAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    public static final String TAG = "NewsCommentAdapter";
    private final Context context;
    private List<NewsComment> comments = new ArrayList<>();

    public NewsCommentAdapter(Context context, List<NewsComment> comments) {
        this.context = context;
        this.comments.addAll(comments);
        setHasStableIds(true);
    }


    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final NewCommentItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.new_comment_item, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        if (comments.get(position) != null) {
            holder.getBinding().setVariable(BR.comment, comments.get(position));
            holder.getBinding().setVariable(BR.context, context);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public long getItemId(int position) {
        if (comments.get(position) != null) {
            return comments.get(position).getId();
        }
        return RecyclerView.NO_ID;
    }
}
