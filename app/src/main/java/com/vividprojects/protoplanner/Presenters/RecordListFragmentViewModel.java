package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.vividprojects.protoplanner.CoreData.Record;


import com.vividprojects.protoplanner.DI.DaggerMainComponent;
import com.vividprojects.protoplanner.DI.MainComponent;
import com.vividprojects.protoplanner.DI.DataManagerModule;
import com.vividprojects.protoplanner.DataManager.DataManager;
import com.vividprojects.protoplanner.PPApplication;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Smile on 06.12.2017.
 */

public class RecordListFragmentViewModel extends ViewModel {
    private final MutableLiveData<RealmResults<Record>> list = new MutableLiveData<RealmResults<Record>>();

    @Inject
    DataManager dataManager;

    public RecordListFragmentViewModel(Context context) {
        super();
        MainComponent dmc = ((PPApplication) context).getMainComponent();
        dmc.inject(this);
        Realm realm = Realm.getDefaultInstance();
        list.setValue(realm.where(Record.class).findAll());
    }

    public MutableLiveData<RealmResults<Record>> getList(){

        return list;
    }

    public List<Record> getTest() {
        return dataManager.queryRecords().findAll();
    }
}
