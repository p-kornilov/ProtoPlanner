package com.vividprojects.protoplanner.images;

import android.content.Context;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

/**
 * Created by Smile on 10.01.2018.
 */

public class LocalResourceModelLoaderFactory implements ModelLoaderFactory<Integer, InputStream> {
    private Context context;

    LocalResourceModelLoaderFactory(Context context) {
        this.context = context;
    }

    @Override
    public ModelLoader<Integer, InputStream> build(MultiModelLoaderFactory unused) {
        return new LocalResourceLoader(context);
    }

    @Override
    public void teardown() {
        // Do nothing.
    }
}
