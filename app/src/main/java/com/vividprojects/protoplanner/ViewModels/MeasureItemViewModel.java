package com.vividprojects.protoplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.vividprojects.protoplanner.BindingModels.CurrencyItemBindingModel;
import com.vividprojects.protoplanner.BindingModels.MeasureItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure_;
import com.vividprojects.protoplanner.DataManager.DataRepository;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class MeasureItemViewModel extends ViewModel {
    private final MutableLiveData<Integer> hash = new MutableLiveData<>();
    private final MutableLiveData<Integer> onSaveHash = new MutableLiveData<>();
    private final LiveData<Measure_.Plain> measure;

    private DataRepository dataRepository;
    private MeasureItemBindingModel bindingModel;

    @Inject
    public MeasureItemViewModel(DataRepository dataRepository, Context context) {

        bindingModel = new MeasureItemBindingModel(context);
        this.dataRepository = dataRepository;

        measure = Transformations.switchMap(hash, input -> MeasureItemViewModel.this.dataRepository.getMeasure(input));
    }

    public void setHash(int newHash) {
        if (Objects.equals(hash.getValue(), newHash)) {
            return;
        }
        hash.setValue(newHash);
    }

    public LiveData<Measure_.Plain> getMeasure(){
        return measure;
    }

    public MeasureItemBindingModel getBindingModel() {
        return bindingModel;
    }

    public LiveData<Integer> getOnSaveHash() {
        return onSaveHash;
    }

    public void save(){
        onSaveHash.setValue(dataRepository.saveMeasure(bindingModel.getMeasure()));
        return;
    }
}
