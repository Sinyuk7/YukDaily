package com.sinyuk.yukdaily.ui.theme;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sinyuk.myutils.resource.StringUtils;
import com.sinyuk.yukdaily.BR;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.ThemeEditorItemBinding;
import com.sinyuk.yukdaily.entity.news.ThemeData;
import com.sinyuk.yukdaily.utils.AvatarHelper;
import com.sinyuk.yukdaily.utils.binding.BindingViewHolder;
import com.sinyuk.yukdaily.utils.glide.CropCircleTransformation;
import com.sinyuk.yukdaily.widgets.TextDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sinyuk on 16.11.1.
 */

public class EditorAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    public static final String TAG = "EditorAdapter";
    private List<ThemeData.Editor> editorList = new ArrayList<>();

    @BindingAdapter({"avatar", "name"})
    public static void loadAvatar(ImageView imageView, String avatarUrl, String name) {
        final TextDrawable placeholder = AvatarHelper.createTextDrawable(StringUtils.valueOrDefault(name, " "), imageView.getContext());
        Glide.with(imageView.getContext())
                .load(avatarUrl)
                .bitmapTransform(new CropCircleTransformation(imageView.getContext()))
                .dontAnimate()
                .placeholder(placeholder)
                .error(placeholder)
                .into(imageView);
    }

    public void setData(List<ThemeData.Editor> data) {
        Log.d(TAG, "setData: " + data.toString());
        editorList.clear();
        editorList.addAll(data);
        notifyDataSetChanged();
    }


    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ThemeEditorItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.theme_editor_item, parent, false);
        return new BindingViewHolder<>(binding);
    }

    public void onItemClick(View v, ThemeData.Editor editor) {

    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        if (editorList.get(position) == null) { return; }
        holder.getBinding().setVariable(BR.adapter, this);
        holder.getBinding().setVariable(BR.editor, editorList.get(position));
    }

    @Override
    public int getItemCount() {
        return editorList == null ? 0 : editorList.size();
    }
}
