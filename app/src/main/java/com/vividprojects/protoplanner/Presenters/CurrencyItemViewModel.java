package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.Bundle2;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class CurrencyItemViewModel extends ViewModel {

    final MutableLiveData<String> filter;
    final MutableLiveData<Integer> currencyIsoCode = new MutableLiveData<>();
    final MutableLiveData<Bundle2<String,Integer>> symbol = new MutableLiveData<>();

    private final LiveData<Currency.Plain> currencyItem;
    private final LiveData<Currency.Plain> baseItem;

    private DataRepository dataRepository;

    @Inject
    public CurrencyItemViewModel(DataRepository dataRepository) {
        //super();
        //list = new MutableLiveData<>();

        this.dataRepository = dataRepository;

        this.filter = new MutableLiveData<>();

        currencyItem = Transformations.switchMap(currencyIsoCode,input -> CurrencyItemViewModel.this.dataRepository.getCurrency(input));
        baseItem = Transformations.switchMap(currencyIsoCode,input -> CurrencyItemViewModel.this.dataRepository.getBaseForCurrency(input));

        currencyItem.observeForever(currency->{
            if (currency != null) {
                Bundle2<String, Integer> bundle = new Bundle2<>();
                bundle.first = currency.symbol;
                bundle.second = currency.pattern;
                symbol.setValue(bundle);
            }
        });

    }

    public void setIsoCode(int iso_code) {
        if (Objects.equals(currencyIsoCode.getValue(), iso_code)) {
            return;
        }
        currencyIsoCode.setValue(iso_code);
    }

    public LiveData<Currency.Plain> getCurrency(){
        return currencyItem;
    }

    public LiveData<Currency.Plain> getBase(){
        return baseItem;
    }

    public LiveData<Bundle2<String,Integer>> getSymbol(){
        return symbol;
    }

    public void deleteCurrency(int iso_code_int) {
        dataRepository.deleteCurrency(iso_code_int);
    }

    public void setDefaultCurrency(int iso_code_int) {
        dataRepository.setDefaultCurrency(iso_code_int);
    }

    public void setSymbol(String newSymbol) {
        if (symbol.getValue() == null || Objects.equals(symbol.getValue().first, newSymbol)) {
            return;
        }
        Bundle2<String, Integer> bundle = new Bundle2<>();
        bundle.first = newSymbol;
        bundle.second = symbol.getValue().second;

        symbol.setValue(bundle);
    }

    public void setPattern(int pattern) {
        if (symbol.getValue() == null || symbol.getValue().second == pattern) {
            return;
        }
        Bundle2<String, Integer> bundle = new Bundle2<>();
        bundle.first = symbol.getValue().first;
        bundle.second = pattern;

        symbol.setValue(bundle);
    }
}
