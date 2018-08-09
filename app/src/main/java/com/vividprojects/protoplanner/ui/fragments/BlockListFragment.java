package com.vividprojects.protoplanner.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.bindingmodels.BlockListBindingModel;
import com.vividprojects.protoplanner.databinding.BlocksFragmentBinding;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.ui.dialogs.EditTextDialog;
import com.vividprojects.protoplanner.utils.ItemActionsBlock;
import com.vividprojects.protoplanner.viewmodels.BlockListViewModel;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 19.10.2017.
 */

public class BlockListFragment extends Fragment implements Injectable, ItemActionsBlock {
    private static final int REQUEST_NEW_BLOCK_NAME = 1;

    private BlocksFragmentBinding binding;
    private BlockListBindingModel bindingModelBlocks;
    private BlockListViewModel model;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private Runnable addAction = () -> {
        EditTextDialog setBlockNameDialog = new EditTextDialog();
        setBlockNameDialog.setTargetFragment(this, REQUEST_NEW_BLOCK_NAME);
        Bundle b = new Bundle();
        b.putString("TITLE","Set new block name");
        b.putString("HINT","Name");
        b.putString("POSITIVE","Create");
        b.putString("NEGATIVE","Cancel");
        b.putString("EDITTEXT", "");
        setBlockNameDialog.setArguments(b);
        setBlockNameDialog.show(getFragmentManager(), "Edit name");
    };

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
        binding = BlocksFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity(),viewModelFactory).get(BlockListViewModel.class);
        bindingModelBlocks = model.getBlockListBindingModel();
        bindingModelBlocks.setContext(this);
        binding.setBlocksModel(bindingModelBlocks);

        model.setFilter(null);
        model.setActionAdd(addAction);

        model.getList().observe(this,resource -> {
            if (resource != null && resource.data != null) {
                bindingModelBlocks.setBlockList(resource.data);
                if (navigationController.isTablet())
                    navigationController.openBlock("Empty");
            }
        });

        model.getRefreshedBlock().observe(this, resource -> {
            if (resource != null && resource.data != null)
                bindingModelBlocks.refreshBlock(resource.data);
        });

        model.getChangedBlock().observe(this, data -> {
            if (data != null)
                bindingModelBlocks.refreshBlock(data);
        });

        model.getNewBlock().observe(this, block -> {
            if (block != null) {
                bindingModelBlocks.refreshBlock(block);
                model.subscribeToBlock(block.id);
                navigationController.openBlock(block.id);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_NEW_BLOCK_NAME:
                if (resultCode == RESULT_OK && data != null) {
                    model.createNewBlock(data.getStringExtra("EDITTEXT"));
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!navigationController.isTablet()) {
            bindingModelBlocks.clearSelected();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Test","Entered - Submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                if (TextUtils.isEmpty(filter)) {
/*
                    adapter.filter("");
                    listView.clearTextFilter();
*/
                    bindingModelBlocks.setFilter(filter);
                    Log.d("Test","Entered - Empty");
                } else {
                    Log.d("Test","Entered - " + filter);
                    bindingModelBlocks.setFilter(filter);
/*
                    adapter.filter(newText);
*/
                }
                return true;
            }
        });
    }

    @Override
    public void itemBlockDelete(String id) {
        model.deleteBlock(id);
        if (navigationController.isTablet())
            navigationController.openBlock("Empty");
    }

    @Override
    public void itemBlockEdit(String id) {
        model.subscribeToBlock(id);
        navigationController.openBlock(id);
    }

}