package com.vividprojects.protoplanner.bindingmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.LabelGroup;
import com.vividprojects.protoplanner.utils.ItemActionsLabel;
import com.vividprojects.protoplanner.widgets.ChipsLayout;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LabelsItemListBindingModel extends BaseObservable {
    private List<Label.Plain> labels = new ArrayList<>();
    private Label.Plain insertedLabel = null;
    private Label.Plain editedLabel = null;
    private String removedLabel = null;
    private String[] selectedId = null;
    private String[] selected = null;

    private boolean showGroup = false;
    private WeakReference<Context> context;
    private LabelGroup.Plain group;
    private boolean selectedSort = false;
    private boolean nameSort = false;
    private String filter = "";
    private ItemActionsLabel listAdapter;
    private boolean startedForResult;

    public LabelsItemListBindingModel(Context context, ItemActionsLabel listAdapter, List<Label.Plain> labels, LabelGroup.Plain group, boolean showGroup, String[] selectedId, boolean startedForResult) {
        this.labels.clear();
        if (labels != null)
            this.labels.addAll(labels);
        this.group = group;
        this.showGroup = showGroup;
        this.listAdapter = listAdapter;
        this.selectedId = selectedId;
        this.startedForResult = startedForResult;

        this.context = new WeakReference<>(context);

        notifyPropertyChanged(BR.labelsListLabels);
        notifyPropertyChanged(BR.labelsListGroupName);
        notifyPropertyChanged(BR.labelsListGroupVisible);
        notifyPropertyChanged(BR.labelsSelected);
        notifyPropertyChanged(BR.labelsMode);
    }

    public void refreshLabel(Label.Plain label) {
        int pos = -1;
        for (Label.Plain l : labels)
            if (l.id.equals(label.id)) {
                pos = labels.indexOf(l);
                l.color = label.color;
                l.name = label.name;
                editedLabel = label;
                notifyPropertyChanged(BR.labelsEditedLabel);
                break;
            }
        if (pos == -1) {
            labels.add(label);
            insertedLabel = label;
            notifyPropertyChanged(BR.labelsInsertedLabel);
        }
    }

    public void deleteLabel(String labelId) {
        for (Label.Plain l : labels)
            if (l.id.equals(labelId)) {
                removedLabel = l.id;
                labels.remove(labels.indexOf(l));
                notifyPropertyChanged(BR.labelsRemovedLabel);
                break;
            }
    }

    public LabelGroup.Plain getGroup() {
        return group;
    }

    public void setGroup(LabelGroup.Plain group) {
        this.group = group;
    }

    @Bindable
    public String[] getLabelsSelected() {
        return selectedId;
    }

    @Bindable
    public int getLabelsMode() {
        return startedForResult ? ChipsLayout.MODE_FULL : ChipsLayout.MODE_NON_SELECTABLE;
    }

    @Bindable
    public void setLabelsSelected(String[] selected) {
        this.selected = selected;
    }

    public String[] getSelected() {
        return selected;
    }

    @Bindable
    public Label.Plain getLabelsEditedLabel() {
        return editedLabel;
    }

    @Bindable
    public String getLabelsRemovedLabel() {
        return removedLabel;
    }

    @Bindable
    public Label.Plain getLabelsInsertedLabel() {
        return insertedLabel;
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
        return labels != null ? labels.toArray(new Label.Plain[labels.size()]) : null;
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

    public void onEditGroupClick(View view) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mge_edit:
                        listAdapter.itemGroupEdit(group.id);
                        return true;
                    case R.id.mge_delete:
                        listAdapter.itemGroupDelete(group.id);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_group_edit, popup.getMenu());
        popup.show();
    }

    public void onAddLabelClick() {
        listAdapter.itemLabelAdd(group.id);
    }

}
