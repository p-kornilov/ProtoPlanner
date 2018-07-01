package com.vividprojects.protoplanner.adapters;

import android.content.Context;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.bindingmodels.RecordItemListBindingModel;
import com.vividprojects.protoplanner.bindingmodels.ShopItemListBindingModel;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.VariantInShop;
import com.vividprojects.protoplanner.utils.DeleteDialogHelper;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.utils.ItemActionsShop;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RecordListAdapter_ extends DataBindingAdapter implements ItemActionsRecord {
    private List<Record.Plain> data = new ArrayList<>();
    private List<Record.Plain> filtered_data = new ArrayList<>();
    private List<RecordItemListBindingModel> models = new ArrayList<>();

    private WeakReference<Context> context;
    private ItemActionsRecord master;
    private String defaultImage;

    public RecordListAdapter_(Context context, String defaultImage) {
        this.context = new WeakReference<>(context);
        this.defaultImage = defaultImage;
    }

    public void setMaster(ItemActionsRecord master) {
        this.master = master;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.record_item_;
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
/*        DeleteDialogHelper.show(context.get(), "Are you sure?", () -> {
            int pos;
            VariantInShop.Plain s = null;
            for (pos = 0; pos < filtered_data.size(); pos++)
                if (filtered_data.get(pos).id.equals(id)) {
                    s = filtered_data.get(pos);
                    break;
                }
            data.remove(s);
            filtered_data.remove(s);
            models.remove(pos);
            master.itemShopDelete(id);
            notifyItemRemoved(pos);
        });*/
    }

    @Override
    public void itemRecordEdit(String id) {
    //    master.itemShopEdit(id);
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

    public void refresh(VariantInShop.Plain shop) {
/*        int posInsert = 0;
        for (VariantInShop.Plain m : this.filtered_data) {
            if (m.id.equals(shop.id)) {
                int pos = filtered_data.indexOf(m);
                data.remove(m);
                filtered_data.remove(m);
                models.remove(pos);
                data.add(shop);
                filtered_data.add(pos,shop);
                models.add(pos,new ShopItemListBindingModel(context.get(),this, shop));
                notifyItemChanged(pos);
                return;
            }
            if (shop.price > m.price)
                posInsert++;
        }
        data.add(shop);
        filtered_data.add(posInsert,shop);
        models.add(posInsert,new ShopItemListBindingModel(context.get(),this, shop));
        notifyItemInserted(posInsert);*/
    }

}
