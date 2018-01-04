package com.vividprojects.protoplanner.DataManager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vividprojects.protoplanner.CoreData.Filter;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.DB.LocalDB;
import com.vividprojects.protoplanner.DB.NetworkDB;
import com.vividprojects.protoplanner.AppExecutors;
import com.vividprojects.protoplanner.CoreData.Block;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DB.NetworkResponse;
import com.vividprojects.protoplanner.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Smile on 05.12.2017.
 */

@Singleton
public class DataRepository {
    private Context context;

    private final NetworkDB networkDB;
    private final LocalDB localDB;
    private final AppExecutors appExecutors;

    @Inject
    public DataRepository(Context context, AppExecutors appExecutors, LocalDB ldb, NetworkDB ndb){
        this.localDB = ldb;
        this.networkDB = ndb;
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
                    ld.setValue(localDB.queryRecords().labels_equalTo(filter).findAll());
                else
                    ld.setValue(localDB.queryRecords().findAll());
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
                ld.setValue(localDB.queryRecords().id_equalTo(id).findFirst());
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
        localDB.initDB();
    }

    public void showDB(){
        localDB.showDB();
    }

}

