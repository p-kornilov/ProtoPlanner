package com.vividprojects.protoplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.net.Uri;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.Settings;
import com.vividprojects.protoplanner.Utils.SingleLiveEvent;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class RecordItemViewModel extends ViewModel {
    final MutableLiveData<String> recordItemId;
    private final LiveData<Resource<Record.Plain>> recordItem;
    private final LiveData<Resource<Variant.Plain>> mainVariantItem;
    private final MutableLiveData<List<Label.Plain>> labels;
    private final MutableLiveData<String> recordNameTrigger;
    private final LiveData<String> recordNameChange;
    private final MediatorLiveData<String> recordName;

    private DataRepository dataRepository;

    private final SingleLiveEvent<Integer> loadProgress;

    private String loaded_image = "";

    private boolean inImageLoading = false;

    @Inject
    public RecordItemViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        recordItemId = new MutableLiveData<>();
        recordNameTrigger = new MutableLiveData<>();

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
        mainVariantItem = Transformations.switchMap(recordItem, input -> {
/*            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return dataRepository.loadRecords(input.getFilter());
            }*/
            if (!input.equals("Empty"))
                return RecordItemViewModel.this.dataRepository.loadVariant(input.data.mainVariant);
            else return null;
        });

        recordName = new MediatorLiveData<>();
        recordName.addSource(recordNameChange,name->{recordName.setValue(name);});
        recordName.addSource(recordItem,record->{recordName.setValue(record.data.name);});

        labels = new MutableLiveData<>();

        loadProgress = new SingleLiveEvent<>();

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

    public LiveData<Resource<Variant.Plain>> getMainVariantItem() {
        return mainVariantItem;
    }
    public SingleLiveEvent<Integer> getLoadProgress() {return loadProgress;}

    public LiveData<String> getRecordItemID() {
        return recordItemId;
    }

    public SingleLiveEvent<Integer> loadImage(String url) {
        if (!inImageLoading) {
            inImageLoading = true;
            loaded_image = dataRepository.saveImageFromURLtoVariant(url, mainVariantItem.getValue().data.title, loadProgress,()->{inImageLoading=false;});
        }
        return loadProgress;
    }

    public SingleLiveEvent<Integer> loadCameraImage(String tempFileName) {
        if (!inImageLoading) {
            inImageLoading = true;
            loaded_image = dataRepository.saveImageFromCameratoVariant(tempFileName, mainVariantItem.getValue().data.title, loadProgress,()->{inImageLoading=false;});
        }
        return loadProgress;
    }

    public SingleLiveEvent<Integer> loadGalleryImage(Uri fileName) {
        if (!inImageLoading) {
            inImageLoading = true;
            loaded_image = dataRepository.saveImageFromGallerytoVariant(fileName, mainVariantItem.getValue().data.title, loadProgress,()->{inImageLoading=false;});
        }
        return loadProgress;
    }

    public boolean isInImageLoading() {
        return inImageLoading;
    }

    public String getLoadedImage() {
        return loaded_image;
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

    public LiveData<List<Measure.Plain>> getMeasures() {
        return dataRepository.getMeasures();
    }

    public LiveData<Measure.Plain> getMeasure(int hash) {
        return dataRepository.getMeasure(hash);
    }

    public LiveData<List<Currency.Plain>> getCurrencies() {
        return dataRepository.getCurrencies();
    }

}
