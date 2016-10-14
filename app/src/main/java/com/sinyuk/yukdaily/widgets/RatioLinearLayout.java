package com.sinyuk.yukdaily.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.sinyuk.yukdaily.R;

/**
 * Created by Sinyuk on 2016/10/14.
 */

public class RatioLinearLayout extends LinearLayout {

    final static float INVALID_RATIO = -1;
    float horizontalRatio = INVALID_RATIO;
    float verticalRatio = INVALID_RATIO;

    public RatioLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.RatioLinearLayout, defStyle, 0);
        try {
            if (a.hasValue(R.styleable.RatioLinearLayout_layoutWOverH))
                horizontalRatio = a.getFloat(R.styleable.RatioLinearLayout_layoutWOverH, INVALID_RATIO);

            if (a.hasValue(R.styleable.RatioLinearLayout_layoutHOverW))
                verticalRatio = a.getFloat(R.styleable.RatioLinearLayout_layoutHOverW, INVALID_RATIO);

        } finally {
            a.recycle();
        }
    }

    public RatioLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioLinearLayout(Context context) {
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
