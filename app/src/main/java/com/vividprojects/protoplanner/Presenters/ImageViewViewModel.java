package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.SingleLiveEvent;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class ImageViewViewModel extends ViewModel {
    final MutableLiveData<String> variantId;
    private final LiveData<List<String>> images;

    private DataRepository dataRepository;

    @Inject
    public ImageViewViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        variantId = new MutableLiveData<>();
        images = Transformations.switchMap(variantId, input -> {
            LiveData<List<String>> ll = ImageViewViewModel.this.dataRepository.loadImagesForVariant2(input);
            return ll;
        });
    }

    public void setVariantId(String id) {
        if (Objects.equals(variantId.getValue(), id)) {
            return;
        }
        variantId.setValue(id);
    }

    public LiveData<List<String>> getImages(){
        return images;
    }

    public LiveData<String> getV(){
        return variantId;
    }
}
