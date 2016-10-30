package com.sinyuk.yukdaily.ui.gank;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sinyuk.myutils.text.DateUtils;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.databinding.GankItemBinding;
import com.sinyuk.yukdaily.entity.Gank.GankData;
import com.sinyuk.yukdaily.ui.browser.WebViewActivity;
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

public class GankAdapter extends RecyclerView.Adapter<GankAdapter.GankItemHolder> {
    public static final String TAG = "GankAdapter";
    private final SimpleDateFormat formatter;
    private final Context context;
    private List<GankData> dataset = new ArrayList<>();

    public GankAdapter(Context context) {
        this.context = context;
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
    }

//    @Override
//    public long getItemId(int position) {
//        return super.getItemId(position);
//        return Long.getLong(dataset.get(position).getId(),)
//    }

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


            holder.getBinding().title.setActivated(result.isClicked());

            holder.getBinding().authorAndDate.setText(
                    new AndroidSpan()
                            .drawTextAppearanceSpan(result.getAuthor() + "/", context, R.style.gank_author)
                            .drawTextAppearanceSpan(DateUtils.getTimeAgo(context, date),
                                    context, R.style.gank_date).getSpanText());

            holder.itemView.setOnClickListener(v -> {
                WebViewActivity.open(context, result.getUrl());
                result.setClicked(true);
                holder.getBinding().title.setActivated(true);
            });

            holder.getBinding().executePendingBindings();

        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setData(List<GankData> data) {
        if (dataset.isEmpty()) {
            dataset.addAll(data);
            notifyItemRangeInserted(0, data.size());
        } else {
            dataset.clear();
            dataset.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void appendData(List<GankData> data) {
        final int start = dataset.size();
        dataset.addAll(data);
        notifyItemRangeInserted(start, data.size());
//        notifyDataSetChanged();
    }


    class GankItemHolder extends BindingViewHolder<GankItemBinding> {

        GankItemHolder(GankItemBinding binding) {
            super(binding);
        }

    }
}
