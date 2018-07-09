package com.vividprojects.protoplanner.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.bindingmodels.RecordListBindingModel;
import com.vividprojects.protoplanner.databinding.RecordsFragmentBinding;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.ui.dialogs.EditTextDialog;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.viewmodels.RecordListViewModel;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 19.10.2017.
 */

public class RecordListFragment extends Fragment implements Injectable, ItemActionsRecord {
    private static final int REQUEST_NEW_RECORD_NAME = 1;

    //private static final int REQUEST_EDIT_RECORD = 1;

    private RecordsFragmentBinding binding;
    private RecordListBindingModel bindingModelRecords;
    private RecordListViewModel model;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private Runnable addAction = () -> {
        EditTextDialog setRecordNameDialog = new EditTextDialog();
        setRecordNameDialog.setTargetFragment(this, REQUEST_NEW_RECORD_NAME);
        Bundle b = new Bundle();
        b.putString("TITLE","Set new record name");
        b.putString("HINT","Name");
        b.putString("POSITIVE","Create");
        b.putString("NEGATIVE","Cancel");
        b.putString("EDITTEXT", "");
        setRecordNameDialog.setArguments(b);
        setRecordNameDialog.show(getFragmentManager(), "Edit name");
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test", "onCreate - Records Fragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RecordsFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity(),viewModelFactory).get(RecordListViewModel.class);
        bindingModelRecords = model.getRecordListBindingModel();
        bindingModelRecords.setContext(this);
        binding.setRecordsModel(bindingModelRecords);

        model.setFilter(null);
        model.setActionAdd(addAction);

        model.getList().observe(this,resource -> {
            if (resource != null && resource.data != null)
                bindingModelRecords.setRecordList(resource.data);
        });

        model.getRefreshedRecord().observe(this, resource -> {
            if (resource != null && resource.data != null)
                bindingModelRecords.refreshRecord(resource.data);
        });

        model.getChangedRecord().observe(this, data -> {
            if (data != null)
                bindingModelRecords.refreshRecord(data);
        });

        model.getNewRecord().observe(this, record -> {
            if (record != null) {
                bindingModelRecords.refreshRecord(record);
              //  model.subscribeToRecord(record.id);
                navigationController.openRecord(record.id);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_NEW_RECORD_NAME:
                if (resultCode == RESULT_OK && data != null) {
                    model.createNewRecord(data.getStringExtra("EDITTEXT"));
                }
        }
    }

    @Override
    public void itemRecordDelete(String item) {

    }

    @Override
    public void itemRecordEdit(String recordId) {
        model.subscribeToRecord(recordId);
        navigationController.openRecord(recordId);
    }

    @Override
    public void itemRecordAttachToBlock(String item) {

    }
}