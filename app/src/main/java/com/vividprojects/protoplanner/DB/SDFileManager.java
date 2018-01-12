package com.vividprojects.protoplanner.DB;

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
                    InputStream inputStream = new FileInputStream(file);
                    byte[] data = new byte[inputStream.available()];
                    inputStream.read(data);
                    return inputStream;

                } catch (IOException e) {
                    Log.d("ExternalStorage", "Error writing " + file, e);
                }

            } else return null;
        }

        return null;
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
