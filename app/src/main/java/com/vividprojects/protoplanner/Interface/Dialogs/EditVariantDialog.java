package com.vividprojects.protoplanner.Interface.Dialogs;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.vividprojects.protoplanner.Utils.RunnableParam;
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

    private AlertDialog dialog;

    private RunnableParam<Integer> enableCheck = (error) -> {
        if (error == 1) {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

  //      binding = DialogVariantEditBinding.inflate(inflater);

        View rootView = binding.getRoot();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Dialog title");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        binding = DialogVariantEditBinding.inflate(inflater);

        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

        bindingModelVariantEdit = model.getBindingModelVariantEdit();
        bindingModelVariantEdit.setEnableCheck(enableCheck);
        binding.setVariantEditModel(bindingModelVariantEdit);

        builder.setView(binding.getRoot())
                .setTitle("Basic variant")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        model.saveVariant(bindingModelVariantEdit.getId()
                                ,bindingModelVariantEdit.getVariantEditName()
                                ,bindingModelVariantEdit.getPriceNum()
                                ,bindingModelVariantEdit.getCountNum()
                                ,bindingModelVariantEdit.getVariantEditCurrency().iso_code_int
                                ,bindingModelVariantEdit.getVariantEditMeasure().hash);
                    }
                })
                .setNegativeButton("Cancel", null);
        dialog = builder.create();

        //-----------------------------
        View rootV = binding.getRoot();
        Toolbar toolbar = rootV.findViewById(R.id.toolbar);
        toolbar.setTitle("Dialog title");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //-----------------------------
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_ak, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            // handle confirmation button click here
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
/*        model.getMainVariantItem().observe(this, resource -> {
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

    public static EditVariantDialog create() {
        return new EditVariantDialog();
    }

}
