package com.vividprojects.protoplanner.Images;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by Smile on 13.01.2018.
 */

public class FullTarget extends BaseTarget<BitmapDrawable> {
    private String file_name;
    private Runnable onError;
    private Runnable onSuccess;

    public FullTarget(String file_name, Runnable onError, Runnable onSuccess) {
        this.file_name = file_name;
        this.onError = onError;
        this.onSuccess = onSuccess;
    }


    @Override
    public void onResourceReady(BitmapDrawable bitmap, Transition<? super BitmapDrawable> transition) {
        File file = new File(file_name);
        try {
/*            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
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
        cb.onSizeReady(SIZE_ORIGINAL,SIZE_ORIGINAL);
    }

    @Override
    public void removeCallback(SizeReadyCallback cb) {}
}
