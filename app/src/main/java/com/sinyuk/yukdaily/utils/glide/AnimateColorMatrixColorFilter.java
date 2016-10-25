package com.sinyuk.yukdaily.utils.glide;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

/// Thanks to @DavidCrawford \
/// see http://stackoverflow.com/a/27301389/2573335
public class AnimateColorMatrixColorFilter {
    private ColorMatrixColorFilter mFilter;
    private ColorMatrix mMatrix;

    public AnimateColorMatrixColorFilter(ColorMatrix matrix) {
        setColorMatrix(matrix);
    }

    public ColorMatrixColorFilter getColorFilter() {
        return mFilter;
    }

    public ColorMatrix getColorMatrix() {
        return mMatrix;
    }

    public void setColorMatrix(ColorMatrix matrix) {
        mMatrix = matrix;
        mFilter = new ColorMatrixColorFilter(matrix);
    }
}