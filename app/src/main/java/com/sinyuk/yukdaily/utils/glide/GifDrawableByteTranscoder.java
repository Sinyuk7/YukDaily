package com.sinyuk.yukdaily.utils.glide;

import android.util.Log;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class GifDrawableByteTranscoder implements ResourceTranscoder<byte[], GifDrawable> {
    @Override
    public Resource<GifDrawable> transcode(Resource<byte[]> toTranscode) {
        try {
            // because DrawableResource is an abstract class , so i used MyDrawableResource
            return new MyDrawableResource(new pl.droidsonroids.gif.GifDrawable(toTranscode.get()));
        } catch (IOException ex) {
            Log.e("GifDrawable", "Cannot decode bytes", ex);
            return null;
        }
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    static class MyDrawableResource implements Resource<GifDrawable> {
        private pl.droidsonroids.gif.GifDrawable drawable;

        public MyDrawableResource(pl.droidsonroids.gif.GifDrawable gifDrawable) {
            this.drawable = gifDrawable;
        }

        @Override
        public GifDrawable get() {
            return drawable;
        }

        @Override
        public int getSize() {
            return (int) drawable.getInputSourceByteCount();
        }

        @Override
        public void recycle() {
            drawable.stop();
            drawable.recycle();
        }
    }
}