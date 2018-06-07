package com.vividprojects.protoplanner.Interface.Dialogs;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.vividprojects.protoplanner.BindingModels.ShopEditBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantEditBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Interface.Helpers.DialogFullScreenDialogAbstract;
import com.vividprojects.protoplanner.Utils.RunnableParam;
import com.vividprojects.protoplanner.ViewModels.ShopViewModel;
import com.vividprojects.protoplanner.ViewModels.VariantViewModel;
import com.vividprojects.protoplanner.databinding.DialogVariantShopEditBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditShopDialog extends DialogFullScreenDialogAbstract implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ShopViewModel model;
    private ShopEditBindingModel bindingModelShopEdit;
    private DialogVariantShopEditBinding binding;

    private String shopId;
    private String variantId;
    private boolean ecShop = true;

    private RunnableParam<Integer> enableCheckShop = (error) -> {
        if (error == 1)
            ecShop = false;
        else
            ecShop = true;
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
        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(ShopViewModel.class);

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
        model.saveShop(bindingModelShopEdit.getShop(), variantId, false);
    }

    @Override
    public String getResult() {
        return model.getShopId();
    }
}
