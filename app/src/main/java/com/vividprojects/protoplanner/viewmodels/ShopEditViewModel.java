package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.bindingmodels.ShopEditBindingModel;
import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.coredata.VariantInShop;
import com.vividprojects.protoplanner.datamanager.DataRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class ShopEditViewModel extends ViewModel {
    final MutableLiveData<String> shopId;
    private final LiveData<Resource<VariantInShop.Plain>> shopItem;

    private DataRepository dataRepository;
    private ShopEditBindingModel bindingModelShopEdit;

    @Inject
    public ShopEditViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        shopId = new MutableLiveData<>();

        bindingModelShopEdit    = new ShopEditBindingModel(dataRepository.getContext());

        shopItem = Transformations.switchMap(shopId, input -> {
            return ShopEditViewModel.this.dataRepository.loadShop(input);
        });

    }

    public void setShopId(String id) {
        shopId.setValue(id);
    }

    public LiveData<Resource<VariantInShop.Plain>> getShopItem() {
        return shopItem;
    }

    public LiveData<List<Currency.Plain>> getCurrencies() {
        return dataRepository.getCurrencies();
    }

    public ShopEditBindingModel getBindingModelShopEdit() {
        return bindingModelShopEdit;
    }

    public String saveShop(VariantInShop.Plain shop, String variantId, boolean asPrimary) {
        return dataRepository.saveShop(shop, variantId, asPrimary);
    }

}
