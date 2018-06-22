package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.Uri;

import com.vividprojects.protoplanner.bindingmodels.CurrencyItemBindingModel;
import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.datamanager.DataRepository;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class CurrencyEditViewModel extends ViewModel {
    private final MutableLiveData<Integer> currencyIsoCode = new MutableLiveData<>();
    private final MutableLiveData<String> onSave = new MutableLiveData<>();
    private final LiveData<Currency.Plain> currency;

    private DataRepository dataRepository;
    private CurrencyItemBindingModel bindingModel;

    @Inject
    public CurrencyEditViewModel(DataRepository dataRepository, Context context) {

        bindingModel = new CurrencyItemBindingModel(context, dataRepository.getImagesDirectory() + "/");

        this.dataRepository = dataRepository;

        currency = Transformations.switchMap(currencyIsoCode, input -> CurrencyEditViewModel.this.dataRepository.getCurrency(input));
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

    public LiveData<String> onSave() {
        return onSave;
    }

    public void save(){
        setIsoCode(dataRepository.saveCurrency(bindingModel.getCurrency()));
        onSave.setValue("OK");
    }

    public LiveData<String> loadGalleryImage(Uri fileName) {
        return dataRepository.saveImageFromGallery(fileName);
    }

    public int getId() {
        return currencyIsoCode.getValue();
    }
}
