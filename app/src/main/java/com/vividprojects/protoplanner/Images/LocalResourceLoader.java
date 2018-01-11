package com.vividprojects.protoplanner.Images;

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

public final class LocalResourceLoader implements ModelLoader<Integer, InputStream> {

    private final Context context;

    LocalResourceLoader(Context context) {
        this.context = context;
    }

    public LoadData<InputStream> buildLoadData(int model, int width, int height, Options options) {
        return buildLoadData(Integer.valueOf(model), width, height, options);
    }


    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(Integer model, int width, int height, Options options) {
        Key diskCacheKey = new ObjectKey(model);
        return new LoadData<>(diskCacheKey, /*fetcher=*/ new LocalResourceFetcher(context,model.intValue()));
    }

    @Override
    public boolean handles(Integer model) {
        InputStream is = context.getResources().openRawResource(model.intValue());
        return is != null;
    }
}
