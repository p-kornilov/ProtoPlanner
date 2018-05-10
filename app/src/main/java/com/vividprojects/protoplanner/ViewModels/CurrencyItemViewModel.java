package com.vividprojects.protoplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.Uri;

import com.vividprojects.protoplanner.BindingModels.CurrencyItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.Bundle2;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class CurrencyItemViewModel extends ViewModel {
    private final MutableLiveData<Integer> currencyIsoCode = new MutableLiveData<>();
    private final MutableLiveData<Integer> onSaveId = new MutableLiveData<>();
    private final LiveData<Currency.Plain> currency;

    private DataRepository dataRepository;
    private CurrencyItemBindingModel bindingModel;

    @Inject
    public CurrencyItemViewModel(DataRepository dataRepository, Context context) {

        bindingModel = new CurrencyItemBindingModel(context, dataRepository.getImagesDirectory() + "/");

        this.dataRepository = dataRepository;

        currency = Transformations.switchMap(currencyIsoCode, input -> CurrencyItemViewModel.this.dataRepository.getCurrency(input));
    }

    public void setIsoCode(int iso_code) {
        if (Objects.equals(currencyIsoCode.getValue(), iso_code)) {
            return;
        }
        currencyIsoCode.setValue(iso_code);
    }

    public LiveData<Currency.Plain> getCurrency(){
        return currency;
    }

    public LiveData<Currency.Plain> getBase(){
        return dataRepository.getCurrencyBase();
    }

    public CurrencyItemBindingModel getBindingModel() {
        return bindingModel;
    }

    public LiveData<Integer> getOnSaveId() {
        return onSaveId;
    }

    public void save(){
        onSaveId.setValue(dataRepository.saveCurrency(bindingModel.getCurrency()));
        return;
    }

    public LiveData<String> loadGalleryImage(Uri fileName) {
        return dataRepository.saveImageFromGallery(fileName);
    }
}
