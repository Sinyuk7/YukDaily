package com.sinyuk.yukdaily.ui.gank;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sinyuk.myutils.text.DateUtils;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.GankItemBinding;
import com.sinyuk.yukdaily.entity.Gank.GankData;
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
    private final SimpleDateFormat formatter;
    private final Context context;
    private List<GankData> dataset = new ArrayList<>();

    public GankAllAdapter(Context context) {
        this.context = context;
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

    }

    @BindingAdapter("gankData")
    public static void bindGankData(LinearLayout viewGroup, List<GankData> dataList) {
//
//        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
//        for (int i = 0; i < dataList.size(); i++) {
//            if (dataList.get(i) == null) { continue; }
//            final GankData data = dataList.get(i);
//            Log.d(TAG, "bind: " + dataList.get(0).getType() + " at " + i);
//
//            GankItemCellBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.gank_item_cell, viewGroup, false);
//            binding.setData(data);
//            Date date = new Date();
//            try {
//                date = formatter.parse(data.getPublishedAt());
//            } catch (ParseException e) {
//                e.printStackTrace();
//                date.setTime(0);
//            }
//
//            binding.authorAndDate.setText(
//                    new AndroidSpan()
//                            .drawTextAppearanceSpan(data.getAuthor() + "/", viewGroup.getContext(), R.style.gank_author)
//                            .drawTextAppearanceSpan(DateUtils.getTimeAgo(viewGroup.getContext(), date),
//                                    viewGroup.getContext(), R.style.gank_date)
//                            .getSpanText());
//
//            binding.title.setOnClickListener(v -> Log.d(TAG, "onClick: " + data.getUrl()));
//
//
//            LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            final Resources res = viewGroup.getContext().getResources();
//            if (res == null) { return; }
//            if (i == 0) {
//                lps.topMargin = res.getDimensionPixelOffset(R.dimen.content_space_8);
//            }
//            lps.bottomMargin = res.getDimensionPixelOffset(R.dimen.content_space_8);
////            lps.rightMargin = res.getDimensionPixelOffset(R.dimen.content_space_16);
////            lps.leftMargin = res.getDimensionPixelOffset(R.dimen.content_space_16);
//
//            viewGroup.addView(binding.getRoot(), lps);
//        }
    }

    @Override
    public GankItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final GankItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.gank_item, parent, false);
        return new GankItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(GankItemHolder holder, int position) {
        if (dataset.get(position) != null) {
            final GankData result = dataset.get(position);
            holder.getBinding().setData(result);
            Date date = new Date();
            try {
                date = formatter.parse(result.getPublishedAt());
            } catch (ParseException e) {
                e.printStackTrace();
                date.setTime(0);
            }

            holder.getBinding().authorAndDate.setText(
                    new AndroidSpan()
                            .drawTextAppearanceSpan(result.getAuthor() + "/", context, R.style.gank_author)
                            .drawTextAppearanceSpan(DateUtils.getTimeAgo(context, date),
                                    context, R.style.gank_date).getSpanText());
            holder.getBinding().executePendingBindings();

        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setData(List<GankData> data) {
        dataset.clear();
        dataset.addAll(data);
        notifyItemRangeInserted(0, data.size());
    }

    public void appendData(List<GankData> data) {
        final int start = dataset.size();

        dataset.addAll(data);
//        notifyItemInserted(start);
        notifyItemRangeInserted(start, data.size());
    }

    class GankItemHolder extends BindingViewHolder<GankItemBinding> {

        GankItemHolder(GankItemBinding binding) {
            super(binding);
        }


    }
}
