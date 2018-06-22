package com.vividprojects.protoplanner.images;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import java.io.InputStream;

/**
 * Created by Smile on 10.01.2018.
 */

public final class LocalSDLoader implements ModelLoader<String, InputStream> {

    private final Context context;

    LocalSDLoader(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(String model, int width, int height, Options options) {
        Key diskCacheKey = new ObjectKey(model);
        return new LoadData<>(diskCacheKey, /*fetcher=*/ new LocalSDFetcher(context,model));
    }

    @Override
    public boolean handles(String model) {
        return model.startsWith("img_");
    }
}
