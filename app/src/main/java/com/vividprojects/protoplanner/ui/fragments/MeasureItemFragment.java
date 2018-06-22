package com.vividprojects.protoplanner.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.bindingmodels.MeasureItemBindingModel;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.ui.helpers.ContainerFragment;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.viewmodels.MeasureEditViewModel;
import com.vividprojects.protoplanner.databinding.MeasureEditFragmentBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class MeasureItemFragment extends ContainerFragment implements Injectable {
    private MeasureEditViewModel model;

    private MeasureEditFragmentBinding binding;
    private MeasureItemBindingModel bindingModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("Test", "onCreate - Records Fragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - RootListFragment");

        binding = MeasureEditFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();

        if (args != null && args.containsKey(NavigationController.MEASURE_HASH)){
            int hash = args.getInt(NavigationController.MEASURE_HASH);
                model = ViewModelProviders.of(getActivity(),viewModelFactory).get(MeasureEditViewModel.class);
                model.setHash(hash);

                bindingModel = model.getBindingModel();
                binding.setMeasureModel(bindingModel);

                model.getMeasure().observe(this,measure->{
                    if (measure != null)
                        bindingModel.setMeasure(measure);
                });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.item_check:
                boolean error = false;
                String errorText = "";
                if (bindingModel.getMeasureName() == null || bindingModel.getMeasureName().length()==0) {
                    errorText = "Please, correct the name. Can't be empty.";
                    error = true;
                } else if (bindingModel.getMeasureSymbol() == null || bindingModel.getMeasureSymbol().length()==0) {
                    errorText = "Please, correct the symbol. Can't be empty.";
                    error = true;
                }

                if (error) {
                    AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                    alert.setTitle("Oops...");
                    alert.setMessage(errorText);
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alert.show();
                    break;
                }
                model.save();
              //  getActivity().finish();
                break;
        }
        return true;
    }

    public static MeasureItemFragment create(int hash) {
        MeasureItemFragment measureItemFragment = new MeasureItemFragment();
        Bundle args = new Bundle();
        args.putInt(NavigationController.MEASURE_HASH, hash);
        measureItemFragment.setArguments(args);
        return measureItemFragment;
    }

    @Override
    public Bundle onCloseActivity() {
        Bundle b = new Bundle();
        b.putInt("HASH", model.getHash());
        return b;
    }

}