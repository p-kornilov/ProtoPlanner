package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.SingleEventTransformations;
import com.vividprojects.protoplanner.Utils.SingleLiveEvent;

import java.security.PublicKey;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class RecordItemViewModel extends ViewModel {
    final MutableLiveData<String> recordItemId;
    private final LiveData<Resource<Record.Plain>> recordItem;
    private final LiveData<Resource<Variant.Plain>> mainVariantItem;

    private DataRepository dataRepository;

    private final SingleLiveEvent<Integer> loadProgress;
 //   private final MutableLiveData<String> loadProgressSwitcher;
 //   private final MutableLiveData<String> cameraProgressSwitcher;

//    private final SingleLiveEvent<Integer> sle;

    private String loaded_image = "";

    private boolean inImageLoading = false;
//    private final LiveData<Integer> lp;

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
            return dataRepository.loadRecord(input);
        });
        mainVariantItem = Transformations.switchMap(recordItem, input -> {
/*            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return dataRepository.loadRecords(input.getFilter());
            }*/
            return dataRepository.loadVariant(input.data.mainVariant);
        });

/*        loadProgressSwitcher = new MutableLiveData<>();
        loadProgress = Transformations.switchMap(loadProgressSwitcher, url->{
            loaded_image = dataRepository.getImageName(url);
            return dataRepository.saveImageFromURLtoVariant(url,mainVariantItem.getValue().data.title);
        });*/

/*        sle = new SingleLiveEvent<>();

        lp = Transformations.switchMap(loadProgress,p->{
            sle.setValue(p);
            Log.d("Test", "Single - " + p);

            return new MutableLiveData<>();
        });*/
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

/*    public LiveData<Integer> saveImage(String URL) {
        return dataRepository.saveImageFromURL(URL,recordItem.getValue().data.mainVariant);
    }*/


   /* public SingleLiveEvent<Integer> getLoadProgress(){
        return sle;//loadProgress;
    }
*/
/*    public LiveData<Integer> getLoadProgress(){
        return loadProgress;
    }*/

/*    public LiveData<Integer> getLp() {
        return lp;
    }*/

/*    public void load(String url) {
        loadProgressSwitcher.setValue(url);
    }*/

    public SingleLiveEvent<Integer> loadImage(String url) {
        if (!inImageLoading) {
            inImageLoading = true;
            loaded_image = dataRepository.saveImageFromURLtoVariant(url, mainVariantItem.getValue().data.title, loadProgress,()->{inImageLoading=false;});
        }
        return loadProgress;
    }

    public boolean isInImageLoading() {
        return inImageLoading;
    }

    public String getLoadedImage() {
        return loaded_image;
    }

}
