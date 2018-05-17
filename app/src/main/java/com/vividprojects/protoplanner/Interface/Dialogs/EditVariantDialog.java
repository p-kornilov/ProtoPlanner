package com.vividprojects.protoplanner.Interface.Dialogs;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.vividprojects.protoplanner.Adapters.SpinnerCurrencyAdapter;
import com.vividprojects.protoplanner.Adapters.SpinnerMeasureAdapter;
import com.vividprojects.protoplanner.BindingModels.VariantEditBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Bundle2;
import com.vividprojects.protoplanner.Utils.Bundle3;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.Widgets.BindingHelper;
import com.vividprojects.protoplanner.databinding.DialogVariantEditBinding;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditVariantDialog extends DialogFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecordItemViewModel model;
    private VariantEditBindingModel bindingModelVariantEdit;
    private DialogVariantEditBinding binding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        binding = DialogVariantEditBinding.inflate(inflater);

        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

        bindingModelVariantEdit = model.getBindingModelVariantEdit();
        binding.setVariantEditModel(bindingModelVariantEdit);

        builder.setView(binding.getRoot())
                .setTitle("Basic variant")
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
/*
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Title",titleET.getText().toString());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, resultIntent);
*/
                       // model.saveMainVariant
                        saveVariant();
                        EditVariantDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //EditVariantDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model.getMainVariantItem().observe(this, resource -> {
            if (resource != null && resource.data != null)
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

    private void saveVariant() {
/*        String name = titleET.getText().toString();
        double price;
        try {
            price = Double.parseDouble(priceET.getText().toString());
        } catch (NumberFormatException e) {
         *//*   error = true;
            сделать databinding для диалога, сделать проверку типов и индикацию ошибки, сделать сохранение*//*
        }*/
    }

    public static EditVariantDialog create() {
        return new EditVariantDialog();
    }

}
