package com.sinyuk.yukdaily.ui.gank;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sinyuk.myutils.text.DateUtils;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.GankItemBinding;
import com.sinyuk.yukdaily.databinding.GankItemCellBinding;
import com.sinyuk.yukdaily.databinding.GankItemLayoutBinding;
import com.sinyuk.yukdaily.entity.Gank.GankData;
import com.sinyuk.yukdaily.entity.Gank.GankResult;
import com.sinyuk.yukdaily.utils.binding.BindingViewHolder;
import com.sinyuk.yukdaily.utils.span.AndroidSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sinyuk on 16.10.25.
 */

public class GankAllAdapter extends RecyclerView.Adapter<GankAllAdapter.GankItemHolder> {
    public static final String TAG = "GankAllAdapter";
    private List<GankResult> dataset = new ArrayList<>();


    @BindingAdapter("gankData")
    public static void bindGankData(LinearLayout viewGroup, List<GankData> dataList) {

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i) == null) { continue; }
            final GankData data = dataList.get(i);

            GankItemCellBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.gank_item_cell, viewGroup, false);
            binding.setData(data);
            Date date = new Date();
            try {
                date = formatter.parse(data.getPublishedAt());
            } catch (ParseException e) {
                e.printStackTrace();
                date.setTime(0);
            }

            binding.authorAndDate.setText(
                    new AndroidSpan()
                            .drawTextAppearanceSpan(data.getAuthor() + "/", viewGroup.getContext(), R.style.gank_author)
                            .drawTextAppearanceSpan(DateUtils.getTimeAgo(viewGroup.getContext(), date),
                                    viewGroup.getContext(), R.style.gank_date)
                            .getSpanText());

            binding.title.setOnClickListener(v -> Log.d(TAG, "onClick: " + data.getUrl()));


            LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            final Resources res = viewGroup.getContext().getResources();
            if (res == null) { return; }
            if (i == 0) {
                lps.topMargin = res.getDimensionPixelOffset(R.dimen.content_space_8);
            }
            lps.bottomMargin = res.getDimensionPixelOffset(R.dimen.content_space_8);
//            lps.rightMargin = res.getDimensionPixelOffset(R.dimen.content_space_16);
//            lps.leftMargin = res.getDimensionPixelOffset(R.dimen.content_space_16);

            viewGroup.addView(binding.getRoot(), lps);
        }
    }

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

            if (result.getAnzhuo() != null && !result.getAnzhuo().isEmpty()) {
                holder.getBinding().androidLayout.setOnInflateListener((stub, inflated) -> {
                    GankItemLayoutBinding binding = DataBindingUtil.bind(inflated);
                    binding.setGankData(result.getAnzhuo());
                    binding.type.setImageResource(R.drawable.ic_android);
                });

                if (!holder.getBinding().androidLayout.isInflated()) {
                    holder.getBinding().androidLayout.getViewStub().inflate();
                    Log.d(TAG, "inflate android: ");
                }
            }
            if (result.getIos() != null && !result.getIos().isEmpty()) {

                holder.getBinding().iOSLayout.setOnInflateListener((stub, inflated) -> {
                    GankItemLayoutBinding binding = DataBindingUtil.bind(inflated);
                    binding.setGankData(result.getIos());
                    binding.type.setImageResource(R.drawable.ic_ios);
                });

                if (!holder.getBinding().iOSLayout.isInflated()) {
                    holder.getBinding().iOSLayout.getViewStub().inflate();
                    Log.d(TAG, "inflate ios: ");
                }
            }

            if (result.getFrontEnd() != null && !result.getFrontEnd().isEmpty()) {

                holder.getBinding().frontEndLayout.setOnInflateListener((stub, inflated) -> {
                    GankItemLayoutBinding binding = DataBindingUtil.bind(inflated);
                    binding.setGankData(result.getFrontEnd());
                    binding.type.setImageResource(R.drawable.ic_frontend);
                });

                if (!holder.getBinding().frontEndLayout.isInflated()) {
                    holder.getBinding().frontEndLayout.getViewStub().inflate();
                    Log.d(TAG, "inflate frontEnd: ");
                }
            }

            if (result.getPlus() != null && !result.getPlus().isEmpty()) {

                holder.getBinding().plusLayout.setOnInflateListener((stub, inflated) -> {
                    GankItemLayoutBinding binding = DataBindingUtil.bind(inflated);
                    binding.setGankData(result.getPlus());
                    binding.type.setImageResource(R.drawable.ic_plus);
                });

                if (!holder.getBinding().plusLayout.isInflated()) {
                    holder.getBinding().plusLayout.getViewStub().inflate();
                    Log.d(TAG, "inflate plus: ");
                }
            }
            holder.getBinding().executePendingBindings();

        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setData(GankResult data) {
        dataset.clear();
        dataset.add(data);
        notifyDataSetChanged();
    }

    public void appendData(GankResult data) {
        Log.d(TAG, "appendData: " + data.getAnzhuo().toString());
        final int start = dataset.size();
        Log.d(TAG, "appendData at : " + start);
        dataset.add(data);
//        notifyItemInserted(start);
        notifyDataSetChanged();
    }

    class GankItemHolder extends BindingViewHolder<GankItemBinding> {

        GankItemHolder(GankItemBinding binding) {
            super(binding);
        }


    }
}
