package com.vividprojects.protoplanner.ui.fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.bindingmodels.CurrencyItemBindingModel;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.ui.helpers.ContainerFragment;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.viewmodels.CurrencyEditViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.databinding.CurrencyEditFragmentBinding;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 19.10.2017.
 */

public class CurrencyItemFragment extends ContainerFragment implements Injectable {
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;

    private CurrencyEditViewModel model;

    private CurrencyEditFragmentBinding binding;
    private CurrencyItemBindingModel bindingModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Runnable onNewImage = ()-> {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, REQUEST_IMAGE_GALLERY);
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    model.loadGalleryImage(data.getData()).observe(this, imageName->{
                        bindingModel.setImage(imageName);
                    });
                }
                return;
        }
    }

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
                model = ViewModelProviders.of(getActivity(),viewModelFactory).get(CurrencyEditViewModel.class);
                model.setIsoCode(iso_code);

                bindingModel = model.getBindingModel();
                bindingModel.setImageAction(onNewImage);

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

    @Override
    public Bundle onCloseActivity() {
        Bundle b = new Bundle();
        b.putInt("ID", model.getId());
        return b;
    }
}