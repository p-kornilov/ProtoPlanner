package com.vividprojects.protoplanner.DB;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import javax.inject.Inject;

/**
 * Created by Smile on 11.01.2018.
 */

public class SDFileManager {

    private Context context;

    @Inject
    public SDFileManager(Context context) {
        this.context = context;
    }

    public void test(){
        Log.d("Test", "TestTest!!!");
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
