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
    private final LiveData<List<Label.Plain>> all_labels_live;
    private final MutableLiveData<List<Label.Plain>> all_labelsRecord_live;
    private final LiveData<List<Label.Plain>> labels_avail_live;
    private final MutableLiveData<List<Label.Plain>> labels_selected_live;
    private List<Label.Plain> labels_avail;
    private List<Label.Plain> labels_selected;
    private final MutableLiveData<String> recordItemId;
    private final MutableLiveData<String> refresh;

    private DataRepository dataRepository;

    @Inject
    public LabelsViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;

      //  all_labels_live = new MutableLiveData<>();
        all_labelsRecord_live = new MutableLiveData<>();

//        labels_avail_live = new MutableLiveData<>();
        labels_selected_live = new MutableLiveData<>();
        recordItemId = new MutableLiveData<>();
        refresh = new MutableLiveData<>();

        all_labels_live = Transformations.switchMap(refresh,(r)->{
            MutableLiveData<List<Label.Plain>> data = new MutableLiveData<>();
            data.setValue(labels_avail);
            return data;
        });

        all_labels_live = Transformations.switchMap(refresh,(r)->{
            MutableLiveData<List<Label.Plain>> data = new MutableLiveData<>();
            data.setValue(labels_avail);
            return data;
        });

        Transformations.switchMap(all_labels_live,labels->{
            labels_avail.addAll(labels);
            return null;
        });

        refresh();

        labels_avail = new ArrayList<>();
        labels_selected = new ArrayList<>();

    }

    public LiveData<List<Label.Plain>> get() {return all_labels_live;};

/*    public LiveData<List<Label.Plain>> getLabels() {
        dataRepository.getLabels(all_labels_live);
        return dataRepository.getLabels(all_labels_live);
    }*/

    public LiveData<List<Label.Plain>> getLabelsRecord(String id) {
        return dataRepository.getLabelsRecord(all_labelsRecord_live, id);
    }

    public void selectLabel(Label.Plain label) {
        labels_selected.add(label);
        labels_avail.remove(label);
        refreshLive();
    }

    public void deselectLabel(Label.Plain label) {
        labels_selected.remove(label);
        labels_avail.add(label);
        refreshLive();
    }

    public void refreshLive() {
        refresh.setValue("refresh");
    }
}
