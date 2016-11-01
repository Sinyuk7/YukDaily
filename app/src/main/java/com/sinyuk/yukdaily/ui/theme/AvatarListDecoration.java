package com.sinyuk.yukdaily.ui.theme;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by Sinyuk on 16.1.21.
 */
public class AvatarListDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private boolean includeEdge;

    public AvatarListDecoration(int resId, boolean includeEdge, Context context) {
        this.mSpace = context.getResources().getDimensionPixelOffset(resId);
        this.includeEdge = includeEdge;
    }

    public AvatarListDecoration(int mSpace, boolean includeEdge) {
        this.mSpace = mSpace;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position

        if (includeEdge) { // 如果边缘也要有边距
            outRect.top = mSpace;
            outRect.bottom = mSpace;
        }

        if (position == 0) { // top edge
            outRect.left = mSpace;
        }
        outRect.right = mSpace; // item bottom
    }
}
