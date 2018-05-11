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

import com.vividprojects.protoplanner.Adapters.SpinnerMeasureAdapter;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Bundle2;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditVariantDialog extends DialogFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    EditText titleET;
    EditText priceET;
    EditText countET;

    Spinner measuresSpinner;

    RecordItemViewModel model;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_variant_edit, null);

        titleET = v.findViewById(R.id.ved_name);
        priceET = v.findViewById(R.id.ved_price);
        countET = v.findViewById(R.id.ved_count);
        measuresSpinner = v.findViewById(R.id.ved_measure);

/*        int size = itemsText.length;
        Bundle2<Integer,String>[] al = new Bundle2[size];

        for (int i = 0; i < itemsText.length; i++) {
            Bundle2<Integer,String> b = new Bundle2<>();
            b.first = itemsImage.getResourceId(i,0);
            b.second = itemsText[i];
            al[i] = b;
        }

        SpinnerImageAdapter spinnerAdapter = new SpinnerImageAdapter(spinner.getContext(), adapterItem, adapterDropItem, textViewId, imageViewId, al);
        int p = spinner.getSelectedItemPosition();
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(p);*/

        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

        model.getMainVariantItem().observe(this, resource -> {
                    if (resource != null && resource.data != null) {
                        titleET.setText(resource.data.title);
                        priceET.setText(String.valueOf(resource.data.price));
                        countET.setText(String.valueOf(resource.data.count));
                    }
                });

        model.getMeasures().observe(this, measures -> {
            if (measures != null) {
                Bundle2<Integer,String>[] al = new Bundle2[measures.size()];
                int i = 0;
                for (Measure.Plain m : measures) {
                    Bundle2<Integer,String> b = new Bundle2<>();
                    b.first = R.drawable.measure_numeric;
                    b.second = Measure.Plain.getString(getContext(),m.symbol,m.symbolId);
                    al[i] = b;
                    i++;
                }
                SpinnerMeasureAdapter spinnerAdapter = new SpinnerMeasureAdapter(getContext(), R.layout.spinner_measure_item, R.layout.spinner_measure_item_dropdown, R.id.spinner_item, R.id.spinner_image, al);
                measuresSpinner.setAdapter(spinnerAdapter);
            }
        });

        builder.setView(v)
                .setTitle("Basic variant")
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Title",titleET.getText().toString());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, resultIntent);
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

    public static EditVariantDialog create() {
        return new EditVariantDialog();
    }

}
