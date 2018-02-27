package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Exchange;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.Bundle2;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class CurrencyListViewModel extends ViewModel {

    final MutableLiveData<String> filter;

    private final LiveData<List<Currency.Plain>> listCurrency;

    private DataRepository dataRepository;

    @Inject
    public CurrencyListViewModel(DataRepository dataRepository) {
        //super();
        //list = new MutableLiveData<>();

        this.dataRepository = dataRepository;

        this.filter = new MutableLiveData<>();

        listCurrency = Transformations.switchMap(filter,input -> {
            return CurrencyListViewModel.this.dataRepository.getCurrencies();
        });

    }

    public void setFilter(String ids) {
        if (Objects.equals(filter.getValue(), ids)) {
            return;
        }
        filter.setValue(ids);
    }

    public LiveData<List<Currency.Plain>> getList(){

        return listCurrency;
    }
}
