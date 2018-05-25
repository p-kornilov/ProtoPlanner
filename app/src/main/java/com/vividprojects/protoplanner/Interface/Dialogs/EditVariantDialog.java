package com.vividprojects.protoplanner.Interface.Dialogs;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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

import com.vividprojects.protoplanner.BindingModels.VariantEditBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.RunnableParam;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.databinding.DialogVariantEditBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditVariantDialog extends DialogFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private RecordItemViewModel model;
    private VariantEditBindingModel bindingModelVariantEdit;
    private DialogVariantEditBinding binding;

    private AlertDialog dialog;
    private MenuItem saveMenu;

    private RunnableParam<Integer> enableCheck = (error) -> {
        if (error == 1) {
            if (navigationController.isTablet())
                dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
            else
                if (saveMenu != null) {
                    saveMenu.setIcon(R.drawable.ic_check_disabled_24dp);
                    saveMenu.setEnabled(false);
                }
        } else {
            if (navigationController.isTablet())
                dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
            else
                if (saveMenu != null) {
                    saveMenu.setIcon(R.drawable.ic_check_white_24dp);
                    saveMenu.setEnabled(true);
                }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (navigationController.isTablet()) {
            return super.onCreateView(inflater,container,savedInstanceState);
        } else {
            binding = DialogVariantEditBinding.inflate(inflater);

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
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (navigationController.isTablet()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            binding = DialogVariantEditBinding.inflate(inflater);

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
            return dialog;
        } else {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setHasOptionsMenu(true);
            return dialog;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_dialog_fullscreen, menu);
        saveMenu = menu.findItem(R.id.mdf_action_save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mdf_action_save) {
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

        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);
        bindingModelVariantEdit = model.getBindingModelVariantEdit();
        bindingModelVariantEdit.setEnableCheck(enableCheck);
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
        });

    }

    public static EditVariantDialog create() {
        return new EditVariantDialog();
    }



}
