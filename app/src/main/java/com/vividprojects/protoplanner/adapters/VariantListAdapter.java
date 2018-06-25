package com.vividprojects.protoplanner.adapters;

import android.content.Context;

import com.vividprojects.protoplanner.bindingmodels.VariantItemListBindingModel;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.DeleteDialogHelper;
import com.vividprojects.protoplanner.utils.ItemActionsVariant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class VariantListAdapter extends DataBindingAdapter implements ItemActionsVariant {
    private List<Variant.Plain> data = new ArrayList<>();
    private List<Variant.Plain> filtered_data = new ArrayList<>();
    private List<VariantItemListBindingModel> models = new ArrayList<>();
    private String defaultImage;

    private WeakReference<Context> context;
    private ItemActionsVariant master;

    public VariantListAdapter(Context context, String defaultImage) {
        this.context = new WeakReference<>(context);
        this.defaultImage = defaultImage;
    }

    public void setMaster(ItemActionsVariant master) {
        this.master = master;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.variant_item;
    }

    @Override
    public int getItemCount() {
        return filtered_data != null ? filtered_data.size() : 0;
    }

    @Override
    public Object getObjForPosition(int position) {
        return models.get(position);
    }

    public void setData(List<Variant.Plain> data) {
        this.data.clear();
        this.data.addAll(data);

        this.filtered_data = Variant.Plain.sort(this.data);
        createModels();
    }

    private void createModels() {
        models.clear();
        for (int i = 0; i < filtered_data.size(); i++) {
            VariantItemListBindingModel model = new VariantItemListBindingModel(context.get(),this, filtered_data.get(i), defaultImage);
            models.add(model);
        }
    }

    @Override
    public void variantDelete(String id) {
        DeleteDialogHelper.show(context.get(), "Are you sure?", () -> {
            int pos;
            Variant.Plain v = null;
            for (pos = 0; pos < filtered_data.size(); pos++)
                if (filtered_data.get(pos).id.equals(id)) {
                    v = filtered_data.get(pos);
                    break;
                }
            data.remove(v);
            filtered_data.remove(v);
            models.remove(pos);
            master.variantDelete(id);
            notifyItemRemoved(pos);
        });
    }

    @Override
    public void variantOpen(String id) {
        master.variantOpen(id);
    }

    @Override
    public void variantSetBasic(String id) {
        master.variantSetBasic(id);
/*        int dpos = 0;
        VariantInShop.Plain ss = null;
        for (VariantInShop.Plain s : filtered_data) {
            if (s.id.equals(id)) {
                ss = s;
                break;
            }
        }

        ss.basicVariant = true;
        filtered_data.get(dpos).basicVariant = false;
        int pos = filtered_data.indexOf(ss);
        itemMove(pos,dpos);

        for (int i = dpos+2; i < filtered_data.size(); i++)
                if (filtered_data.get(i).price >= filtered_data.get(dpos+1).price) {
                    itemMove(dpos+1,i-1);
                    break;
                }

        master.itemShopPrimary(id);*/
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

    public void refresh(Variant.Plain variant) {
        int posInsert = 0;
        for (Variant.Plain v : this.filtered_data) {
            if (v.id.equals(variant.id)) {
                int pos = filtered_data.indexOf(v);
                data.remove(v);
                filtered_data.remove(v);
                models.remove(pos);
                data.add(v);
                filtered_data.add(pos,variant);
                models.add(pos,new VariantItemListBindingModel(context.get(),this, variant, defaultImage));
                notifyItemChanged(pos);
                return;
            }
            if (variant.primaryShop.price > v.primaryShop.price)
                posInsert++;
        }
        data.add(variant);
        filtered_data.add(posInsert,variant);
        models.add(posInsert,new VariantItemListBindingModel(context.get(),this, variant, defaultImage));
        notifyItemInserted(posInsert);
    }

}
