package com.vividprojects.protoplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.BindingModels.RecordItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.DataManager.DataRepository;

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
    private final MutableLiveData<String> refreshedVariantId = new MutableLiveData<>();
    private final LiveData<Resource<Variant.Plain>> refreshedVariant;
    private final MutableLiveData<String> defaultImage  = new MutableLiveData<>();

    private DataRepository dataRepository;
    private RecordItemBindingModel bindingModelRecord;

    private boolean inImageLoading = false;

    @Inject
    public RecordItemViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        recordItemId = new MutableLiveData<>();
        recordNameTrigger = new MutableLiveData<>();

        bindingModelRecord = new RecordItemBindingModel();
        bindingModelRecord.setDefaultImage(dataRepository.getDefaultVariantImage());

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

        refreshedVariant = Transformations.switchMap(refreshedVariantId, id -> {
            int i =1;
            return RecordItemViewModel.this.dataRepository.loadVariant(id);
        });
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

    public void refreshVariant(String id) {
        dataRepository.attachVariantToRecord(id, recordItemId.getValue());
        refreshedVariantId.setValue(id);
    }

    public LiveData<Resource<Variant.Plain>> getRefreshedVariant() {
        return refreshedVariant;
    }

    public MutableLiveData<String> getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String image) {
        if (Objects.equals(defaultImage.getValue(), image)) {
            return;
        }
        defaultImage.setValue(image);
    }
}
