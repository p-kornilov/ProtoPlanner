package com.vividprojects.protoplanner.db;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public InputStream getFileStream(String file_name) {
        if (isExternalStorageReadable()) {
            File path = context.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES);
            File file = new File(path, file_name);

            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (IOException e) {
                    Log.d("ExternalStorage", "Error reading " + file, e);
                }

            } else return null;
        }

        return null;
    }

    public boolean saveFileStream(InputStream inputStream, String file_name) {

        return false;
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
