package com.vividprojects.protoplanner.ViewModels;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.BindingModels.ShopEditBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantEditBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Interface.AddImageURLDialog;
import com.vividprojects.protoplanner.Interface.Helpers.DialogFullScreenHelper;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Camera;
import com.vividprojects.protoplanner.Utils.RunnableParam;
import com.vividprojects.protoplanner.Utils.SingleLiveEvent;

import java.io.File;
import java.util.List;
import java.util.Objects;

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
