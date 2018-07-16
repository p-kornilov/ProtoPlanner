package com.vividprojects.protoplanner.adapters;

import android.content.Context;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.bindingmodels.RecordItemListBindingModel;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.VariantInShop;
import com.vividprojects.protoplanner.utils.DeleteDialogHelper;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RecordListAdapter extends DataBindingAdapter implements ItemActionsRecord {
    private List<Record.Plain> data = new ArrayList<>();
    private List<Record.Plain> filtered_data = new ArrayList<>();
    private List<RecordItemListBindingModel> models = new ArrayList<>();

    private WeakReference<Context> context;
    private ItemActionsRecord master;
    private String defaultImage;
    private String filter;

    public RecordListAdapter(Context context, String defaultImage) {
        this.context = new WeakReference<>(context);
        this.defaultImage = defaultImage;
    }

    public void setMaster(ItemActionsRecord master) {
        this.master = master;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.record_item;
    }

    @Override
    public int getItemCount() {
        return filtered_data != null ? filtered_data.size() : 0;
    }

    @Override
    public Object getObjForPosition(int position) {
        return models.get(position);
    }

    public void setData(List<Record.Plain> data) {
        this.data.clear();
        this.data.addAll(data);

        this.filtered_data = data;// VariantInShop.Plain.sort(this.data);
        createModels();
        //notifyDataSetChanged();
    }

    public void setFilter(String filter) {
        String fLower;
        if (filter != null)
            fLower = filter.toLowerCase();
        else
            return;
        if (!fLower.equals(this.filter)) {
            this.filter = fLower;
            filtered_data = new ArrayList<>();
            for (Record.Plain r : data) {
                if (r.name.toLowerCase().contains(filter) || (r.mainVariant != null && r.mainVariant.title.toLowerCase().contains(filter)))
                    filtered_data.add(r);
            }
            createModels();
            notifyDataSetChanged();
        } else
            filtered_data = data;

    }

    private void createModels() {
        models.clear();
        for (int i = 0; i < filtered_data.size(); i++) {

            Record.Plain v = filtered_data.get(i);
            RecordItemListBindingModel model = new RecordItemListBindingModel(context.get(),this, v, defaultImage);

            models.add(model);
        }
    }

    @Override
    public void itemRecordDelete(String id) {
        DeleteDialogHelper.show(context.get(), "Are you sure?", () -> {
            int pos;
            Record.Plain r = null;
            for (pos = 0; pos < filtered_data.size(); pos++)
                if (filtered_data.get(pos).id.equals(id)) {
                    r = filtered_data.get(pos);
                    break;
                }
            data.remove(r);
            filtered_data.remove(r);
            models.remove(pos);
            master.itemRecordDelete(id);
            notifyItemRemoved(pos);
        });
    }

    @Override
    public void itemRecordEdit(String id) {
        master.itemRecordEdit(id);
    }

    @Override
    public void itemRecordAttachToBlock(String item) {

    }

    private void itemMove(int from, int to) {
        if (from > getItemCount() || to > getItemCount())
            return;

        filtered_data.add(to,filtered_data.remove(from));
        models.add(to,models.remove(from));

        notifyItemMoved(from,to);
        notifyItemChanged(to);
        notifyItemChanged(from);
    }

    public void refresh(Record.Plain record) {
        int posInsert = 0;
        for (Record.Plain r : this.filtered_data) {
            if (r.id.equals(record.id)) {
                int pos = filtered_data.indexOf(r);
                data.remove(r);
                filtered_data.remove(r);
                models.remove(pos);
                data.add(record);
                filtered_data.add(pos,record);
                models.add(pos,new RecordItemListBindingModel(context.get(),this, record, defaultImage));
                notifyItemChanged(pos);
                return;
            }
/*            if (record.mainVariant.primaryShop.price > record.mainVariant.primaryShop.price)
                posInsert++;*/
        }
        data.add(record);
        filtered_data.add(posInsert, record);
        models.add(posInsert,new RecordItemListBindingModel(context.get(),this, record, defaultImage));
        notifyItemInserted(posInsert);
    }

}
