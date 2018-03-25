package com.vividprojects.protoplanner.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by p.kornilov on 14.02.2018.
 */

@Singleton
public class Settings {
    private Context context;

    @Inject
    public Settings(Context context) {
        this.context = context;
    }

    public static boolean getGeneralSelectedSort(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("general_selected_sort", true);
    }

    public static int getMeasureSystem(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt("generalMeasureSystem", 0);
    }

}
