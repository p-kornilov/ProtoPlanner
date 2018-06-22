package com.vividprojects.protoplanner.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Smile on 27.01.2018.
 */

public class TouchableViewPager extends ViewPager {
    public TouchableViewPager(Context context) {
        super(context);
    }

    public TouchableViewPager(Context context, AttributeSet attrs)
    {
        super(context,attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
