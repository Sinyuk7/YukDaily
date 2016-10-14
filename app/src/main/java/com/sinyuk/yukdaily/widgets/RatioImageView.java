package com.sinyuk.yukdaily.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sinyuk.yukdaily.R;

/**
 * Created by Sinyuk on 2016/10/13.
 */

public class RatioImageView extends ImageView {

    final static float INVALID_RATIO = -1;
    float horizontalRatio = INVALID_RATIO;
    float verticalRatio = INVALID_RATIO;

    public RatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.RatioImageView, defStyle, 0);
        try {
            if (a.hasValue(R.styleable.RatioImageView_imageWOverH))
                horizontalRatio = a.getFloat(R.styleable.RatioImageView_imageWOverH, INVALID_RATIO);

            if (a.hasValue(R.styleable.RatioImageView_imageHOverW))
                verticalRatio = a.getFloat(R.styleable.RatioImageView_imageHOverW, INVALID_RATIO);

        } finally {
            a.recycle();
        }
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (verticalRatio != INVALID_RATIO) {
            int height = MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(widthSpec) * verticalRatio),
                    MeasureSpec.EXACTLY);
            super.onMeasure(widthSpec, height);
        } else if (horizontalRatio != INVALID_RATIO) {
            int width = MeasureSpec.makeMeasureSpec((int) (MeasureSpec.getSize(heightSpec) * horizontalRatio),
                    MeasureSpec.EXACTLY);
            super.onMeasure(width, heightSpec);
        } else {
            super.onMeasure(widthSpec, heightSpec);
        }
    }
}
