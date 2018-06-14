package com.vividprojects.protoplanner.ViewModels;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.BindingModels.RecordItemBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantEditBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Interface.Fragments.RecordItemFragment;
import com.vividprojects.protoplanner.Interface.Helpers.DialogFullScreenHelper;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Interface.RecordAddImageURLDialog;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Camera;
import com.vividprojects.protoplanner.Utils.RunnableParam;
import com.vividprojects.protoplanner.Utils.SingleLiveEvent;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class RecordItemViewModel extends ViewModel {
    final MutableLiveData<String> recordItemId;
    private final LiveData<Resource<Record.Plain>> recordItem;
    private final MutableLiveData<List<Label.Plain>> labels;
    private final MutableLiveData<String> recordNameTrigger;
    private final LiveData<String> recordNameChange;
    private final MediatorLiveData<String> recordName;
    private final MediatorLiveData<List<Variant.Plain>> alternativeVariants;

    private DataRepository dataRepository;
    private RecordItemBindingModel bindingModelRecord;

    private boolean inImageLoading = false;

    @Inject
    public RecordItemViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        recordItemId = new MutableLiveData<>();
        recordNameTrigger = new MutableLiveData<>();

        bindingModelRecord = new RecordItemBindingModel();

        recordNameChange = Transformations.switchMap(recordNameTrigger, name->{
            return RecordItemViewModel.this.dataRepository.setRecordName(recordItemId.getValue(),name);
        });

        recordItem = Transformations.switchMap(recordItemId, input -> {
/*            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return dataRepository.loadRecords(input.getFilter());
            }*/
            return RecordItemViewModel.this.dataRepository.loadRecord(input);
        });

        alternativeVariants = new MediatorLiveData<>();
        alternativeVariants.addSource(recordItem, ri -> {
            if (ri != null && ri.data != null)
                alternativeVariants.setValue(ri.data.variants);
        });

        recordName = new MediatorLiveData<>();
        recordName.addSource(recordNameChange,name->{recordName.setValue(name);});
        recordName.addSource(recordItem,record->{recordName.setValue(record.data.name);});

        labels = new MutableLiveData<>();
    }

    public void setId(String id) {
        if (Objects.equals(recordItemId.getValue(), id)) {
            return;
        }
        recordItemId.setValue(id);
    }

    public LiveData<Resource<Record.Plain>> getRecordItem() {
        return recordItem;
    }

    public LiveData<String> getRecordName() {
        return recordName;
    }

    public LiveData<String> getRecordItemID() {
        return recordItemId;
    }

    public LiveData<List<Label.Plain>> getLabels() {
        return dataRepository.getLabels(labels);
    }

    public void setLabels(String[] ids) {
        dataRepository.saveLabelsForRecord(recordItemId.getValue(),ids);
        recordItemId.setValue(recordItemId.getValue());
    }

    public void setRecordName(String name) {
        if (Objects.equals(recordNameTrigger.getValue(), name)) {
            return;
        }
        recordNameTrigger.setValue(name);
    }

    public RecordItemBindingModel getBindingModelRecord() {
        return bindingModelRecord;
    }

    public LiveData<String> setComment(String comment) {
        return dataRepository.setRecordComment(bindingModelRecord.getRecordId(),comment);
    }

    public LiveData<List<Variant.Plain>> getAlternativeVariants() {
        return alternativeVariants;
    }
}
