package com.vividprojects.protoplanner.images;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

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
    private Runnable onError;
    private Runnable onSuccess;

    public ThumbnailTarget(String file_name, Runnable onError, Runnable onSuccess) {
        this.file_name = file_name;
        this.onError = onError;
        this.onSuccess = onSuccess;
    }


    @Override
    public void onResourceReady(BitmapDrawable bitmap, Transition<? super BitmapDrawable> transition) {
        File file = new File(file_name);
        try {
            OutputStream stream = new FileOutputStream(file);
            bitmap.getBitmap().compress(Bitmap.CompressFormat.JPEG,80,stream);
            stream.flush();
            stream.close();
            onSuccess.run();
        } catch (IOException e) {
            e.printStackTrace();
            onError.run();
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
