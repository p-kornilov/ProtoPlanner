package com.vividprojects.protoplanner.Interface.Dialogs;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

import com.vividprojects.protoplanner.BindingModels.VariantEditBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Interface.Helpers.DialogFullScreenDialogAbstract;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.ViewModels.VariantViewModel;
import com.vividprojects.protoplanner.databinding.DialogVariantEditBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditVariantDialog extends DialogFullScreenDialogAbstract implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private VariantViewModel model;
    private VariantEditBindingModel bindingModelVariantEdit;
    private DialogVariantEditBinding binding;

    private String variantId;


    @Override
    public View getRootView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DialogVariantEditBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void observeModels() {
        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(VariantViewModel.class);
        bindingModelVariantEdit = model.getBindingModelVariantEdit();
        bindingModelVariantEdit.setEnableCheck(getEnableCheck());
        binding.setVariantEditModel(bindingModelVariantEdit);

        if (variantId != null) {
            model.setVariantId(variantId);

            model.getVariantItem().observe(this, resource -> {
                if (resource != null)
                    bindingModelVariantEdit.setVariant(resource.data);
            });

            model.getMeasures().observe(this, measures -> {
                if (measures != null)
                    bindingModelVariantEdit.setVariantEditMeasureList(Measure.Plain.sort(getContext(), measures));
            });

            model.getCurrencies().observe(this, currencies -> {
                if (currencies != null)
                    bindingModelVariantEdit.setVariantEditCurrencyList(Currency.Plain.sort(getContext(), currencies));
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null)
            variantId = b.getString("ID", null);
    }

    @Override
    public void onSave() {
        model.saveVariant(bindingModelVariantEdit.getId()
                ,bindingModelVariantEdit.getVariantEditName()
                ,bindingModelVariantEdit.getPriceNum()
                ,bindingModelVariantEdit.getCountNum()
                ,bindingModelVariantEdit.getVariantEditCurrency().iso_code_int
                ,bindingModelVariantEdit.getVariantEditMeasure().hash);
    }

    @Override
    public String getResult() {
        return model.getVariantId();
    }
}
