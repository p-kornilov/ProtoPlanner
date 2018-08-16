package com.vividprojects.protoplanner.bindingmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.coredata.Label;


import java.lang.ref.WeakReference;
import java.util.List;

public class LabelsItemListBindingModel extends BaseObservable {
    private List<Label.Plain> labels;

    private boolean showGroup = false;
    private WeakReference<Context> context;
    private String groupName;
    private boolean selectedSort = false;
    private boolean nameSort = false;
    private String filter = "";

    public LabelsItemListBindingModel(Context context, List<Label.Plain> labels, String groupName, boolean showGroup) {
        this.labels = labels;
        this.groupName = groupName;
        this.showGroup = showGroup;

        this.context = new WeakReference<>(context);

        notifyPropertyChanged(BR.labelsListLabels);
        notifyPropertyChanged(BR.labelsListGroupName);
        notifyPropertyChanged(BR.labelsListGroupVisible);
    }

    @Bindable
    public String getLabelsListGroupName() {
        return groupName;
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
