package com.vividprojects.protoplanner.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.vividprojects.protoplanner.BindingModels.ShopItemListBindingModel;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.DeleteDialogHelper;
import com.vividprojects.protoplanner.Utils.ItemActionsShop;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends DataBindingAdapter implements ItemActionsShop {
    private List<VariantInShop.Plain> data = new ArrayList<>();
    private List<VariantInShop.Plain> filtered_data = new ArrayList<>();
    private List<ShopItemListBindingModel> models = new ArrayList<>();

    private WeakReference<Context> context;
    private ItemActionsShop master;

    public ShopListAdapter(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setMaster(ItemActionsShop master) {
        this.master = master;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.shop_item;
    }

    @Override
    public int getItemCount() {
        return filtered_data != null ? filtered_data.size() : 0;
    }

    @Override
    public Object getObjForPosition(int position) {
        return models.get(position);
    }

    public void setData(List<VariantInShop.Plain> data) {
        VariantInShop.Plain basicVariant = getBasicVariant(this.data);
        this.data.clear();
        this.data.addAll(data);
        if (basicVariant != null)
            this.data.add(basicVariant);

        this.filtered_data = VariantInShop.Plain.sort(this.data);
        createModels();
    }

    public void setData(List<VariantInShop.Plain> data, VariantInShop.Plain basicVariant) {
        this.data.clear();
        this.data.addAll(data);
        if (basicVariant != null)
            this.data.add(basicVariant);

        this.filtered_data = VariantInShop.Plain.sort(this.data);
        createModels();
    }

    public void setBasic(VariantInShop.Plain basicVariant) {
        VariantInShop.Plain basicVariantCurrent = getBasicVariant(this.data);
        if (basicVariantCurrent != null)
            this.data.remove(basicVariantCurrent);
        this.data.add(basicVariant);

        this.filtered_data = VariantInShop.Plain.sort(this.data);
        createModels();
    }

    private VariantInShop.Plain getBasicVariant(List<VariantInShop.Plain> list) {
        for (VariantInShop.Plain v : list) {
            if (v.basicVariant)
                return v;
        }
        return null;
    }

    private void createModels() {
        models.clear();
        for (int i = 0; i < filtered_data.size(); i++) {

            VariantInShop.Plain v = filtered_data.get(i);
            ShopItemListBindingModel model = new ShopItemListBindingModel(context.get(),this, v);

            models.add(model);
        }
    }

    @Override
    public void itemShopDelete(String id) {
        DeleteDialogHelper.show(context.get(), "Are you sure?", () -> {
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
        });
    }

    @Override
    public void itemShopEdit(String id) {
        master.itemShopEdit(id);
    }

    @Override
    public void itemShopPrimary(String id) {
        int dpos = 0;
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

        master.itemShopPrimary(id);
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
        int posInsert = 0;
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
        notifyItemInserted(posInsert);
    }

}
