package com.vividprojects.protoplanner.db;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Smile on 11.01.2018.
 */

@Singleton
public class LocalImageDB {
    private Context context;

    @Inject
    LocalImageDB(Context context) {
        this.context = context;
    }

    public BitmapDrawable getImage(String id) {
/*        if (isExternalStorageReadable()) {

        }*/
        return null;
    }

    public List<BitmapDrawable> getVariantImages(String title) {
        return null;
    }

}
