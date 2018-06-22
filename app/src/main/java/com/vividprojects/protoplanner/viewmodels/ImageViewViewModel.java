package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.utils.Bundle2;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class ImageViewViewModel extends ViewModel {
    final MutableLiveData<String> variantId;
    private final LiveData<Bundle2<List<String>,Integer>> images;

    private DataRepository dataRepository;

    @Inject
    public ImageViewViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        variantId = new MutableLiveData<>();
        images = Transformations.switchMap(variantId, input -> ImageViewViewModel.this.dataRepository.loadImagesForVariant2(input));
    }

    public void setVariantId(String id) {
        if (Objects.equals(variantId.getValue(), id)) {
            return;
        }
        variantId.setValue(id);
    }

    public LiveData<Bundle2<List<String>,Integer>> getImages(){
        return images;
    }

    public LiveData<String> getV(){
        return variantId;
    }
}
