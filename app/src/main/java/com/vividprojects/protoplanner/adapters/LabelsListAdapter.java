package com.vividprojects.protoplanner.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.bindingmodels.LabelsItemListBindingModel;
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.LabelGroup;
import com.vividprojects.protoplanner.utils.DeleteDialogHelper;
import com.vividprojects.protoplanner.utils.ItemActionsLabel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabelsListAdapter extends DataBindingAdapter implements ItemActionsLabel {
    private List<Label.Plain> dataLabels = new ArrayList<>();
    private List<LabelGroup.Plain> dataGroups = new ArrayList<>();
    private List<Label.Plain> filtered_dataLabels = new ArrayList<>();
    private List<LabelGroup.Plain> filtered_dataGroups = new ArrayList<>();
    private List<LabelsItemListBindingModel> models = new ArrayList<>();

    private WeakReference<Context> context;
    private String filter;
    private boolean showGroups = false;
    private ItemActionsLabel master;

    private LinearLayoutManager layoutManager;

    public LabelsListAdapter(Context context, boolean showGroups) {
        this.context = new WeakReference<>(context);
        this.showGroups = showGroups;
    }

    public void setListLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.labels_item;
    }

    @Override
    public int getItemCount() {
        return models != null ? models.size() : 0;
    }

    @Override
    public Object getObjForPosition(int position) {
        return models.get(position);
    }

    public void setData(List<Label.Plain> dataLabels, List<LabelGroup.Plain> dataGroups) {
        this.dataLabels.clear();
        this.dataGroups.clear();
        this.dataLabels.addAll(dataLabels);
        this.dataGroups.addAll(dataGroups);

        this.filtered_dataLabels = this.dataLabels;
        this.filtered_dataGroups = this.dataGroups;
        createModels();
    }

    public void addGroup(LabelGroup.Plain group) {
        dataGroups.add(group);
        LabelsItemListBindingModel model = new LabelsItemListBindingModel(context.get(), this, null, group, showGroups);
        models.add(model);
        notifyItemInserted(models.size()-1);
        scrollTo(models.size()-1);
    }

    public void refreshLabel(Label.Plain label) {
        for (LabelsItemListBindingModel m : models)
            if (m.getGroup().id.equals(label.group.id))
                m.refreshLabel(label);
    }

    public void editGroup(LabelGroup.Plain group) {
        for (LabelGroup.Plain g : dataGroups)
            if (g.id.equals(group.id)) {
                g.color = group.color;
                g.name = group.name;
            }

        for (LabelsItemListBindingModel m : models)
            if (m.getGroup().id.equals(group.id)) {
                m.setGroup(group);
                notifyItemChanged(models.indexOf(m));
            }
    }

    private void scrollTo(int position) {
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context.get()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        smoothScroller.setTargetPosition(position);
        layoutManager.startSmoothScroll(smoothScroller);
    }

    public void setSelectedSort(boolean selectedSort) {
        for (LabelsItemListBindingModel model : models)
            model.setSelectedSort(selectedSort);
    }

    public void setNameSort(boolean nameSort) {
        for (LabelsItemListBindingModel model : models)
            model.setNameSort(nameSort);
    }

    public void setFilter(String filter) {
        for (LabelsItemListBindingModel model : models)
            model.setFilter(filter);
    }

    private void createModels() {
        models.clear();
        Map<String,List<Label.Plain>> labelsMap = new HashMap<>();
        Map<String,LabelGroup.Plain> groupsMap = new HashMap<>();
        for (LabelGroup.Plain lp : filtered_dataGroups)
            groupsMap.put(lp.id, lp);
        for (int i = 0; i < filtered_dataLabels.size(); i++) {

            Label.Plain label = filtered_dataLabels.get(i);

            List<Label.Plain> l;
            if (labelsMap.containsKey(label.group.id)) {
                l = labelsMap.get(label.group.id);
            } else {
                l = new ArrayList<>();
                labelsMap.put(label.group.id,l);
            }
            l.add(label);
        }

        for (String groupId : LabelGroup.Plain.sort(groupsMap.keySet(), labelsMap)) {
            LabelsItemListBindingModel model = new LabelsItemListBindingModel(context.get(), this, labelsMap.get(groupId), groupsMap.get(groupId), showGroups);
            models.add(model);
        }
    }

    public void refresh(Block.Plain block) {
/*
        int posInsert = 0;
        for (Block.Plain b : this.filtered_data) {
            if (b.id.equals(block.id)) {
                int pos = filtered_data.indexOf(b);
                data.remove(b);
                filtered_data.remove(b);
                models.remove(pos);
                data.add(block);
                filtered_data.add(pos,block);
                models.add(pos,new BlockItemListBindingModel(context.get(),this, block, defaultImage));
                notifyItemChanged(pos);
                return;
            }
*/
/*            if (record.mainVariant.primaryShop.price > record.mainVariant.primaryShop.price)
                posInsert++;*//*

        }
        data.add(block);
        filtered_data.add(posInsert, block);
        models.add(posInsert,new BlockItemListBindingModel(context.get(),this, block, defaultImage));
        notifyItemInserted(posInsert);
*/
    }

    @Override
    public void itemGroupDelete(String groupId) {
        DeleteDialogHelper.show(context.get(), "Are you sure to delete group?", () -> {
            int pos;
            LabelGroup.Plain g = null;
            for (pos = 0; pos < models.size(); pos++)
                if (models.get(pos).getGroup().id.equals(groupId)) {
                    g = models.get(pos).getGroup();
                    break;
                }
            dataGroups.remove(g);
            filtered_dataGroups.remove(g);
            models.remove(pos);
            ((ItemActionsLabel) context.get()).itemGroupDelete(groupId);
            notifyItemRemoved(pos);
        });
    }

    @Override
    public void itemGroupEdit(String groupId) {
        ((ItemActionsLabel) context.get()).itemGroupEdit(groupId);
    }

    @Override
    public void itemLabelAdd(String groupId) {
        ((ItemActionsLabel) context.get()).itemLabelAdd(groupId);
    }
}
