package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.vividprojects.protoplanner.bindingmodels.RecordListBindingModel;
import com.vividprojects.protoplanner.coredata.Filter;
import com.vividprojects.protoplanner.coredata.Record;


import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.datamanager.DataSubscriber;
import com.vividprojects.protoplanner.utils.FabActions;
import com.vividprojects.protoplanner.utils.RunnableParam;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class RecordListViewModel extends ViewModel implements FabActions {

    private Runnable addAction;

    final MutableLiveData<Filter> filter;

    private final LiveData<Resource<List<Record.Plain>>> list;
    private final LiveData<Resource<Record.Plain>> refreshedRecord;
    private final MutableLiveData<String> refreshedRecordId = new MutableLiveData<>();

    private DataRepository dataRepository;
    private DataSubscriber dataSubscriber;

    private RecordListBindingModel recordListBindingModel;
    private MediatorLiveData<Record.Plain> changedRecord = new MediatorLiveData<>();

    private LiveData<Record.Plain> currentSubscribedRecord;

    private MutableLiveData<String> newRecordName = new MutableLiveData<>();
    private final LiveData<Record.Plain> newRecord;

    @Inject
    public RecordListViewModel(DataRepository dataRepository, DataSubscriber dataSubscriber) {
        //super();
        //list = new MutableLiveData<>();

        recordListBindingModel = new RecordListBindingModel();
        recordListBindingModel.setDefaultImage(dataRepository.getDefaultVariantImage());

        this.dataRepository = dataRepository;
        this.dataSubscriber = dataSubscriber;

        this.filter = new MutableLiveData<>();
        list = Transformations.switchMap(filter,input -> {
/*            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return dataRepository.loadRecords(input.getFilter());
            }*/
            return RecordListViewModel.this.dataRepository.loadRecords(input.getFilter());
        });

        refreshedRecord = Transformations.switchMap(refreshedRecordId, id -> RecordListViewModel.this.dataRepository.loadRecord(id));
        newRecord = Transformations.switchMap(newRecordName, name -> RecordListViewModel.this.dataRepository.newRecord(name));

    }

    public void setFilter(List<String> ids) {
        Filter update = new Filter(ids);
        if (Objects.equals(filter.getValue(), update)) {
            return;
        }
        filter.setValue(update);
    }

    public LiveData<Resource<List<Record.Plain>>> getList(){

        return list;//dataRepository.loadRecords();
    }

    public RecordListBindingModel getRecordListBindingModel() {
        return recordListBindingModel;
    }

    public LiveData<Resource<Record.Plain>> getRefreshedRecord() {
        return refreshedRecord;
    }

    public void refreshRecord(String id) {
        //dataRepository.attachVariantToRecord(id, recordItemId.getValue());
        refreshedRecordId.setValue(id);
    }

    public void subscribeToRecord(String id) {
        if (currentSubscribedRecord != null) {
            if (currentSubscribedRecord.getValue() != null)
                dataSubscriber.unsubscribePlain(currentSubscribedRecord.getValue().id);
            changedRecord.removeSource(currentSubscribedRecord);
        }
        currentSubscribedRecord = dataSubscriber.subscribePlain(Record.Plain.class,id);
        changedRecord.addSource(currentSubscribedRecord, rp->changedRecord.setValue(rp));
    }

    public LiveData<Record.Plain> getChangedRecord() {
        return changedRecord;
    }

    public void setActionAdd(Runnable addAction) {
        this.addAction = addAction;
    }

    @Override
    public void actionAdd() {
        if (addAction != null)
            addAction.run();
    }

    public void createNewRecord(String recordName) {
        newRecordName.setValue(recordName);
    }

    public LiveData<Record.Plain> getNewRecord() {
        return newRecord;
    }

    public void deleteRecord(String id) {
        dataRepository.deleteRecord(id);
    }
}
