package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.utils.ItemNew;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class CurrencyListViewModel extends ViewModel implements ItemNew {

    final MutableLiveData<String> filter;

    private final LiveData<List<Currency.Plain>> listCurrency;

    private DataRepository dataRepository;

    final private MutableLiveData<Integer> refreshId = new MutableLiveData<>();

    final private LiveData<Currency.Plain> refreshCurrency;
    final private MutableLiveData<Integer> onNewTrigger = new MutableLiveData<>();

    @Inject
    public CurrencyListViewModel(DataRepository dataRepository) {
        //super();
        //list = new MutableLiveData<>();

        this.dataRepository = dataRepository;

        this.filter = new MutableLiveData<>();

        listCurrency = Transformations.switchMap(filter,input -> {
            return CurrencyListViewModel.this.dataRepository.getCurrencies();
        });

        refreshCurrency = Transformations.switchMap(refreshId,input -> {
            return CurrencyListViewModel.this.dataRepository.getCurrency(input);
        });

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

    public LiveData<List<Currency.Plain>> getList(){

        return listCurrency;
    }

    public LiveData<Currency.Plain> getRefreshCurrency() {
        return refreshCurrency;
    }

    public LiveData<Currency.Plain> getBase() {
        return dataRepository.getCurrencyBase();
    }

    public void deleteCurrency(int iso_code_int) {
        dataRepository.deleteCurrency(iso_code_int);
    }

    public void setDefaultCurrency(int iso_code_int) {
        dataRepository.setDefaultCurrency(iso_code_int);
    }

    public LiveData<Integer> getOnNewTrigger() {
        return onNewTrigger;
    }

    @Override
    public void itemNew() {
        onNewTrigger.setValue(1);
    }

    public String getImagesDirectory() {
        return dataRepository.getImagesDirectory();
    }
}
