package com.vividprojects.protoplanner.Images;

import android.content.Context;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

/**
 * Created by Smile on 10.01.2018.
 */

public class LocalSDModelLoaderFactory implements ModelLoaderFactory<String, InputStream> {
    private Context context;

    LocalSDModelLoaderFactory(Context context) {
        this.context = context;
    }

    @Override
    public ModelLoader<String, InputStream> build(MultiModelLoaderFactory unused) {
        return new LocalSDLoader(context);
    }

    @Override
    public void teardown() {
        // Do nothing.
    }
}
