package com.vividprojects.protoplanner.ui.dialogs;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.vividprojects.protoplanner.bindingmodels.ShopEditBindingModel;
import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.ui.helpers.DialogFullScreenDialogAbstract;
import com.vividprojects.protoplanner.utils.RunnableParam;
import com.vividprojects.protoplanner.viewmodels.ShopEditViewModel;
import com.vividprojects.protoplanner.databinding.DialogVariantShopEditBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditShopDialog extends DialogFullScreenDialogAbstract implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ShopEditViewModel model;
    private ShopEditBindingModel bindingModelShopEdit;
    private DialogVariantShopEditBinding binding;

    private String shopId;
    private String variantId;
    private boolean ecShop = true;

    private RunnableParam<Boolean> enableCheckShop = (error) -> {
        ecShop = !error;
        enableCheck();
    };

    private void enableCheck() {
        if (ecShop)
            enableButtons();
        else
            disableButtons();
    }


    @Override
    public View getRootView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DialogVariantShopEditBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void observeModels() {
        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(ShopEditViewModel.class);

        bindingModelShopEdit = model.getBindingModelShopEdit();
        bindingModelShopEdit.setEnableCheck(enableCheckShop);

        binding.setShopEditModel(bindingModelShopEdit);

        if (shopId != null) {
            model.setShopId(shopId);

            model.getShopItem().observe(this, resource -> {
                if (resource != null) {
                    bindingModelShopEdit.setShop(resource.data);
                }
            });

        }

        model.getCurrencies().observe(this, currencies -> {
            if (currencies != null)
                bindingModelShopEdit.setShopEditCurrencyList(Currency.Plain.sort(getContext(), currencies));
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Bundle b = bundle.getBundle("BUNDLE");
            if (b != null) {
                shopId = b.getString("ID", null);
                variantId = b.getString("VARIANTID", null);
            }
        }
    }

    @Override
    public void onSave() {
        String shopId = model.saveShop(bindingModelShopEdit.getShop(), variantId, false);
        bindingModelShopEdit.setShopId(shopId);
    }

    @Override
    public String getResult() {
        return bindingModelShopEdit.getId();
    }
}
