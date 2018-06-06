package com.vividprojects.protoplanner.Adapters;

import android.content.Context;

import com.vividprojects.protoplanner.BindingModels.ShopItemListBindingModel;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends DataBindingAdapter {
    private List<VariantInShop.Plain> data = new ArrayList<>();
    private List<VariantInShop.Plain> filtered_data = new ArrayList<>();
    private List<ShopItemListBindingModel> models = new ArrayList<>();

    private WeakReference<Context> context;

    public ShopListAdapter(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.shop_item;
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
            ShopItemListBindingModel model = new ShopItemListBindingModel(context.get(),null, v);

            models.add(model);
        }
    }
}
