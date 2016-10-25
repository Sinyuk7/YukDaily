package com.sinyuk.yukdaily.ui.gank;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sinyuk.yukdaily.BR;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.GankItemBinding;
import com.sinyuk.yukdaily.entity.Gank.GankResult;
import com.sinyuk.yukdaily.utils.binding.BindingViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sinyuk on 16.10.25.
 */

public class GankAllAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private List<GankResult> dataset = new ArrayList<>();

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final GankItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.gank_item, parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        if (dataset.get(position) != null) {
            holder.getBinding().setVariable(BR.result, dataset.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public void setData(List<GankResult> data) {
        dataset.clear();
        dataset.addAll(data);
        notifyDataSetChanged();
    }

    public void appendData(List<GankResult> data) {
        final int start = dataset.size();
        dataset.addAll(data);
        notifyItemRangeInserted(start, data.size());
    }
}
