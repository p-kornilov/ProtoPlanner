package com.vividprojects.protoplanner.bindingmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.LabelGroup;


import java.lang.ref.WeakReference;
import java.util.List;

public class LabelsItemListBindingModel extends BaseObservable {
    private List<Label.Plain> labels;

    private boolean showGroup = false;
    private WeakReference<Context> context;
    private LabelGroup.Plain group;
    private boolean selectedSort = false;
    private boolean nameSort = false;
    private String filter = "";

    public LabelsItemListBindingModel(Context context, List<Label.Plain> labels, LabelGroup.Plain group, boolean showGroup) {
        this.labels = labels;
        this.group = group;
        this.showGroup = showGroup;

        this.context = new WeakReference<>(context);

        notifyPropertyChanged(BR.labelsListLabels);
        notifyPropertyChanged(BR.labelsListGroupName);
        notifyPropertyChanged(BR.labelsListGroupVisible);
    }

    @Bindable
    public int getLabelsListGroupColor() {
        return group != null ? group.color : 0xffffffff;
    }

    @Bindable
    public String getLabelsListGroupName() {
        return group != null ? group.name : "";
    }

    @Bindable
    public boolean getLabelsListGroupVisible() {
        return showGroup;
    }

    @Bindable
    public boolean getLabelsListSelectedSort() {
        return selectedSort;
    }

    @Bindable
    public boolean getLabelsListNameSort() {
        return nameSort;
    }

    @Bindable
    public String getLabelsListFilter() {
        return filter;
    }

    @Bindable
    public Label.Plain[] getLabelsListLabels() {
        return labels.toArray(new Label.Plain[labels.size()]);
    }

    public void setSelectedSort(boolean selectedSort) {
        this.selectedSort = selectedSort;
        notifyPropertyChanged(BR.labelsListSelectedSort);
    }

    public void setNameSort(boolean nameSort) {
        this.nameSort = nameSort;
        notifyPropertyChanged(BR.labelsListNameSort);
    }

    public void setFilter(String filter) {
        this.filter = filter;
        notifyPropertyChanged(BR.labelsListFilter);
    }
}
