package com.vividprojects.protoplanner.Interface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
        currentActivity = null;
    }

    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public void openRecord(String id) {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {
            // replace fragment record (id)
        } else {
            // open new ViewRecord (id)
            Intent intent = new Intent(currentActivity, RecordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra("RECORD_ID",id);
            currentActivity.startActivity(intent);

        }
    }

    public String getType() {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        if (isTablet) return "This is tablet";
        else return "This is phone";
    }
}
