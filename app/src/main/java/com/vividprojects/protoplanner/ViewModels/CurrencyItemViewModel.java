package com.vividprojects.protoplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

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

    private final MutableLiveData<String> filter;
    private final MutableLiveData<Integer> currencyIsoCode = new MutableLiveData<>();

    private final LiveData<Currency.Plain> currency;
    private final LiveData<Currency.Plain> base;

    private DataRepository dataRepository;

    private CurrencyItemBindingModel bindingModel;

    @Inject
    public CurrencyItemViewModel(DataRepository dataRepository, Context context) {

        bindingModel = new CurrencyItemBindingModel(context);

        this.dataRepository = dataRepository;

        this.filter = new MutableLiveData<>();

        currency = Transformations.switchMap(currencyIsoCode, input -> CurrencyItemViewModel.this.dataRepository.getCurrency(input));
        base = Transformations.switchMap(currencyIsoCode, input -> CurrencyItemViewModel.this.dataRepository.getBaseForCurrency(input));

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
        return base;
    }

    public void deleteCurrency(int iso_code_int) {
        dataRepository.deleteCurrency(iso_code_int);
    }

    public void setDefaultCurrency(int iso_code_int) {
        dataRepository.setDefaultCurrency(iso_code_int);
    }

    public CurrencyItemBindingModel getBindingModel() {
        return bindingModel;
    }
}
