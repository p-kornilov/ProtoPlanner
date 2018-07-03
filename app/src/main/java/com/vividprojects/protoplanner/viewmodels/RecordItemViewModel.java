package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.vividprojects.protoplanner.bindingmodels.RecordItemBindingModel;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.datamanager.DataRepository;

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
    private final MediatorLiveData<String> mainVariantId = new MediatorLiveData<>();

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

        mainVariantId.addSource(recordItem,record->{
            if (record != null && record.data != null)
                mainVariantId.setValue(record.data.mainVariant.id);
        });

        labels = new MutableLiveData<>();

        refreshedVariant = Transformations.switchMap(refreshedVariantId, id -> RecordItemViewModel.this.dataRepository.loadVariant(id));
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
        //dataRepository.attachVariantToRecord(id, recordItemId.getValue());
        refreshedVariantId.setValue(id);
    }

    public void addVariant(String id) {
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

    public void deleteVariant(String id) {
        dataRepository.deleteVariant(id);
    }

    public void setBasicVariant(String id) {
        if (recordItem != null && recordItem.getValue().data != null) {
            refreshedVariantId.setValue(mainVariantId.getValue());
            LiveData<String> v = dataRepository.setBasicVariant(recordItem.getValue().data.id, id);
            mainVariantId.addSource(v, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String mainVariant) {
                    mainVariantId.setValue(mainVariant);
                    mainVariantId.removeSource(v);
                }
            });
        }
    }

    public LiveData<String> getMainVariantId() {
        return mainVariantId;
    }
}
