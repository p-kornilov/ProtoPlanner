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
import com.vividprojects.protoplanner.ViewModels.VariantItemViewModel;
import com.vividprojects.protoplanner.databinding.DialogVariantEditBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditVariantDialog extends DialogFullScreenDialogAbstract implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private VariantItemViewModel model;
    private VariantEditBindingModel bindingModelVariantEdit;
    private ShopEditBindingModel bindingModelShopEdit;
    private DialogVariantEditBinding binding;

    private String variantId;

    private boolean ecVariant = true;
    private boolean ecShop = true;

    private RunnableParam<Integer> enableCheckVariant = (error) -> {
        if (error == 1)
            ecVariant = false;
        else
            ecVariant = true;
        enableCheck();
    };

    private RunnableParam<Integer> enableCheckShop = (error) -> {
        if (error == 1)
            ecShop = false;
        else
            ecShop = true;
        enableCheck();
    };

    private void enableCheck() {
        if (ecVariant && ecShop)
            enableButtons();
        else
            disableButtons();
    }


    @Override
    public View getRootView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DialogVariantEditBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void observeModels() {
        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(VariantItemViewModel.class);

        bindingModelVariantEdit = model.getBindingModelVariantEdit();
        bindingModelVariantEdit.setEnableCheck(enableCheckVariant);

        bindingModelShopEdit = model.getBindingModelShopEdit();
        bindingModelShopEdit.setEnableCheck(enableCheckShop);

        binding.setVariantEditModel(bindingModelVariantEdit);
        binding.setShopEditModel(bindingModelShopEdit);

        if (variantId != null) {
            model.setVariantId(variantId);

            model.getVariantItem().observe(this, resource -> {
                if (resource != null) {
                    bindingModelVariantEdit.setVariant(resource);
                    bindingModelShopEdit.setShop(resource.primaryShop);
                }
            });

            model.getMeasures().observe(this, measures -> {
                if (measures != null)
                    bindingModelVariantEdit.setVariantEditMeasureList(Measure.Plain.sort(getContext(), measures));
            });

            model.getCurrencies().observe(this, currencies -> {
                if (currencies != null)
                    bindingModelShopEdit.setShopEditCurrencyList(Currency.Plain.sort(getContext(), currencies));
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Bundle b = bundle.getBundle("BUNDLE");
            if (b != null)
                variantId = b.getString("ID", null);
        }
    }

    @Override
    public void onSave() {
        model.saveVariant(bindingModelVariantEdit.getId()
                ,bindingModelVariantEdit.getVariantEditName()
                ,bindingModelShopEdit.getPriceNum()
                ,bindingModelVariantEdit.getCountNum()
                ,bindingModelShopEdit.getShopEditCurrency().iso_code_int
                ,bindingModelVariantEdit.getVariantEditMeasure().hash);
    }

    @Override
    public String getResult() {
        return model.getVariantId();
    }
}
