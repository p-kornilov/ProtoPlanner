package com.vividprojects.protoplanner.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    private final LiveData<Resource<Variant.Plain>> mvItem;
    private final MediatorLiveData<Variant.Plain> mainVariantItem;
    private final MutableLiveData<List<Label.Plain>> labels;
    private final MutableLiveData<String> recordNameTrigger;
    private final LiveData<String> recordNameChange;
    private final MediatorLiveData<String> recordName;
    private final LiveData<Resource<VariantInShop.Plain>> refreshedShop;
    private final MutableLiveData<String> refreshShopId = new MutableLiveData<>();

    private DataRepository dataRepository;
    private RecordItemBindingModel bindingModelRecord;
    private VariantItemBindingModel bindingModelVariant;
    private VariantEditBindingModel bindingModelVariantEdit;

    private final SingleLiveEvent<Integer> loadProgress;

    private boolean inImageLoading = false;

    @Inject
    public RecordItemViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        recordItemId = new MutableLiveData<>();
        recordNameTrigger = new MutableLiveData<>();

        bindingModelRecord = new RecordItemBindingModel();
        bindingModelVariant = new VariantItemBindingModel(dataRepository.getContext());
        bindingModelVariantEdit = new VariantEditBindingModel(dataRepository.getContext());

        recordNameChange = Transformations.switchMap(recordNameTrigger, name->{
            return RecordItemViewModel.this.dataRepository.setRecordName(recordItemId.getValue(),name);
        });

        refreshedShop = Transformations.switchMap(refreshShopId, input -> RecordItemViewModel.this.dataRepository.loadShop(input));

        recordItem = Transformations.switchMap(recordItemId, input -> {
/*            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return dataRepository.loadRecords(input.getFilter());
            }*/
            return RecordItemViewModel.this.dataRepository.loadRecord(input);
        });
        mvItem = Transformations.switchMap(recordItem, input -> {
/*            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return dataRepository.loadRecords(input.getFilter());
            }*/
            if (!input.equals("Empty"))
                return RecordItemViewModel.this.dataRepository.loadVariant(input.data.mainVariant);
            else return null;
        });

        mainVariantItem = new MediatorLiveData<>();
        mainVariantItem.addSource(mvItem, mv -> {mainVariantItem.setValue(mv.data);});

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

    public LiveData<Variant.Plain> getMainVariantItem() {
        return mainVariantItem;
    }
    public SingleLiveEvent<Integer> getLoadProgress() {return loadProgress;}

    public LiveData<String> getRecordItemID() {
        return recordItemId;
    }

    public SingleLiveEvent<Integer> loadImage(String url) {
        if (!inImageLoading) {
            inImageLoading = true;
            bindingModelVariant.setLoadedImage(dataRepository.saveImageFromURLtoVariant(url, mainVariantItem.getValue().id, loadProgress,()->{inImageLoading=false;}));
        }
        return loadProgress;
    }

    public SingleLiveEvent<Integer> loadCameraImage(String tempFileName) {
        if (!inImageLoading) {
            inImageLoading = true;
            bindingModelVariant.setLoadedImage(dataRepository.saveImageFromCameratoVariant(tempFileName, mainVariantItem.getValue().id, loadProgress,()->{inImageLoading=false;}));
        }
        return loadProgress;
    }

    public SingleLiveEvent<Integer> loadGalleryImage(Uri fileName) {
        if (!inImageLoading) {
            inImageLoading = true;
            bindingModelVariant.setLoadedImage(dataRepository.saveImageFromGallerytoVariant(fileName, mainVariantItem.getValue().id, loadProgress,()->{inImageLoading=false;}));
        }
        return loadProgress;
    }

    public boolean isInImageLoading() {
        return inImageLoading;
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

    public LiveData<Measure.Plain> getMeasure(int hash) {
        return dataRepository.getMeasure(hash);
    }


    public RecordItemBindingModel getBindingModelRecord() {
        return bindingModelRecord;
    }
    public VariantItemBindingModel getBindingModelVariant() {
        return bindingModelVariant;
    }

    public LiveData<String> setComment(String comment) {
        return dataRepository.setRecordComment(bindingModelRecord.getRecordId(),comment);
    }

    public void saveMainVariant(String variantId) {
        if (variantId != null && (mainVariantItem.getValue() == null || !variantId.equals(mainVariantItem.getValue().id)))
            dataRepository.saveMainVariantToRecord(variantId, recordItemId.getValue());
        LiveData<Resource<Variant.Plain>> currentVariant = dataRepository.loadVariant(variantId);
        mainVariantItem.addSource(currentVariant, new Observer<Resource<Variant.Plain>>() {
            @Override
            public void onChanged(@Nullable Resource<Variant.Plain> variant) {
                mainVariantItem.setValue(variant.data);
                mainVariantItem.removeSource(currentVariant);
            }
        });
    }

    public void refreshShop(String shopId) {
        refreshShopId.setValue(shopId);
    }

    public LiveData<Resource<VariantInShop.Plain>> getRefreshedShop() {
        return refreshedShop;
    }
}
