package com.sinyuk.yukdaily.ui.gank;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.sinyuk.myutils.text.DateUtils;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.customtab.CustomTabActivityHelper;
import com.sinyuk.yukdaily.customtab.WebviewActivityFallback;
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

public class GankAdapter extends RecyclerView.Adapter<GankAdapter.GankItemHolder> {
    public static final String TAG = "GankAdapter";
    private final SimpleDateFormat formatter;
    private final Context context;
    private final CustomTabsIntent.Builder customTabsBuilder;
    private final DrawableRequestBuilder<String> requestManager;
    private List<GankData> dataset = new ArrayList<>();

    public GankAdapter(Context context) {
        this.context = context;
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);

        CustomTabActivityHelper customTabActivityHelper = new CustomTabActivityHelper();

        customTabsBuilder = new CustomTabsIntent.Builder(customTabActivityHelper.getSession());
        customTabsBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        customTabsBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorAccent));
        customTabsBuilder.addDefaultShareMenuItem();

        requestManager = Glide.with(context).fromString().crossFade(300)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.sample)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .centerCrop()
                .error(R.drawable.sample);
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

            if (context.getString(R.string.item_fuli).equals(result.getType())) {
                holder.getBinding().imageView.setVisibility(View.VISIBLE);
                requestManager.load(result.getUrl()).into(holder.getBinding().imageView);
                holder.getBinding().badge.setImageResource(R.drawable.ic_pic);

            } else {
                holder.getBinding().imageView.setVisibility(View.GONE);
                requestManager.load("").into(holder.getBinding().imageView);
                holder.getBinding().badge.setImageResource(R.drawable.ic_link);
            }

            holder.getBinding().title.setActivated(result.isClicked());

            holder.getBinding().authorAndDate.setText(
                    new AndroidSpan()
                            .drawTextAppearanceSpan(result.getAuthor() + "/", context, R.style.gank_author)
                            .drawTextAppearanceSpan(DateUtils.getTimeAgo(context, date),
                                    context, R.style.gank_date).getSpanText());

            holder.itemView.setOnClickListener(v -> {
                CustomTabsIntent customTabsIntent = customTabsBuilder.build();
                CustomTabActivityHelper.openCustomTab((Activity) context, customTabsIntent, Uri.parse(result.getUrl()), new WebviewActivityFallback());
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
