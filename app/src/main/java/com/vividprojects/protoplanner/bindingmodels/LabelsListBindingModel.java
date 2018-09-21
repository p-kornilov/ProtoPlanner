package com.vividprojects.protoplanner.bindingmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.adapters.LabelsListAdapter;
import com.vividprojects.protoplanner.adapters.RecordListAdapter;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.LabelGroup;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.widgets.FabManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LabelsListBindingModel extends BaseObservable {
    private LabelsListAdapter labelsListAdapter;

    private WeakReference<Runnable> onFabClick;
    private boolean startedForResult;
    private boolean selectedSort = false;
    private boolean nameSort = false;
    private String[] selected = null;
    private List<Label.Plain> labelList = null;
    private List<LabelGroup.Plain> labelGroupList = null;

    public void setContext(Context context, boolean showGroups, boolean startedForResult) {
        this.labelsListAdapter = new LabelsListAdapter(context, showGroups, startedForResult);
        this.startedForResult = startedForResult;
    }

    public void setListLayoutManager(LinearLayoutManager layoutManager) {
        labelsListAdapter.setListLayoutManager(layoutManager);
    }

    @Bindable
    public RecyclerView.Adapter getLabelsListAdapter() {
        return labelsListAdapter;
    }

    @Bindable
    public boolean getStartedForResult() {
        return startedForResult;
    }

    public void setLabelsList(List<Label.Plain> list, String[] selected) {
        if (this.labelList != null) {
            this.labelList.clear();
            this.labelList.addAll(list);
        } else
            this.labelList = new ArrayList<>(list);
        this.selected = selected;
        if (this.labelGroupList != null) {
            labelsListAdapter.setData(this.labelList, this.labelGroupList, selected);
            notifyPropertyChanged(BR.labelsListAdapter);
        }
    }

    public String[] getSelected() {
        return labelsListAdapter.getSelected();
    }

    public void setLabelGroupsList(List<LabelGroup.Plain> list) {
        if (this.labelGroupList != null) {
            this.labelGroupList.clear();
            this.labelGroupList.addAll(list);
        } else
            this.labelGroupList = new ArrayList<>(list);
        if (this.labelList != null) {
            labelsListAdapter.setData(this.labelList, this.labelGroupList, selected);
            notifyPropertyChanged(BR.labelsListAdapter);
        }
    }

    public void addGroup(LabelGroup.Plain group) {
        labelsListAdapter.addGroup(group);
        notifyPropertyChanged(BR.labelsListAdapter);
    }

    public void refreshLabel(Label.Plain label) {
        labelsListAdapter.refreshLabel(label);
        //notifyPropertyChanged(BR.labelsListAdapter);
    }

    public void deleteLabel(Label.Plain label) {
        labelsListAdapter.deleteLabel(label);
    }

    public void editGroup(LabelGroup.Plain group) {
        labelsListAdapter.editGroup(group);
        notifyPropertyChanged(BR.labelsListAdapter);
    }

    public void setSelectedSort(boolean selectedSort) {
        this.selectedSort = selectedSort;
        labelsListAdapter.setSelectedSort(selectedSort);
    }

    public void setNameSort(boolean nameSort) {
        this.nameSort = nameSort;
        labelsListAdapter.setNameSort(nameSort);
    }

/*
    public void refreshRecord(Record.Plain record) {
        recordListAdapter.refresh(record);
        notifyPropertyChanged(BR.recordListAdapter);
    }
*/
    public void setOnFabClick(Runnable func) {
        this.onFabClick = new WeakReference<>(func);
    }

    public void onFabClick() {
        if (onFabClick != null && onFabClick.get() != null)
            onFabClick.get().run();
    }

    public void setFilter(String filter) {
        labelsListAdapter.setFilter(filter);
    }

}
