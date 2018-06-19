package com.vividprojects.protoplanner.Interface.Fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.BindingModels.VariantItemBindingModel;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Interface.Helpers.VariantFragmentHelper;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.ItemActionsShop;
import com.vividprojects.protoplanner.Utils.ItemActionsVariant;
import com.vividprojects.protoplanner.ViewModels.VariantItemViewModel;
import com.vividprojects.protoplanner.databinding.VariantFragmentBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class VariantItemFragment extends Fragment implements Injectable, ItemActionsShop, ItemActionsVariant {

    private VariantItemViewModel model;

    private VariantFragmentBinding binding;
    private VariantItemBindingModel bindingModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

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

        binding = VariantFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        VariantFragmentHelper.onActivityResult(requestCode, resultCode, data,model, bindingModel, this.getContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        VariantFragmentHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();

        if (args != null && args.containsKey(NavigationController.VARIANT_ID)){
            String id = args.getString(NavigationController.VARIANT_ID);
            model = ViewModelProviders.of(getActivity(),viewModelFactory).get(VariantItemViewModel.class);

            model.init(this
                    ,VariantFragmentHelper.REQUEST_IMAGE_URL_LOAD
                    ,VariantFragmentHelper.REQUEST_IMAGE_GALLERY
                    ,VariantFragmentHelper.REQUEST_IMAGE_CAPTURE
                    ,VariantFragmentHelper.REQUEST_IMAGE_SHOW
                    ,VariantFragmentHelper.REQUEST_EDIT_SHOP
                    ,VariantFragmentHelper.REQUEST_EDIT_VARIANT
                    ,VariantFragmentHelper.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    ,navigationController.isTablet()
                    ,true);

            model.setVariantId(id);

            bindingModel = model.getBindingModelVariant();
            binding.setVariantModel(bindingModel);

            model.getVariantItem().observe(this, variantResource ->{
                if (variantResource != null)
                    bindingModel.setVariant(variantResource);
            });

            model.getLoadProgress().observe(this, (progress) -> {
                if (progress != null)
                    bindingModel.setLoadProgress(progress);
            });

            model.getRefreshedShop().observe(this, shop -> {
                if (shop != null && shop.data != null)
                    bindingModel.refreshShop(shop.data);
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_edit:
                model.onVariantEditClick.run();
/*                boolean error = false;
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
                model.save();*/
              //  getActivity().finish();
                return true;
        }
        return true;
    }

    public static VariantItemFragment create(String id) {
        VariantItemFragment variantItemFragment = new VariantItemFragment();
        Bundle args = new Bundle();
        args.putString(NavigationController.VARIANT_ID, id);
        variantItemFragment.setArguments(args);
        return variantItemFragment;
    }

    @Override
    public void itemShopDelete(String id) {
        VariantFragmentHelper.itemShopDelete(id, model);
    }

    @Override
    public void itemShopEdit(String item) {
        VariantFragmentHelper.itemShopEdit(item, bindingModel, navigationController.isTablet(), this);
    }

    @Override
    public void itemShopPrimary(String id) {
        VariantFragmentHelper.itemShopPrimary(id, model);
    }

    @Override
    public void itemVariantDelete(String id) {
        VariantFragmentHelper.itemVariantDelete(id);
    }

    @Override
    public void itemVariantOpen(String id) {
        VariantFragmentHelper.itemVariantOpen(id, this);
    }

    @Override
    public void itemVariantBasic(String id) {
        VariantFragmentHelper.itemVariantBasic(id);
    }
}