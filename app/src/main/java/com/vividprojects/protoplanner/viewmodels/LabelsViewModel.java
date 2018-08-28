package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.adapters.LabelsListAdapter;
import com.vividprojects.protoplanner.bindingmodels.LabelsListBindingModel;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.LabelGroup;
import com.vividprojects.protoplanner.datamanager.DataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class LabelsViewModel extends ViewModel {
    private final LiveData<List<Label.Plain>> original_labels_live;
    private final LiveData<List<LabelGroup.Plain>> original_label_groups_live;
    private final LiveData<List<Label.Plain>> original_labelsSelected_live;
    private final MutableLiveData<List<Label.Plain>> current_labels_live;
    private final MutableLiveData<List<Label.Plain>> current_labelsSelected_live;
    private List<Label.Plain> labels_avail;
    private List<Label.Plain> labels_selected;
    private final MutableLiveData<String> recordId;
    private final MutableLiveData<String> deleteLabelTrigger;
    private final LiveData<Label.Plain> onNewLabel;
    private final LiveData<Label.Plain> onStartAddLabel;
    private final LiveData<LabelGroup.Plain> onNewGroup;
    private final LiveData<Label.Plain> onEditLabel;
    private final LiveData<LabelGroup.Plain> onEditGroup;
    private final MutableLiveData<Label.Plain> newLabelTrigger = new MutableLiveData<>();
    private final MutableLiveData<String> onStartAddLabelTrigger = new MutableLiveData<>();
    private final MutableLiveData<LabelGroup.Plain> newGroupTrigger = new MutableLiveData<>();
    private final MutableLiveData<Label.Plain> editLabelTrigger = new MutableLiveData<>();
    private final MutableLiveData<LabelGroup.Plain> editGroupTrigger = new MutableLiveData<>();

    private final MutableLiveData<String> onStartEditGroupId = new MutableLiveData<>();
    private final LiveData<LabelGroup.Plain> onStartEditGroup;


    private String currentLabel = "";

    private DataRepository dataRepository;
    private LabelsListBindingModel labelsListBindingModel;

    @Inject
    public LabelsViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;

        labelsListBindingModel = new LabelsListBindingModel();

      //  all_labels_live = new MutableLiveData<>();
        current_labels_live = new MutableLiveData<>();
        current_labelsSelected_live = new MutableLiveData<>();
        deleteLabelTrigger = new MutableLiveData<>();

        labels_avail = new ArrayList<>();
        labels_selected = new ArrayList<>();

        recordId = new MutableLiveData<>();

        original_labels_live = Transformations.switchMap(recordId,id->dataRepository.getLabels());
        original_label_groups_live = Transformations.switchMap(recordId,id->dataRepository.getLabelGroups());

        original_labelsSelected_live = Transformations.switchMap(recordId,id->dataRepository.getRecordLabels(id));

        onNewLabel = Transformations.switchMap(newLabelTrigger, (label)->dataRepository.createLabel(label));
        onStartAddLabel = Transformations.switchMap(onStartAddLabelTrigger, (groupId)-> {
            Label.Plain label = null;
            LiveData<LabelGroup.Plain> g = dataRepository.getLabelGroup(groupId);
            if (g != null)
                label = Label.getPlain(-1,"",g.getValue(), null);
            MutableLiveData<Label.Plain> ml = new MutableLiveData<>();
            ml.setValue(label);
            return ml;
        });
        onNewGroup = Transformations.switchMap(newGroupTrigger, (group)->dataRepository.createLabelGroup(group));
        onEditLabel = Transformations.switchMap(editLabelTrigger, (label)->dataRepository.editLabel(label));
        onEditGroup = Transformations.switchMap(editGroupTrigger, (group)->dataRepository.editLabelGroup(group));

        onStartEditGroup = Transformations.switchMap(onStartEditGroupId, (groupId)->dataRepository.getLabelGroup(groupId));

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
    public LiveData<List<LabelGroup.Plain>> getLabelGroups() {return original_label_groups_live;};
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

    public String getCurrentLabel() {
        return currentLabel;
    }

    public void deleteCurrentLabel() {
        deleteLabelTrigger.setValue(currentLabel);
        dataRepository.deleteLabel(currentLabel);
    }

    public LiveData<String> getDeleteLabelTrigger() {
        return deleteLabelTrigger;
    }

    public LiveData<Label.Plain> getOnEditLabel() {
        return onEditLabel;
    }

    public LiveData<LabelGroup.Plain> getOnEditGroup() {
        return onEditGroup;
    }

    public void newLabel(String name, String groupId, int color) {
        newLabelTrigger.setValue(Label.getPlain(color, name, groupId,""));
    }

    public void editLabel(String name, String group, int color, String id) {
        editLabelTrigger.setValue(Label.getPlain(color, name, "", id));
    }

    public void newGroup(String name, int color) {
        newGroupTrigger.setValue(LabelGroup.getPlain(color, name, ""));
    }

    public void editGroup(String name, int color, String id) {
        editGroupTrigger.setValue(LabelGroup.getPlain(color, name, id));
    }

    public LiveData<Label.Plain> getOnNewLabel() {
        return onNewLabel;
    }

    public LiveData<LabelGroup.Plain> getOnNewGroup() {
        return onNewGroup;
    }

    public LabelsListBindingModel getLabelsListBindingModel() {
        return labelsListBindingModel;
    }

    public void deleteGroup(String groupId) {
        dataRepository.deleteLabelGroup(groupId);
    }

    public void startEditGroup(String id) {
        onStartEditGroupId.setValue(id);
    }

    public void startAddLabel(String groupId) {
        onStartAddLabelTrigger.setValue(groupId);
    }

    public LiveData<LabelGroup.Plain> getOnStartEditGroup() {
        return onStartEditGroup;
    }

    public LiveData<Label.Plain> getOnStartAddLabel() {
        return onStartAddLabel;
    }
}
