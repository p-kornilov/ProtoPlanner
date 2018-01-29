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

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class LabelsViewModel extends ViewModel {
    private final MutableLiveData<List<Label.Plain>> all_labels_live;
    private final MutableLiveData<List<Label.Plain>> all_labelsRecord_live;
    private final MutableLiveData<List<Label.Plain>> labels_avail_live;
    private final MutableLiveData<List<Label.Plain>> labels_selected_live;
    private Set<Label.Plain> labels_avail;
    private Set<Label.Plain> labels_selected;
    private final MutableLiveData<String> recordItemId;

    private DataRepository dataRepository;

    @Inject
    public LabelsViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;

      //  all_labels_live = new MutableLiveData<>();
        all_labelsRecord_live = new MutableLiveData<>();

        labels_avail_live = new MutableLiveData<>();
        labels_selected_live = new MutableLiveData<>();
        recordItemId = new MutableLiveData<>();

        all_labels_live = Transformations.switchMap(recordItemId,(id)->{
            return labels_avail_live;
        });

        labels_avail = new HashSet<Label.Plain>();
        labels_selected = new HashSet<Label.Plain>();

    }

    public LiveData<List<Label.Plain>> get() {return all_labels_live;};

    public LiveData<List<Label.Plain>> getLabels() {
        dataRepository.getLabels(all_labels_live);
        return dataRepository.getLabels(all_labels_live);
    }

    public LiveData<List<Label.Plain>> getLabelsRecord(String id) {
        return dataRepository.getLabelsRecord(all_labelsRecord_live, id);
    }

    public void init(String recordId) {
        if (Objects.equals(recordItemId.getValue(), recordId)) {
            return;
        }
        recordItemId.setValue(recordId);

        dataRepository.getLabels(all_labels_live);
        dataRepository.getLabelsRecord(all_labelsRecord_live, recordId);

        labels_avail.addAll(all_labels_live.getValue());
        labels_selected.addAll(all_labelsRecord_live.getValue());
    }
}
