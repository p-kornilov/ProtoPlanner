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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class LabelsViewModel extends ViewModel {
    private final LiveData<List<Label.Plain>> original_labels_live;
    private final LiveData<List<Label.Plain>> original_labelsSelected_live;
    private final MutableLiveData<List<Label.Plain>> current_labels_live;
    private final MutableLiveData<List<Label.Plain>> current_labelsSelected_live;
    private List<Label.Plain> labels_avail;
    private List<Label.Plain> labels_selected;
    private final MutableLiveData<String> recordId;

    private String currentLabel = "";

    private DataRepository dataRepository;

    @Inject
    public LabelsViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;

      //  all_labels_live = new MutableLiveData<>();
        current_labels_live = new MutableLiveData<>();
        current_labelsSelected_live = new MutableLiveData<>();

        labels_avail = new ArrayList<>();
        labels_selected = new ArrayList<>();

        recordId = new MutableLiveData<>();

        original_labels_live = Transformations.switchMap(recordId,id->dataRepository.getLabels());


        original_labelsSelected_live = Transformations.switchMap(recordId,id->dataRepository.getRecordLabels(id));



        original_labels_live.observeForever(labels->{
            labels_avail.clear();
            labels_avail.addAll(labels);
            current_labels_live.setValue(labels_avail);
        });

        original_labelsSelected_live.observeForever(labels->{
            labels_selected.clear();
            labels_selected.addAll(labels);
            current_labelsSelected_live.setValue(labels_selected);
        });

    }

    public LiveData<List<Label.Plain>> getLabels() {return current_labels_live;};
    public LiveData<List<Label.Plain>> getSelectedLabels() {return current_labelsSelected_live;};

    public void selectLabel(Label.Plain label) {
        labels_selected.add(label);
        labels_avail.remove(label);
        current_labelsSelected_live.setValue(labels_selected);
        current_labels_live.setValue(labels_avail);
    }

    public void deselectLabel(Label.Plain label) {
        labels_selected.remove(label);
        labels_avail.add(label);
        current_labelsSelected_live.setValue(labels_selected);
        current_labels_live.setValue(labels_avail);
    }

    public void refreshOriginal(String id) {
        recordId.setValue(id);
    }

    public void setCurrentLabel(String id) {
        currentLabel = new String(id);
        int i =1;
    }

    public void deleteCurrentLabel() {
        dataRepository.deleteLabel(currentLabel);
    }
}
