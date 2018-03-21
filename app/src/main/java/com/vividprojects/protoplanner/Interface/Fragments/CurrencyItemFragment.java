package com.vividprojects.protoplanner.Interface.Fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.BindingModels.CurrencyItemBindingModel;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.ViewModels.CurrencyItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.databinding.CurrencyEditFragmentBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class CurrencyItemFragment extends Fragment implements Injectable {
    private CurrencyItemViewModel model;

    private CurrencyEditFragmentBinding binding;
    private CurrencyItemBindingModel bindingModel;

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

        binding = CurrencyEditFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();

        if (args != null && args.containsKey(NavigationController.CURRENCY_ID)){
            int iso_code = args.getInt(NavigationController.CURRENCY_ID);
           // if (iso_code > 0) {
                model = ViewModelProviders.of(getActivity(),viewModelFactory).get(CurrencyItemViewModel.class);
                model.setIsoCode(iso_code);

                bindingModel = model.getBindingModel();

                binding.setSymbolModel(bindingModel);

                model.getCurrency().observe(this,currency->{
                    if (currency != null)
                        bindingModel.setCurrency(currency);
                });

                model.getBase().observe(this,base->{
                    if (base != null)
                        bindingModel.setBase(base);
                });
          //  }
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
                if (!bindingModel.getCheckCode() || bindingModel.getCurrencyCode() == null) {
                    errorText = "Please, correct the code. It must be 3 characters long.";
                    error = true;
                }
                if (!bindingModel.getStatus()) {
                    errorText = "Please, correct the symbol. Wrong unicode format.";
                    error = true;
                }
                if (bindingModel.getCurrencyName() == null) {
                    errorText = "Please, correct the name. Can't be empty.";
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

    public static CurrencyItemFragment create(int iso_code) {
        CurrencyItemFragment currencyItemFragment = new CurrencyItemFragment();
        Bundle args = new Bundle();
        args.putInt(NavigationController.CURRENCY_ID, iso_code);
        currencyItemFragment.setArguments(args);
        return currencyItemFragment;
    }

}