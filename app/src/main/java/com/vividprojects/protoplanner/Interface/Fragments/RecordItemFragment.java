package com.vividprojects.protoplanner.Interface.Fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.BindingModels.RecordItemBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Images.BitmapUtils;
import com.vividprojects.protoplanner.Interface.Dialogs.EditTextDialog;
import com.vividprojects.protoplanner.Interface.Helpers.DialogFullScreenHelper;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.Utils.ItemActionsShop;
import com.vividprojects.protoplanner.Utils.ItemActionsVariant;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModels.VariantItemViewModel;
import com.vividprojects.protoplanner.databinding.RecordFragmentBinding;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 31.10.2017.
 */

public class RecordItemFragment extends Fragment implements Injectable, ItemActionsShop, ItemActionsVariant {

    public static final String RECORD_ID = "RECORD_ID";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_IMAGE_URL_LOAD = 3;
    private static final int REQUEST_LABELS_SET = 4;
    private static final int REQUEST_EDIT_NAME = 5;
    private static final int REQUEST_EDIT_COMMENT = 6;
    private static final int REQUEST_EDIT_VARIANT = 7;
    private static final int REQUEST_EDIT_SHOP = 8;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private RecordItemViewModel modelRecord;
    private VariantItemViewModel modelMainVariant;
    private boolean empty = false;

    //private String mTempPhotoPath;

    private RecordFragmentBinding binding;
    private RecordItemBindingModel bindingModelRecord;
    private VariantItemBindingModel bindingModelVariant;

    private Runnable onCommentEditClick = () -> {
        EditTextDialog editNameDialog = new EditTextDialog();
        editNameDialog.setTargetFragment(this, REQUEST_EDIT_COMMENT);
        Bundle b = new Bundle();
        b.putString("TITLE","Edit");
        b.putString("HINT","Comment");
        b.putString("POSITIVE","Save");
        b.putString("NEGATIVE","Cancel");
        b.putString("EDITTEXT", bindingModelRecord.getRecordComment());
        editNameDialog.setArguments(b);
        editNameDialog.show(getFragmentManager(), "Edit comment");
    };

    private Runnable onLabelsEditClick = () -> {
        Label.Plain[] labels = bindingModelRecord.getRecordLabels();
        int size = labels.length;
        String[] ids = new String[size];
        for (int i = 0; i < size; i++)
            ids[i] = labels[i].id;

        NavigationController.openLabelsForResult(ids,this, REQUEST_LABELS_SET);
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("Test", "onCreate - Record Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - Record Fragment");
        Bundle args = getArguments();

        if (args != null && args.containsKey(RECORD_ID)){
            //    modelRecord.setFilter();
            if (args.getString(RECORD_ID).equals("Empty"))
                empty = true;
            else
                empty= false;
        }

        View v;

        if (empty) {
            return inflater.inflate(R.layout.empty_item_fragment, container, false);
        } else {
            binding = RecordFragmentBinding.inflate(inflater);
            return binding.getRoot();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (navigationController.isTablet()) {
            ((MainActivity) getActivity()).getSecondToolBar().getMenu().clear();
            ((MainActivity) getActivity()).getSecondToolBar().inflateMenu(R.menu.menu_record);
            ((MainActivity) getActivity()).getSecondToolBar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        } else {
            if (menu.size() == 0)
                inflater.inflate(R.menu.menu_record, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.ra_edit_name:
                EditTextDialog editNameDialog = new EditTextDialog();
                editNameDialog.setTargetFragment(this, REQUEST_EDIT_NAME);
                Bundle b = new Bundle();
                b.putString("TITLE","Edit record name");
                b.putString("HINT","Name");
                b.putString("POSITIVE","Save");
                b.putString("NEGATIVE","Cancel");
                b.putString("EDITTEXT", bindingModelRecord.getRecordName());
                editNameDialog.setArguments(b);
                editNameDialog.show(getFragmentManager(), "Edit name");
                return true;
        }
        return false;
    }

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
/*        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.
        inflater.inflate(R.menu.context_menu, menu);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    bindingModelVariant.initImageLoad();
                    modelMainVariant.loadCameraImage();
                } else {
                    BitmapUtils.deleteImageFile(getContext().getApplicationContext(), modelMainVariant.getTempPhotoPath());
                }
                return;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    bindingModelVariant.initImageLoad();
                    modelMainVariant.loadGalleryImage(data.getData());
                }
                return;
            case REQUEST_IMAGE_URL_LOAD:
                if (resultCode == RESULT_OK && data != null) {
                    String url = data.getExtras().get("URL").toString();
                    bindingModelVariant.initImageLoad();
                    modelMainVariant.loadImage(url);
                }
                return;
            case REQUEST_LABELS_SET:
                if (resultCode == RESULT_OK && data != null) {
                    String[] t = data.getStringArrayExtra("SELECTED");
                    modelRecord.setLabels(data.getStringArrayExtra("SELECTED"));
                }
                return;
            case REQUEST_EDIT_NAME:
                if (resultCode == RESULT_OK && data != null) {
                    modelRecord.setRecordName(data.getStringExtra("EDITTEXT"));
                }
                return;
            case REQUEST_EDIT_COMMENT:
                if (resultCode == RESULT_OK && data != null) {
                    String comment = data.getStringExtra("EDITTEXT");
                    modelRecord.setComment(comment);
                    bindingModelRecord.setRecordComment(comment);
                    // Другой вариант
                    //modelRecord.setComment(comment).observe(this, c -> {
                    //    bindingModelRecord.setRecordComment(comment);
                    //});
                }
                return;
            case REQUEST_EDIT_VARIANT:
                if (resultCode == RESULT_OK && data != null) {
                    String id = data.getStringExtra("ID");
                    modelMainVariant.saveVariant(id);
                }
                return;
            case REQUEST_EDIT_SHOP:
                if (resultCode == RESULT_OK && data != null) {
                    String id = data.getStringExtra("ID");
                    modelMainVariant.refreshShop(id);
                }
                return;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (!empty) {
            modelRecord = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);
            modelMainVariant = ViewModelProviders.of(getActivity(), viewModelFactory).get(VariantItemViewModel.class);


            modelMainVariant.init(this
                    ,REQUEST_IMAGE_URL_LOAD
                    ,REQUEST_IMAGE_GALLERY
                    ,REQUEST_IMAGE_CAPTURE
                    ,REQUEST_EDIT_SHOP
                    ,REQUEST_EDIT_VARIANT
                    ,PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    ,navigationController.isTablet());

            bindingModelRecord = modelRecord.getBindingModelRecord();
            bindingModelRecord.setContext(this);
            bindingModelRecord.setOnCommentEditClick(onCommentEditClick);
            bindingModelRecord.setOnLabelsEditClick(onLabelsEditClick);
            binding.setRecordModel(bindingModelRecord);

            bindingModelVariant = modelMainVariant.getBindingModelVariant();
            binding.setVariantModel(bindingModelVariant);

            Bundle args = getArguments();

            if (args != null && args.containsKey(RECORD_ID)) {
                //    modelRecord.setFilter();
                String id = args.getString(RECORD_ID);
                modelRecord.setId(id);
                modelMainVariant.setRecordId(id);
            } else {
                modelRecord.setId(null);
            }

            modelRecord.getRecordItem().observe(this, resource -> {
                if (resource != null && resource.data != null) {
                    bindingModelRecord.setRecord(resource.data);
                    modelMainVariant.setVariantId(resource.data.mainVariant);
                }
            });

            modelMainVariant.getVariantItem().observe(this, resource -> {
                if (resource != null && resource != null)
                    bindingModelVariant.setVariant(resource);
            });

            if (navigationController.isTablet())
                modelRecord.getRecordName().observe(this, name -> {
                    if (name != null)
                        ((MainActivity) getActivity()).getSecondToolBar().setTitle(name);
                });

            modelMainVariant.getLoadProgress().observe(this, (progress) -> {
                if (progress != null)
                    bindingModelVariant.setLoadProgress(progress);
            });

            modelMainVariant.getRefreshedShop().observe(this, shop -> {
                if (shop != null && shop.data != null)
                    bindingModelVariant.refreshShop(shop.data);
            });

            modelRecord.getAlternativeVariants().observe(this, list -> {
                if (list != null && list.size() > 0)
                    bindingModelRecord.setAlternativeVariants(list);
            });
        }
    }

    public static RecordItemFragment create(String id) {
        RecordItemFragment recordItemFragment = new RecordItemFragment();
        Bundle args = new Bundle();
        args.putString(RECORD_ID,id);
        recordItemFragment.setArguments(args);
        return recordItemFragment;
    }

    @Override
    public void itemShopDelete(String id) {
        modelMainVariant.deleteShop(id);
    }

    @Override
    public void itemShopEdit(String item) {
        Bundle b = new Bundle();
        b.putString("ID", item);
        b.putString("VARIANTID", bindingModelVariant.getVariantId());
        DialogFullScreenHelper.showDialog(DialogFullScreenHelper.DIALOG_SHOP, this, !navigationController.isTablet(), REQUEST_EDIT_SHOP, b);
    }

    @Override
    public void itemShopPrimary(String id) {
        modelMainVariant.setShopPrimary(id);
    }

    @Override
    public void itemVariantDelete(String id) {


    }

    @Override
    public void itemVariantEdit(String id) {
        NavigationController.openVariantForResult(id, this);
    }

    @Override
    public void itemVariantBasic(String id) {

    }
}
