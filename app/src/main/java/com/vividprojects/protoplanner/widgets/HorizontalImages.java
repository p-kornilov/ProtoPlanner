package com.vividprojects.protoplanner.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vividprojects.protoplanner.R;

/**
 * Created by Smile on 11.11.2017.
 */

public class HorizontalImages extends LinearLayout {
    private ImageView noneImage;

    public HorizontalImages(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        if (noneImage != null && noneImage.getVisibility()!=GONE) {
            noneImage.setVisibility(GONE);
            boolean empty = true;
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child.getVisibility() != GONE) {
                    empty = false;
                }
            }
            if (empty) noneImage.setVisibility(VISIBLE);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public HorizontalImages(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HorizontalImages(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setNoneImage(boolean set) {
        if (set && noneImage==null) {
            noneImage = new ImageView(getContext());
            noneImage.setImageResource(R.drawable.ic_image_black_24dp);
            int width_in_dp = 100;
            final float scale = getResources().getDisplayMetrics().density;
            int width_in_px = (int) (width_in_dp * scale + 0.5f);
            int paddind = (int) (4*scale+0.5f);
            noneImage.setLayoutParams(new LinearLayout.LayoutParams(width_in_px,LinearLayout.LayoutParams.MATCH_PARENT));
            noneImage.setPadding(paddind,0,paddind,0);
            this.addView(noneImage);
//            noneImage.setVisibility(GONE);

        } else if (set && noneImage!=null) {
            noneImage.setVisibility(VISIBLE);
        } else if (!set && noneImage!=null) {
            noneImage.setVisibility(GONE);
        }
    }
}
