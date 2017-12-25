package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.vividprojects.protoplanner.CoreData.Record;


import com.vividprojects.protoplanner.DataManager.DataRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class RecordListViewModel extends ViewModel {
    private final MutableLiveData<List<Record>> list = new MutableLiveData<>();

    @Inject
    DataRepository dataRepository;

    @Inject
    public RecordListViewModel(Context context) {
        super();
    }

    public MutableLiveData<List<Record>> getList(){

        list.setValue(dataRepository.queryRecords().findAll());
        return list;
    }

    public List<Record> getTest() {
        return dataRepository.queryRecords().findAll();
    }
}
