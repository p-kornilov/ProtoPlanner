package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.provider.ContactsContract;

import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.DataManager.DataRepository;

import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class RecordItemViewModel extends ViewModel {
    final MutableLiveData<String> recordItemId;
    private final LiveData<Resource<Record>> recordItem;
    private DataRepository dataRepository;

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

    }

    public void setId(String id) {
        if (Objects.equals(recordItemId.getValue(), id)) {
            return;
        }
        recordItemId.setValue(id);
    }

    public LiveData<Resource<Record>> getRecordItem() {
        return recordItem;
    }

    public LiveData<Integer> saveImage(String URL) {
        return dataRepository.saveImageFromURL(URL,recordItem.getValue().data.getMainVariant());
    }

}
