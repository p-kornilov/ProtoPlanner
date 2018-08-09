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

import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.bindingmodels.BlockItemBindingModel;
import com.vividprojects.protoplanner.databinding.BlockFragmentBinding;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.ui.dialogs.EditTextDialog;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.viewmodels.BlockItemViewModel;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 31.10.2017.
 */

public class BlockItemFragment extends Fragment implements Injectable, ItemActionsRecord {

    public static final String BLOCK_ID = "BLOCK_ID";
    private static final int REQUEST_LABELS_SET = 4;
    private static final int REQUEST_EDIT_NAME = 5;
    private static final int REQUEST_EDIT_COMMENT = 6;
    private static final int REQUEST_NEW_VARIANT = 7;
    private static final int REQUEST_CREATE_BASIC_VARIANT = 8;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private BlockItemViewModel modelBlock;
    private boolean empty = false;

    //private String mTempPhotoPath;

    private BlockFragmentBinding binding;
    private BlockItemBindingModel bindingModelBlock;

    private Runnable onCommentEditClick = () -> {
        EditTextDialog editNameDialog = new EditTextDialog();
        editNameDialog.setTargetFragment(this, REQUEST_EDIT_COMMENT);
        Bundle b = new Bundle();
        b.putString("TITLE","Edit");
        b.putString("HINT","Comment");
        b.putString("POSITIVE","Save");
        b.putString("NEGATIVE","Cancel");
        b.putString("EDITTEXT", bindingModelBlock.getBlockComment());
        editNameDialog.setArguments(b);
        editNameDialog.show(getFragmentManager(), "Edit comment");
    };

    private Runnable onLabelsEditClick = () -> {
/*        Label.Plain[] labels = bindingModelBlock.getRecordLabels();
        int size = labels.length;
        String[] ids = new String[size];
        for (int i = 0; i < size; i++)
            ids[i] = labels[i].id;

        NavigationController.openLabelsForResult(ids,this, REQUEST_LABELS_SET);*/
    };

    private Runnable onAddRecordClick = () -> {
/*        Bundle b = new Bundle();
        b.putString("ID", "");
        DialogFullScreenHelper.showDialog(DialogFullScreenHelper.DIALOG_VARIANT, this, !navigationController.isTablet(), REQUEST_NEW_VARIANT, b);*/
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("Test", "onCreate - Block Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - Record Fragment");
        Bundle args = getArguments();

        if (args != null && args.containsKey(BLOCK_ID)){
            //    modelBlock.setFilter();
            if (args.getString(BLOCK_ID).equals("Empty"))
                empty = true;
            else
                empty= false;
        }

        if (empty) {
            return inflater.inflate(R.layout.empty_item_fragment, container, false);
        } else {
            binding = BlockFragmentBinding.inflate(inflater);
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
                inflater.inflate(R.menu.menu_block_item, menu);
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
                b.putString("TITLE","Edit block name");
                b.putString("HINT","Name");
                b.putString("POSITIVE","Save");
                b.putString("NEGATIVE","Cancel");
                b.putString("EDITTEXT", bindingModelBlock.getBlockName());
                editNameDialog.setArguments(b);
                editNameDialog.show(getFragmentManager(), "Edit name");
                return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

/*        if (VariantFragmentHelper.onActivityResult(requestCode, resultCode, data, modelMainVariant, bindingModelVariant, this.getContext()))
            return;

        switch (requestCode) {
            case REQUEST_LABELS_SET:
                if (resultCode == RESULT_OK && data != null) {
                    String[] t = data.getStringArrayExtra("SELECTED");
                    modelBlock.setLabels(data.getStringArrayExtra("SELECTED"));
                }
                return;
            case REQUEST_EDIT_NAME:
                if (resultCode == RESULT_OK && data != null) {
                    modelBlock.setRecordName(data.getStringExtra("EDITTEXT"));
                }
                return;
            case REQUEST_EDIT_COMMENT:
                if (resultCode == RESULT_OK && data != null) {
                    String comment = data.getStringExtra("EDITTEXT");
                    modelBlock.setComment(comment);
                    bindingModelBlock.setRecordComment(comment);
                    // Другой вариант
                    //modelBlock.setComment(comment).observe(this, c -> {
                    //    bindingModelBlock.setRecordComment(comment);
                    //});
                }
                return;
            case REQUEST_NEW_VARIANT:
                if (resultCode == RESULT_OK && data != null) {
                    String id = data.getStringExtra("ID");
                    modelBlock.addVariant(id);
                }
                return;
            case REQUEST_CREATE_BASIC_VARIANT:
                if (resultCode == RESULT_OK && data != null) {
                    String id = data.getStringExtra("ID");
                    modelBlock.createBasicVariant(id);
                }
                return;
            case NavigationController.REQUEST_OPEN_VARIANT:
                if (resultCode == RESULT_OK && data != null) {
                    Bundle b = data.getBundleExtra("BUNDLE");
                    if (b != null) {
                        String id = b.getString("ID","");
                        if (id != null && !id.isEmpty())
                            modelBlock.refreshVariant(id);
                    }
                }
                return;
        }*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (!empty) {
            modelBlock = ViewModelProviders.of(getActivity(), viewModelFactory).get(BlockItemViewModel.class);

            bindingModelBlock = modelBlock.getBindingModelBlock();
            bindingModelBlock.setContext(this);
            bindingModelBlock.setOnCommentEditClick(onCommentEditClick);
            bindingModelBlock.setOnLabelsEditClick(onLabelsEditClick);
            binding.setBlockModel(bindingModelBlock);

            Bundle args = getArguments();

            if (args != null && args.containsKey(BLOCK_ID)) {
                //    modelBlock.setFilter();
                String id = args.getString(BLOCK_ID);
                modelBlock.setId(id);
            } else {
                modelBlock.setId(null);
            }

            modelBlock.getBlockItem().observe(this, resource -> {
                if (resource != null && resource.data != null) {
                    bindingModelBlock.setBlock(resource.data);
                    //modelMainVariant.setVariantId(resource.data.mainVariant);
                }
            });

            modelBlock.getRecords().observe(this, records -> {
                if (records != null && records.data != null) {
                    bindingModelBlock.setRecords(records.data);
                }
            });

        }
    }

    public static BlockItemFragment create(String id) {
        BlockItemFragment blockItemFragment = new BlockItemFragment();
        Bundle args = new Bundle();
        args.putString(BLOCK_ID,id);
        blockItemFragment.setArguments(args);
        return blockItemFragment;
    }

    @Override
    public void itemRecordDelete(String id) {
        //modelBlock.deleteRecord(id);
    }

    @Override
    public void itemRecordEdit(String id) {
        //NavigationController.openRecordForResult(id, this);
    }

    @Override
    public void itemRecordAttachToBlock(String id) {}

}
