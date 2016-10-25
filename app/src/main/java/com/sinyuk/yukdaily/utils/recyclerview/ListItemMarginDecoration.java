package com.sinyuk.yukdaily.utils.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by Sinyuk on 16.1.21.
 */
public class ListItemMarginDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int mSpace;
    private boolean includeEdge;

    public ListItemMarginDecoration(int spanCount, int resId, boolean includeEdge, Context context) {
        this.mSpace = context.getResources().getDimensionPixelOffset(resId);
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
    }

    public ListItemMarginDecoration(int spanCount, int mSpace, boolean includeEdge) {
        this.spanCount = spanCount;
        this.mSpace = mSpace;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column 列数

        if (includeEdge) { // 如果边缘也要有边距
            outRect.left = mSpace - column * mSpace / spanCount; // mSpace - column * ((1f / spanCount) * mSpace)
            outRect.right = (column + 1) * mSpace / spanCount; // (column + 1) * ((1f / spanCount) * mSpace)

            if (position < spanCount) { // top edge
                outRect.top = mSpace;
            }
            outRect.bottom = mSpace; // item bottom
        } else {
            outRect.left = column * mSpace / spanCount; // column * ((1f / spanCount) * mSpace)
            outRect.right = mSpace - (column + 1) * mSpace / spanCount; // mSpace - (column + 1) * ((1f /    spanCount) * mSpace)
            if (position >= spanCount) {
                outRect.top = mSpace; // item top
            }
        }
    }
}
