package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.SingleEventTransformations;
import com.vividprojects.protoplanner.Utils.SingleLiveEvent;

import java.security.PublicKey;
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

    private DataRepository dataRepository;

    private final SingleLiveEvent<Integer> loadProgress;

    private String loaded_image = "";

    private boolean inImageLoading = false;

    @Inject
    public RecordItemViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        recordItemId = new MutableLiveData<>();
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
    public LiveData<Resource<Variant.Plain>> getMainVariantItem() {
        return mainVariantItem;
    }
    public SingleLiveEvent<Integer> getLoadProgress() {return loadProgress;}

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

}
