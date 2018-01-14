package com.vividprojects.protoplanner.DataManager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.Target;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.DB.LocalDataDB;
import com.vividprojects.protoplanner.DB.NetworkDataDB;
import com.vividprojects.protoplanner.AppExecutors;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.DB.NetworkResponse;
import com.vividprojects.protoplanner.Images.FullTarget;
import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Images.ThumbnailTarget;
import com.vividprojects.protoplanner.R;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Smile on 05.12.2017.
 */

@Singleton
public class DataRepository {
    private Context context;

    private final NetworkDataDB networkDataDB;
    private final LocalDataDB localDataDB;
    private final AppExecutors appExecutors;

    @Inject
    public DataRepository(Context context, AppExecutors appExecutors, LocalDataDB ldb, NetworkDataDB ndb){
        this.localDataDB = ldb;
        this.networkDataDB = ndb;
        this.appExecutors = appExecutors;

        this.context = context;
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

    public LiveData<Resource<Record>> loadRecord(String id) {
        return new NetworkBoundResource<Record, Record>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Record item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable Record data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Record> loadFromLocalDB() {
                MutableLiveData<Record> ld = new MutableLiveData<>();
                ld.setValue(localDataDB.queryRecords().id_equalTo(id).findFirst());
                return ld;
            }

            @NonNull
            @Override
            protected LiveData<NetworkResponse<Record>> loadFromNetworkDB() {
                return new MutableLiveData<NetworkResponse<Record>>();
            }
        }.asLiveData();
    }

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

    public String saveImageFromURL(String url) {
        String file_name = UUID.nameUUIDFromBytes(url.getBytes()).toString() + ".jpg";
        Log.d("Test", "UUID - " + file_name);
        Target target = new ThumbnailTarget("img_s_"+file_name,context);
        GlideApp.with(context)
                .load(url)
                .into(target);
        target = new FullTarget("img_f_"+file_name,context);
        GlideApp.with(context)
                .load(url)
                .into(target);

        return file_name;
    }
}

