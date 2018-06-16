package com.vividprojects.protoplanner.Adapters;

import android.content.Context;

import com.vividprojects.protoplanner.BindingModels.VariantItemListBindingModel;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.ItemActionsVariant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class VariantListAdapter extends DataBindingAdapter implements ItemActionsVariant {
    private List<Variant.Plain> data = new ArrayList<>();
    private List<Variant.Plain> filtered_data = new ArrayList<>();
    private List<VariantItemListBindingModel> models = new ArrayList<>();

    private WeakReference<Context> context;
    private ItemActionsVariant master;

    public VariantListAdapter(Context context) {
        this.context = new WeakReference<>(context);
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
        if (filtered_data != null)
            return filtered_data.size();
        else
            return 0;
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

            Variant.Plain v = filtered_data.get(i);
            VariantItemListBindingModel model = new VariantItemListBindingModel(context.get(),this, v);

            models.add(model);
        }
    }

    @Override
    public void itemVariantDelete(String id) {
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
    public void itemVariantOpen(String id) {
        master.itemVariantOpen(id);
    }

    @Override
    public void itemVariantBasic(String id) {
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
