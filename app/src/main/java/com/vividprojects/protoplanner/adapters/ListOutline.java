package com.vividprojects.protoplanner.adapters;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.vividprojects.protoplanner.R;

/**
 * Created by Smile on 26.03.2018.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ListOutline extends ViewOutlineProvider {

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setRect(0, (int)view.getResources().getDimension(R.dimen.cardElevation), view.getWidth(),view.getHeight());
    }
}