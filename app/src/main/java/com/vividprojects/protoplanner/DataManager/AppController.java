package com.vividprojects.protoplanner.DataManager;

import android.content.Context;
import android.content.Intent;

import com.vividprojects.protoplanner.Interface.RecordActivity;

/**
 * Created by Smile on 06.12.2017.
 */

public class AppController {
    private Context context;

    public AppController(Context context) {
        this.context = context;
    }

    public void openRecord(String id) {
        Intent intent = new Intent(context, RecordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("RECORD_ID",id);
        context.startActivity(intent);
    }
}
