package com.vividprojects.protoplanner.Widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomDividerDecoration extends DividerItemDecoration {

    private Drawable divider;
    private int startOffset;
    private int endOffset;

    public CustomDividerDecoration(Context context, int o) {
        super(context, o);
    };

    public void setOffset(int start, int end) {
        this.startOffset = start;
        this.endOffset = end;
    }

    @Override
    public void setDrawable(@NonNull Drawable drawable) {
        this.divider = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft() + startOffset;
        int dividerRight = parent.getWidth() - parent.getPaddingRight() - endOffset;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + divider.getIntrinsicHeight();

            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
