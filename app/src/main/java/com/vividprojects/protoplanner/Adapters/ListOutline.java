package com.vividprojects.protoplanner.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
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
    private float elevation;

    public ListOutline(Context context) {
        elevation = context.getResources().getDimension(R.dimen.cardElevation);
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setRect(0, (int)view.getResources().getDimension(R.dimen.cardElevation), view.getWidth(),view.getHeight());
    }
}