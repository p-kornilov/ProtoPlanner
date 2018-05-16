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
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Bundle2;
import com.vividprojects.protoplanner.Utils.Bundle3;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.Widgets.BindingHelper;

import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditVariantDialog extends DialogFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private EditText titleET;
    private EditText priceET;
    private EditText countET;
    private Spinner measuresSpinner;
    private Spinner currenciesSpinner;

    private RecordItemViewModel model;

    private int selectedMeasure = 0;
    private int selectedCurrency = -1;
    private List<Measure.Plain> measuresList;
    private List<Currency.Plain> currenciesList;
    //private Bundle2<Integer,String>[] measuresList;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_variant_edit, null);

        titleET = v.findViewById(R.id.ved_name);
        priceET = v.findViewById(R.id.ved_price);
        countET = v.findViewById(R.id.ved_count);
        measuresSpinner = v.findViewById(R.id.ved_measure);
        currenciesSpinner = v.findViewById(R.id.ved_currency);


        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

        model.getMainVariantItem().observe(this, resource -> {
                    if (resource != null && resource.data != null) {
                        titleET.setText(resource.data.title);
                        priceET.setText(String.valueOf(resource.data.price));
                        countET.setText(String.valueOf(resource.data.count));
                        selectedMeasure = resource.data.measure.hash;
                        selectedCurrency = resource.data.currency.iso_code_int;
                        selectMeasure();
                      //  measuresSpinner.setSelection(resource.data.measure.hash);
                    }
                });

        model.getMeasures().observe(this, measures -> {
            if (measures != null) {
                measuresList = Measure.Plain.sort(getContext(), measures);
                setupMeasuresSpinner();
                selectMeasure();
            }
        });

        model.getCurrencies().observe(this, currencies -> {
            if (currencies != null) {
                currenciesList = Currency.Plain.sort(getContext(), currencies);
                setupCurrenciesSpinner();
                selectCurrency();
            }
        });

        builder.setView(v)
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
                        EditVariantDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void saveVariant() {
        String name = titleET.getText().toString();
        double price;
        try {
            price = Double.parseDouble(priceET.getText().toString());
        } catch (NumberFormatException e) {
            error = true;
            сделать databinding для диалога, сделать проверку типов и индикацию ошибки, сделать сохранение
        }
    }

    private void selectMeasure() {
        if (selectedMeasure != 0 && measuresList != null) {
            for (int i = 0; i < measuresList.size(); i++)
                if (measuresList.get(i).hash == selectedMeasure) {
                    measuresSpinner.setSelection(i);
                    return;
                }
            model.getMeasure(selectedMeasure).observe(this, measure ->{
                if (measure != null) {
                    measuresList.add(measure);
                    measuresList = Measure.Plain.sort(getContext(), measuresList);
                    setupMeasuresSpinner();
                    for (int i = 0; i < measuresList.size(); i++)
                        if (measuresList.get(i).hash == selectedMeasure) {
                            measuresSpinner.setSelection(i);
                            return;
                        }
                }
            });
        }
    }

    private void selectCurrency() {
        if (selectedCurrency != -1 && currenciesList != null) {
            for (int i = 0; i < currenciesList.size(); i++)
                if (currenciesList.get(i).iso_code_int == selectedCurrency) {
                    currenciesSpinner.setSelection(i);
                    return;
                }
        }
    }

    private void setupMeasuresSpinner() {
        int i = 0;
        Bundle2<Integer,String>[] al = new Bundle2[measuresList.size()];
        for (Measure.Plain m : measuresList) {
            Bundle2<Integer,String> b = new Bundle2<>();
            b.first = BindingHelper.getMeasureImageResource(m.measure);
            b.second = Measure.Plain.getString(getContext(),m.symbol,m.symbolId);
            al[i] = b;
            i++;
        }
        SpinnerMeasureAdapter spinnerAdapter = new SpinnerMeasureAdapter(getContext(), al);
        measuresSpinner.setAdapter(spinnerAdapter);
    }

    private void setupCurrenciesSpinner() {
        int i = 0;
        Bundle3<Integer,String,String>[] al = new Bundle3[currenciesList.size()];
        for (Currency.Plain c : currenciesList) {
            Bundle3<Integer,String,String> b = new Bundle3<>();
            b.first = c.flag_id;
            b.second = c.symbol;
            b.third = c.iso_code_str;
            al[i] = b;
            i++;
        }
        SpinnerCurrencyAdapter spinnerAdapter = new SpinnerCurrencyAdapter(getContext(), al);
        currenciesSpinner.setAdapter(spinnerAdapter);
    }

    public static EditVariantDialog create() {
        return new EditVariantDialog();
    }

}
