package com.vividprojects.protoplanner.Interface.Dialogs;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

import com.vividprojects.protoplanner.BindingModels.VariantEditBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Interface.Helpers.DialogFullScreenDialogAbstract;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.databinding.DialogVariantEditBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditVariantDialog_ extends DialogFullScreenDialogAbstract implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecordItemViewModel model;
    private VariantEditBindingModel bindingModelVariantEdit;
    private DialogVariantEditBinding binding;


    @Override
    public View getRootView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DialogVariantEditBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void observeModels() {
/*        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);
        bindingModelVariantEdit = model.getBindingModelVariantEdit();
        bindingModelVariantEdit.setEnableCheck(getEnableCheck());
        binding.setVariantEditModel(bindingModelVariantEdit);

        model.getMainVariantItem().observe(this, resource -> {
            if (resource != null)
                bindingModelVariantEdit.setVariant(resource);
        });

        model.getMeasures().observe(this, measures -> {
            if (measures != null)
                bindingModelVariantEdit.setVariantEditMeasureList(Measure.Plain.sort(getContext(), measures));
        });

        model.getCurrencies().observe(this, currencies -> {
            if (currencies != null)
                bindingModelVariantEdit.setVariantEditCurrencyList(Currency.Plain.sort(getContext(), currencies));
        });*/
    }

    @Override
    public void onSave() {
/*        model.saveVariant(bindingModelVariantEdit.getId()
                ,bindingModelVariantEdit.getVariantEditName()
                ,bindingModelVariantEdit.getPriceNum()
                ,bindingModelVariantEdit.getCountNum()
                ,bindingModelVariantEdit.getVariantEditCurrency().iso_code_int
                ,bindingModelVariantEdit.getVariantEditMeasure().hash);*/
    }
}
