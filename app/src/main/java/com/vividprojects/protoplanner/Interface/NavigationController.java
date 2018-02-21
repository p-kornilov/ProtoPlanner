package com.vividprojects.protoplanner.Interface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;

import com.vividprojects.protoplanner.Interface.Activity.ContainerActivity;
import com.vividprojects.protoplanner.Interface.Activity.RecordActivity;
import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.R;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Smile on 20.12.2017.
 */
@Singleton
public class NavigationController {
    private Context context;
    private Activity currentActivity;

    @Inject
    public NavigationController(Context context) {
        this.context = context;
    }

    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public void openRecord(String id) {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);

/*        SDK_INT value        Build.VERSION_CODES        Human Version Name
        1                  BASE                      Android 1.0 (no codename)
        2                  BASE_1_1                  Android 1.1 Petit Four
        3                  CUPCAKE                   Android 1.5 Cupcake
        4                  DONUT                     Android 1.6 Donut
        5                  ECLAIR                    Android 2.0 Eclair
        6                  ECLAIR_0_1                Android 2.0.1 Eclair
        7                  ECLAIR_MR1                Android 2.1 Eclair
        8                  FROYO                     Android 2.2 Froyo
        9                  GINGERBREAD               Android 2.3 Gingerbread
        10                  GINGERBREAD_MR1           Android 2.3.3 Gingerbread
        11                  HONEYCOMB                 Android 3.0 Honeycomb
        12                  HONEYCOMB_MR1             Android 3.1 Honeycomb
        13                  HONEYCOMB_MR2             Android 3.2 Honeycomb
        14                  ICE_CREAM_SANDWICH        Android 4.0 Ice Cream Sandwich
        15                  ICE_CREAM_SANDWICH_MR1    Android 4.0.3 Ice Cream Sandwich
        16                  JELLY_BEAN                Android 4.1 Jellybean
        17                  JELLY_BEAN_MR1            Android 4.2 Jellybean
        18                  JELLY_BEAN_MR2            Android 4.3 Jellybean
        19                  KITKAT                    Android 4.4 KitKat
        20                  KITKAT_WATCH              Android 4.4 KitKat Watch
        21                  LOLLIPOP                  Android 5.0 Lollipop
        22                  LOLLIPOP_MR1              Android 5.1 Lollipop
        23                  M                         Android 6.0 Marshmallow
        24                  N                         Android 7.0 Nougat
        25                  N_MR1                     Android 7.1.1 Nougat
        26                  O                         Android 8.0 Oreo
        27                  O_MR1                     Android 8 Oreo MR1
        10000                CUR_DEVELOPMENT           Current Development Version*/


        if (isTablet) {
            // replace fragment record (id)
            ((MainActivity) currentActivity).setRecordItem(id);
        } else {
            // open new ViewRecord (id)
            Intent intent;
            if (Build.VERSION.SDK_INT <= 23) {
                intent = new Intent(context, RecordActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                intent = new Intent(context, RecordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            intent.putExtra("RECORD_ID",id);
            context.startActivity(intent);
        }
    }

    public void openImageView(int position, String variant) {

        Intent intent;
        if (Build.VERSION.SDK_INT <= 23) {
            intent = new Intent(context, ImageViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(context, ImageViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        intent.putExtra("POSITION",position);
        intent.putExtra("VARIANT_ID",variant);
        context.startActivity(intent);
    }

    public void openLabels() {

        Intent intent;
        if (Build.VERSION.SDK_INT <= 23) {
            intent = new Intent(context, LabelsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(context, LabelsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    public void openCurrensies() {

        Intent intent;
        if (Build.VERSION.SDK_INT <= 23) {
            intent = new Intent(context, ContainerActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(context, ContainerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    public void openSettings() {

        Intent intent;
        if (Build.VERSION.SDK_INT <= 23) {
            intent = new Intent(context, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(context, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    public static void openLabelsForResult(String[] id, Fragment context, int requestCode) {
        Intent intent;
        if (Build.VERSION.SDK_INT <= 23) {
            intent = new Intent(context.getActivity(), LabelsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(context.getActivity(), LabelsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        intent.putExtra("SELECTED",id);
        context.startActivityForResult(intent,requestCode);
    }

    public String getType() {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        if (isTablet) return "This is tablet";
        else return "This is phone";
    }

    public boolean isTablet() {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }
}
