package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.coredata.Measure;
import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.utils.ItemNew;
import com.vividprojects.protoplanner.utils.Settings;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class MeasureListViewModel extends ViewModel implements ItemNew {

    final MutableLiveData<String> filter;

    private final LiveData<List<Measure.Plain>> list;

    private DataRepository dataRepository;

    final private MutableLiveData<Integer> refreshId = new MutableLiveData<>();

    final private LiveData<Measure.Plain> refreshMeasure;

    final private MutableLiveData<Integer> onNewTrigger = new MutableLiveData<>();

    @Inject
    public MeasureListViewModel(DataRepository dataRepository) {
        //super();
        //list = new MutableLiveData<>();

        this.dataRepository = dataRepository;

        this.filter = new MutableLiveData<>();

        list = Transformations.switchMap(filter,input -> MeasureListViewModel.this.dataRepository.getMeasures(Settings.getMeasureSystem(dataRepository.getContext())));

        refreshMeasure = Transformations.switchMap(refreshId,input -> MeasureListViewModel.this.dataRepository.getMeasure(input));

    }

    public void setFilter(String ids) {
        if (Objects.equals(filter.getValue(), ids)) {
            return;
        }
        filter.setValue(ids);
    }

    public void refresh(int id) {
        refreshId.setValue(id);
    }

    public LiveData<List<Measure.Plain>> getList(){

        return list;
    }

    public LiveData<Measure.Plain> getRefreshMeasure() {
        return refreshMeasure;
    }

    public void deleteMeasure(int hash) {
        dataRepository.deleteMeasure(hash);
    }

    public void setDefaultMeasure(int hash) {
        dataRepository.setDefaultMeasure(hash);
    }

    public LiveData<Integer> getOnNewTrigger() {
        return onNewTrigger;
    }

    @Override
    public void itemNew() {
        onNewTrigger.setValue(1);
    }
}
