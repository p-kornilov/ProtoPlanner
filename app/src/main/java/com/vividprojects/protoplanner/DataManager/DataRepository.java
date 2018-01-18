package com.vividprojects.protoplanner.DataManager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.Target;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.DB.LocalDataDB;
import com.vividprojects.protoplanner.DB.NetworkDataDB;
import com.vividprojects.protoplanner.AppExecutors;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.DB.NetworkResponse;
import com.vividprojects.protoplanner.Images.FullTarget;
import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Images.ThumbnailTarget;
import com.vividprojects.protoplanner.Network.NetworkLoader;
import com.vividprojects.protoplanner.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Smile on 05.12.2017.
 */

@Singleton
public class DataRepository {
    private Context context;
    private String imagesDirectory;

    private final NetworkDataDB networkDataDB;
    private final LocalDataDB localDataDB;
    private final AppExecutors appExecutors;
    private final NetworkLoader networkLoader;

    @Inject
    public DataRepository(Context context, AppExecutors appExecutors, LocalDataDB ldb, NetworkDataDB ndb, NetworkLoader networkLoader){
        this.localDataDB = ldb;
        this.networkDataDB = ndb;
        this.appExecutors = appExecutors;
        this.networkLoader = networkLoader;
        this.context = context;
        imagesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    public LiveData<Resource<List<Record>>> loadRecords(List<String> filter) {
        return new NetworkBoundResource<List<Record>, List<Record>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Record> item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<Record> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Record>> loadFromLocalDB() {
                MutableLiveData<List<Record>> ld = new MutableLiveData<>();
                if (filter != null && filter.size() > 0)
                    ld.setValue(localDataDB.queryRecords().labels_equalTo(filter).findAll());
                else
                    ld.setValue(localDataDB.queryRecords().findAll());
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<List<Record>>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<List<Record>>>();
            }
        }.asLiveData();
    }

    public LiveData<Resource<Record.Plain>> loadRecord(String id) {
        return new NetworkBoundResource<Record.Plain, Record.Plain>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Record.Plain item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Record.Plain data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Record.Plain> loadFromLocalDB() {
                MutableLiveData<Record.Plain> ld = new MutableLiveData<>();
                Record.Plain record = localDataDB
                        .queryRecords()
                        .id_equalTo(id)
                        .findFirst()
                        .getPlain();
                ld.setValue(record);
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<Record.Plain>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<Record.Plain>>();
            }
        }.asLiveData();
    }

    public LiveData<Resource<Variant.Plain>> loadVariant(String id) {
        return new NetworkBoundResource<Variant.Plain, Variant.Plain>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Variant.Plain item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Variant.Plain data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Variant.Plain> loadFromLocalDB() {
                MutableLiveData<Variant.Plain> ld = new MutableLiveData<>();
                Variant.Plain variant = localDataDB
                        .queryVariants()
                        .title_equalTo(id)
                        .findFirst()
                        .getPlain();
                for (int i = 0;i<variant.small_images.size();i++) variant.small_images.set(i, imagesDirectory + "/img_s_" + variant.small_images.get(i) + ".jpg");
                for (int i = 0;i<variant.full_images.size();i++) variant.full_images.set(i, imagesDirectory + "/img_f_" + variant.full_images.get(i) + ".jpg");
                ld.setValue(variant);
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<Variant.Plain>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<Variant.Plain>>();
            }
        }.asLiveData();
    }

/*    public LiveData<Resource<String>> loadVariantImages(String title) {
        return new NetworkBoundResource<String, String>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull String item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable String data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<String> loadFromLocalDB() {
                MutableLiveData<String> ld = new MutableLiveData<>();
                ld.setValue(localDataDB.queryRecords().id_equalTo(id).findFirst());
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<Record>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<Record>>();
            }
        }.asLiveData();
    }*/

    public int getHeight() {return context.getResources().getConfiguration().screenHeightDp;}

    public Context getContext() {return context;}

    public void setContext(Context context) {
        this.context = context;
    }

    public String getType() {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        if (isTablet) return "This is tablet";
        else return "This is phone";
    }

    public void initDB(){
        localDataDB.initDB();
    }

    public void showDB(){
        localDataDB.showDB();
    }

    public void initImages() {
        Log.d("Test", "External Storage - " + getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        //fileManager.test();
        BaseTarget target = new FullTarget("test.jpg",getContext());
        GlideApp.with(context)
                //  .load("http://anub.ru/uploads/07.2015/976_podborka_34.jpg")
                // .load(R.raw.testpicture)
                // .load("img_testpicture.jpg")
                .load(new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"img_testpicture.jpg"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                //.into(iv);
                .into(target);
    }

   // public void save

    public LiveData<Integer> saveImageFromURLtoVariant(String url, String variant) {
        //MutableLiveData<Integer> progress = new MutableLiveData<>();
        String file_name = imagesDirectory + "/img_f_" + UUID.nameUUIDFromBytes(url.getBytes()).toString() + ".jpg";
        LiveData<Integer> progress = networkLoader.loadImage(url,file_name,()->{
            Log.d("Test", "Done loading in Repository!!!");
        });
        return progress;
    }
/*
    public String saveImageFromURL(String URL, ProgressBar bar) {

        final ProgressBar progressBar = bar;

        String file_name = UUID.nameUUIDFromBytes(URL.getBytes()).toString() + ".jpg";
        Log.d("Test", "UUID - " + file_name);
        Target target = new ThumbnailTarget("img_s_"+file_name,context);

        Glide.with(context).
                .register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
        Glide.with(context)
                .load(URL)
                // Disabling cache to see download progress with every app load
                // You may want to enable caching again in production
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }*/

}

