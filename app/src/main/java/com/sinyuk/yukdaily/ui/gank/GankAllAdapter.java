package com.sinyuk.yukdaily.ui.gank;

import android.databinding.DataBindingUtil;
import android.databinding.ViewStubProxy;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sinyuk.yukdaily.BR;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.GankItemBinding;
import com.sinyuk.yukdaily.databinding.GankItemLayoutBinding;
import com.sinyuk.yukdaily.entity.Gank.GankData;
import com.sinyuk.yukdaily.entity.Gank.GankResult;
import com.sinyuk.yukdaily.utils.binding.BindingViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sinyuk on 16.10.25.
 */

public class GankAllAdapter extends RecyclerView.Adapter<GankAllAdapter.GankItemHolder> {
    public static final String TAG = "GankAllAdapter";
    private List<GankResult> dataset = new ArrayList<>();

    @Override
    public GankItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final GankItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.gank_item, parent, false);
        return new GankItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(GankItemHolder holder, int position) {
        if (dataset.get(position) != null) {
            final GankResult result = dataset.get(position);
            holder.getBinding().setVariable(BR.result, result);

            holder.getBinding().androidStub.setOnInflateListener((stub, inflated) -> {
                GankItemLayoutBinding binding = DataBindingUtil.bind(inflated);
                binding.content.bindData(result.getAnzhuo());
                binding.type.setImageResource(R.drawable.avatar);
            });

            holder.getBinding().iosStub.setOnInflateListener((stub, inflated) -> {
                GankItemLayoutBinding binding = DataBindingUtil.bind(inflated);
                binding.content.bindData(result.getIos());
                binding.type.setImageResource(R.drawable.avatar);
            });

            holder.getBinding().frontEndStub.setOnInflateListener((stub, inflated) -> {
                GankItemLayoutBinding binding = DataBindingUtil.bind(inflated);
                binding.content.bindData(result.getFrontEnd());
                binding.type.setImageResource(R.drawable.avatar);
            });

            holder.getBinding().plusStub.setOnInflateListener((stub, inflated) -> {
                GankItemLayoutBinding binding = DataBindingUtil.bind(inflated);
                binding.content.bindData(result.getPlus());
                binding.type.setImageResource(R.drawable.avatar);
            });


            inflate(result.getAnzhuo(), holder.getBinding().androidStub);
            inflate(result.getIos(), holder.getBinding().iosStub);
            inflate(result.getFrontEnd(), holder.getBinding().frontEndStub);
            inflate(result.getPlus(), holder.getBinding().plusStub);
        }
    }

    private void inflate(List<GankData> datas, ViewStubProxy stub) {
        if (datas == null) {
            Log.d(TAG, "inflate: android is NULL");
        } else if (datas.isEmpty()) {
            Log.d(TAG, "inflate: android is empty");
        }

        if (datas != null && !datas.isEmpty()) {
            if (!stub.isInflated()) {
                stub.getViewStub().inflate();
                Log.d(TAG, "inflate: ");
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public void setData(GankResult data) {
        Log.d(TAG, "setData: " + data.getAnzhuo().toString());
        dataset.clear();
        dataset.add(data);
        notifyDataSetChanged();
    }

    public void appendData(GankResult data) {
        final int start = dataset.size();
        dataset.add(data);
        notifyItemInserted(start);
    }

    class GankItemHolder extends BindingViewHolder<GankItemBinding> {

        GankItemHolder(GankItemBinding binding) {
            super(binding);
        }


    }
}
