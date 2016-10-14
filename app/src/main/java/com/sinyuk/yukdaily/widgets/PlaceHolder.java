package com.sinyuk.yukdaily.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.Space;
import android.util.AttributeSet;

import com.sinyuk.yukdaily.R;

/**
 * Created by Sinyuk on 2016/10/12.
 */
public class PlaceHolder extends Space {

    final static float INVALID_RATIO = -1;
    float horizontalRatio = INVALID_RATIO;
    float verticalRatio = INVALID_RATIO;

    public PlaceHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.PlaceHolder, defStyle, 0);
        try {
            if (a.hasValue(R.styleable.PlaceHolder_holderWOverH))
                horizontalRatio = a.getFloat(R.styleable.PlaceHolder_holderWOverH, INVALID_RATIO);

            if (a.hasValue(R.styleable.PlaceHolder_holderHOverW))
                verticalRatio = a.getFloat(R.styleable.PlaceHolder_holderHOverW, INVALID_RATIO);

        } finally {
            a.recycle();
        }
    }

    public PlaceHolder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlaceHolder(Context context) {
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
