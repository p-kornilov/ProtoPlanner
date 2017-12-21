package com.vividprojects.protoplanner.Interface;

import android.content.Context;

import com.vividprojects.protoplanner.R;

import javax.inject.Inject;

/**
 * Created by Smile on 20.12.2017.
 */

public class NavigationController {
    private Context context;

    @Inject
    public NavigationController(Context context) {
        this.context = context;
    }

    public void openRecord(String id) {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {
            // replace fragment record (id)
        } else {
            // open new ViewRecord (id)
        }
    }
}
