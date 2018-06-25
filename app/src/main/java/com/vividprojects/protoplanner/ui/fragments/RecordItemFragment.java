package com.vividprojects.protoplanner.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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

import com.vividprojects.protoplanner.bindingmodels.RecordItemBindingModel;
import com.vividprojects.protoplanner.bindingmodels.VariantItemBindingModel;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.ui.dialogs.EditTextDialog;
import com.vividprojects.protoplanner.ui.helpers.DialogFullScreenHelper;
import com.vividprojects.protoplanner.ui.helpers.VariantFragmentHelper;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.utils.ItemActionsShop;
import com.vividprojects.protoplanner.utils.ItemActionsVariant;
import com.vividprojects.protoplanner.viewmodels.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.viewmodels.VariantItemViewModel;
import com.vividprojects.protoplanner.databinding.RecordFragmentBinding;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 31.10.2017.
 */

public class RecordItemFragment extends Fragment implements Injectable, ItemActionsShop, ItemActionsVariant {

    public static final String RECORD_ID = "RECORD_ID";
    private static final int REQUEST_LABELS_SET = 4;
    private static final int REQUEST_EDIT_NAME = 5;
    private static final int REQUEST_EDIT_COMMENT = 6;
    private static final int REQUEST_NEW_VARIANT = 7;
    private static final int REQUEST_NEW_MAIN_VARIANT = 8;

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

    private Runnable onAddVariantClick = () -> {
        Bundle b = new Bundle();
        b.putString("ID", "");
        DialogFullScreenHelper.showDialog(DialogFullScreenHelper.DIALOG_VARIANT, this, !navigationController.isTablet(), REQUEST_NEW_VARIANT, b);
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
            ((MainActivity) getActivity()).getSecondToolBar().inflateMenu(R.menu.menu_edit);
            ((MainActivity) getActivity()).getSecondToolBar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        } else {
            if (menu.size() == 0)
                inflater.inflate(R.menu.menu_edit, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_edit:
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
        VariantFragmentHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
/*        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.
        inflater.inflate(R.menu.context_menu, menu);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (VariantFragmentHelper.onActivityResult(requestCode, resultCode, data, modelMainVariant, bindingModelVariant, this.getContext()))
            return;

        switch (requestCode) {
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
            case REQUEST_NEW_VARIANT:
                if (resultCode == RESULT_OK && data != null) {
                    String id = data.getStringExtra("ID");
                    modelRecord.addVariant(id);
                }
                return;
            case NavigationController.REQUEST_OPEN_VARIANT:
                if (resultCode == RESULT_OK && data != null) {
                    Bundle b = data.getBundleExtra("BUNDLE");
                    if (b != null) {
                        String id = b.getString("ID","");
                        if (id != null && !id.isEmpty())
                            modelRecord.refreshVariant(id);
                    }
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
                    ,VariantFragmentHelper.REQUEST_IMAGE_URL_LOAD
                    ,VariantFragmentHelper.REQUEST_IMAGE_GALLERY
                    ,VariantFragmentHelper.REQUEST_IMAGE_CAPTURE
                    ,VariantFragmentHelper.REQUEST_IMAGE_SHOW
                    ,VariantFragmentHelper.REQUEST_EDIT_SHOP
                    ,VariantFragmentHelper.REQUEST_EDIT_VARIANT
                    ,VariantFragmentHelper.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    ,navigationController.isTablet()
                    ,false);

            //добавить onResult из варианта

            bindingModelRecord = modelRecord.getBindingModelRecord();
            bindingModelRecord.setContext(this);
            bindingModelRecord.setOnCommentEditClick(onCommentEditClick);
            bindingModelRecord.setOnLabelsEditClick(onLabelsEditClick);
            bindingModelRecord.setOnAddVariantClick(onAddVariantClick);
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
                    //modelMainVariant.setVariantId(resource.data.mainVariant);
                }
            });

            modelRecord.getMainVariantId().observe(this, mainVariantId -> {
                if (mainVariantId != null) {
                    modelMainVariant.setVariantId(mainVariantId);
                }
            });

            modelMainVariant.getVariantItem().observe(this, variant -> {
                if (variant != null) {
                    bindingModelVariant.setVariant(variant);
               //     modelRecord.setDefaultImage(variant.full_images.get(variant.defaultImage));
                }
            });

            modelMainVariant.getDefaultImage().observe(this, image -> {
                if (image != null) {
                    modelRecord.setDefaultImage(image);
                }
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

            modelRecord.getRefreshedVariant().observe(this, variant -> {
                if (variant != null && variant.data != null)
                    bindingModelRecord.refreshAlternativeVariant(variant.data);
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
        VariantFragmentHelper.itemShopDelete(id, modelMainVariant);
    }

    @Override
    public void itemShopEdit(String item) {
        VariantFragmentHelper.itemShopEdit(item, bindingModelVariant, navigationController.isTablet(), this);
    }

    @Override
    public void itemShopPrimary(String id) {
        VariantFragmentHelper.itemShopPrimary(id, modelMainVariant);
    }

    @Override
    public void variantDelete(String id) {
        modelRecord.deleteVariant(id);
    }

    @Override
    public void variantOpen(String id) {
        NavigationController.openVariantForResult(id, this);
    }

    @Override
    public void variantSetBasic(String id) {
        modelRecord.setBasicVariant(id);
        //VariantFragmentHelper.variantSetBasic(id);
    }
}
