package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.vividprojects.protoplanner.CoreData.Record;


import com.vividprojects.protoplanner.DI.AppComponent;
import com.vividprojects.protoplanner.DataManager.DataManager;
import com.vividprojects.protoplanner.PPApplication;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Smile on 06.12.2017.
 */

public class RecordListViewModel extends ViewModel {
    private final MutableLiveData<List<Record>> list = new MutableLiveData<>();

    @Inject
    DataManager dataManager;

    @Inject
    public RecordListViewModel(Context context) {
        super();
    }

    public MutableLiveData<List<Record>> getList(){

        list.setValue(dataManager.queryRecords().findAll());
        return list;
    }

    public List<Record> getTest() {
        return dataManager.queryRecords().findAll();
    }
}
