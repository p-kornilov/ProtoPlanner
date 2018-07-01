package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.coredata.Filter;
import com.vividprojects.protoplanner.coredata.Record;


import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.datamanager.DataRepository;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class RecordListViewModel extends ViewModel {

    final MutableLiveData<Filter> filter;

    private final LiveData<Resource<List<Record.Plain>>> list;

    private DataRepository dataRepository;

    @Inject
    public RecordListViewModel(DataRepository dataRepository) {
        //super();
        //list = new MutableLiveData<>();

        this.dataRepository = dataRepository;

        this.filter = new MutableLiveData<>();
        list = Transformations.switchMap(filter,input -> {
/*            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return dataRepository.loadRecords(input.getFilter());
            }*/
            return RecordListViewModel.this.dataRepository.loadRecords(input.getFilter());
        });
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
}
