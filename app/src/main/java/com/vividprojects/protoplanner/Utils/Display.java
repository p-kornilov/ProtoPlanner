package com.vividprojects.protoplanner.Utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by p.kornilov on 02.02.2018.
 */

public class Display {
    public static int calc_pixels(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
