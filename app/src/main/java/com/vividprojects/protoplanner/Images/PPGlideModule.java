package com.vividprojects.protoplanner.Images;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * Created by Smile on 10.01.2018.
 */

@GlideModule
public class PPGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

        //registry.prepend(Integer.class, InputStream.class, new LocalResourceModelLoaderFactory(context));
      //  registry.prepend(String.class, InputStream.class, new LocalSDModelLoaderFactory(context));
    }
}
