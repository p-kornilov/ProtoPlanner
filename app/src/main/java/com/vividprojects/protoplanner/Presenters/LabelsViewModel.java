package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import com.vividprojects.protoplanner.CoreData.Label;
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

public class LabelsViewModel extends ViewModel {
    private final MutableLiveData<List<Label.Plain>> labels;
    private final MutableLiveData<List<Label.Plain>> labelsRecord;

    private DataRepository dataRepository;

    @Inject
    public LabelsViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        labels = new MutableLiveData<>();
        labelsRecord = new MutableLiveData<>();
    }

    public LiveData<List<Label.Plain>> getLabels() {
        return dataRepository.getLabels(labels);
    }

    public LiveData<List<Label.Plain>> getLabelsRecord(String id) {
        return dataRepository.getLabelsRecord(labels, id);
    }
}
