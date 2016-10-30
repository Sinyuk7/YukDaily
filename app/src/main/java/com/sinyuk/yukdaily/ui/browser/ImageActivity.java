package com.sinyuk.yukdaily.ui.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sinyuk.myutils.system.ScreenUtils;
import com.sinyuk.yukdaily.R;
import com.sinyuk.yukdaily.base.BaseActivity;
import com.sinyuk.yukdaily.databinding.ActivityImageBinding;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Sinyuk on 16.10.21.
 */

public class ImageActivity extends BaseActivity {
    private static final String KEY_SRC = "SRC";
    private static final String KEY_URL = "URL";
    private static final String KEY_CURRENT_ITEM = "CURRENT_ITEM";
    private static final int SWIPE_MIN_DISTANCE = 60;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;

    private ActivityImageBinding binding;
    private ArrayList<String> srcList = new ArrayList<>();

    public static void start(Context context, int currentItem, ArrayList<String> src) {
        Intent starter = new Intent(context, ImageActivity.class);
        starter.putStringArrayListExtra(KEY_SRC, src);
        starter.putExtra(KEY_CURRENT_ITEM, currentItem);
        context.startActivity(starter);
    }

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, ImageActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
    }

    public static void start(Context context, int currentItem, ArrayList<String> src, ActivityOptionsCompat options) {
        Intent starter = new Intent(context, ImageActivity.class);
        starter.putStringArrayListExtra(KEY_SRC, src);
        starter.putExtra(KEY_CURRENT_ITEM, currentItem);
        context.startActivity(starter, options.toBundle());
    }

    public static void start(Context context, String url, ActivityOptionsCompat options) {
        Intent starter = new Intent(context, ImageActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter, options.toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideSystemyBar(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_image);

        int currentItem = 0;

        if (!TextUtils.isEmpty(getIntent().getStringExtra(KEY_URL))) {
            srcList.add(getIntent().getStringExtra(KEY_URL));
        } else {
            srcList = getIntent().getStringArrayListExtra(KEY_SRC);
            currentItem = getIntent().getIntExtra(KEY_CURRENT_ITEM, 0);
        }

        PhotoAdapter adapter = new PhotoAdapter();
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(currentItem, false);

    }

    private void handleError(String message) {

    }

    private class PhotoAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return srcList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setLayoutParams(layoutParams);
            photoView.setOnViewTapListener((view, x, y) -> {
                if (((PhotoView) view).getScale() == 1) {
                    ActivityCompat.finishAfterTransition((Activity) view.getContext());
                }
            });

            if (srcList.get(position) != null) {
                Glide.with(ImageActivity.this)
                        .load(srcList.get(position))
                        .dontAnimate()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                handleError(e.getLocalizedMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(photoView);
            } else {
                handleError("");
            }
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object.equals(view);
        }
    }
}
