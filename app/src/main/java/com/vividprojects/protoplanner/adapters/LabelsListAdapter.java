package com.vividprojects.protoplanner.adapters;

import android.content.Context;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.bindingmodels.LabelsItemListBindingModel;
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.LabelGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabelsListAdapter extends DataBindingAdapter {
    private List<Label.Plain> dataLabels = new ArrayList<>();
    private List<LabelGroup.Plain> dataGroups = new ArrayList<>();
    private List<Label.Plain> filtered_dataLabels = new ArrayList<>();
    private List<LabelGroup.Plain> filtered_dataGroups = new ArrayList<>();
    private List<LabelsItemListBindingModel> models = new ArrayList<>();

    private WeakReference<Context> context;
    private String filter;
    private boolean showGroups = false;

    public LabelsListAdapter(Context context, boolean showGroups) {
        this.context = new WeakReference<>(context);
        this.showGroups = showGroups;
    }

/*
    public void setMaster(ItemActionsBlock master) {
        this.master = master;
    }
*/

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
            LabelsItemListBindingModel model = new LabelsItemListBindingModel(context.get(), labelsMap.get(groupId), groupsMap.get(groupId), showGroups);
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

}
