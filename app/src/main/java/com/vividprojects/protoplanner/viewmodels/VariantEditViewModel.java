package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.bindingmodels.ShopEditBindingModel;
import com.vividprojects.protoplanner.bindingmodels.VariantEditBindingModel;
import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.coredata.Measure;
import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.datamanager.DataRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class VariantEditViewModel extends ViewModel {
    private final LiveData<Resource<Variant.Plain>> variantItem;
    private final MutableLiveData<String> variantId = new MutableLiveData<>();

    private DataRepository dataRepository;
    private VariantEditBindingModel bindingModelVariantEdit;
    private ShopEditBindingModel bindingModelShopEdit;

    @Inject
    public VariantEditViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;

        bindingModelVariantEdit = new VariantEditBindingModel(dataRepository.getContext());
        bindingModelShopEdit    = new ShopEditBindingModel(dataRepository.getContext());

        variantItem = Transformations.switchMap(variantId, input -> {
            if (!input.equals("Empty"))
                return VariantEditViewModel.this.dataRepository.loadVariant(input);
            else return null;
        });

    }

    public void setVariantId(String id) {
        variantId.setValue(id);
    }

    public LiveData<Resource<Variant.Plain>> getVariantItem() {
        return variantItem;
    }

    public LiveData<List<Measure.Plain>> getMeasures() {
        return dataRepository.getMeasures();
    }

    public LiveData<Measure.Plain> getMeasure(int hash) {
        return dataRepository.getMeasure(hash);
    }

    public LiveData<List<Currency.Plain>> getCurrencies() {
        return dataRepository.getCurrencies();
    }

    public VariantEditBindingModel getBindingModelVariantEdit() {
        return bindingModelVariantEdit;
    }

    public ShopEditBindingModel getBindingModelShopEdit() {
        return bindingModelShopEdit;
    }

    public String getVariantId() {
        return variantId.getValue();
    }

    public void saveVariant(String id, String name, double price, double count, int currency, int measure) {
        String saveId = dataRepository.saveVariant(id, name, price, count, currency, measure);
        setVariantId(saveId);
    }
}
