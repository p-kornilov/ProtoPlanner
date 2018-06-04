package com.vividprojects.protoplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.vividprojects.protoplanner.BindingModels.RecordItemBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantEditBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.SingleLiveEvent;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class VariantViewModel extends ViewModel {
    final MutableLiveData<String> variantId;
    private final LiveData<Resource<Variant.Plain>> variantItem;

    private DataRepository dataRepository;
    private VariantEditBindingModel bindingModelVariantEdit;

    private boolean inImageLoading = false;

    @Inject
    public VariantViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        variantId = new MutableLiveData<>();

        bindingModelVariantEdit = new VariantEditBindingModel(dataRepository.getContext());

        variantItem = Transformations.switchMap(variantId, input -> {
            return VariantViewModel.this.dataRepository.loadVariant(input);
        });

    }

    public void setVariantId(String id) {
/*        if (Objects.equals(variantId.getValue(), id)) {
            return;
        }*/
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

    public String getVariantId() {
        return variantId.getValue();
    }


    public void saveVariant(String id, String name, double price, double count, int currency, int measure) {
        String saveId = dataRepository.saveVariant(id, name, price, count, currency, measure);
        setVariantId(saveId);
/*        LiveData<Resource<Variant.Plain>> currentVariant = dataRepository.loadVariant(saveId);
        mainVariantItem.addSource(currentVariant, new Observer<Resource<Variant.Plain>>() {
            @Override
            public void onChanged(@Nullable Resource<Variant.Plain> variant) {
                mainVariantItem.setValue(variant.data);
                mainVariantItem.removeSource(currentVariant);
            }
        });*/
    }

}
