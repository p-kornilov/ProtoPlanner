package com.vividprojects.protoplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure_;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.Bundle2;
import com.vividprojects.protoplanner.Utils.Settings;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class MeasureListViewModel extends ViewModel {

    final MutableLiveData<String> filter;

    private final LiveData<List<Measure_.Plain>> list;

    private DataRepository dataRepository;

    final private MutableLiveData<Integer> refreshId = new MutableLiveData<>();

    final private LiveData<Measure_.Plain> refreshMeasure;

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

    public LiveData<List<Measure_.Plain>> getList(){

        return list;
    }

    public LiveData<Measure_.Plain> getRefreshMeasure() {
        return refreshMeasure;
    }

    public void deleteMeasure(int hash) {
        dataRepository.deleteMeasure(hash);
    }

    public void setDefaultMeasure(int hash) {
        dataRepository.setDefaultMeasure(hash);
    }
}
