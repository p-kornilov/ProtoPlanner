package com.vividprojects.protoplanner.Images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Smile on 13.01.2018.
 */

public class ThumbnailTarget extends BaseTarget<BitmapDrawable> {
    private String file_name;
    private Context context;

    public ThumbnailTarget(String file_name, Context context) {
        this.file_name = file_name;
        this.context = context; // TODO !!! Удалить
    }


    @Override
    public void onResourceReady(BitmapDrawable bitmap, Transition<? super BitmapDrawable> transition) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),file_name);
        try {
            OutputStream stream = new FileOutputStream(file);
            bitmap.getBitmap().compress(Bitmap.CompressFormat.JPEG,80,stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSize(SizeReadyCallback cb) {
        //cb.onSizeReady(Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels);
        cb.onSizeReady(256,256);
    }

    @Override
    public void removeCallback(SizeReadyCallback cb) {}
}
