package com.vividprojects.protoplanner.Adapters;

import android.content.Context;

import com.vividprojects.protoplanner.BindingModels.ShopItemListBindingModel;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends DataBindingAdapter {
    private List<VariantInShop.Plain> data;
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
        this.filtered_data = new ArrayList<>(data);
        createModels();
    }

    private void createModels() {
        models.clear();
        for (int i = 0; i < filtered_data.size(); i++) {

            ShopItemListBindingModel model = new ShopItemListBindingModel(context.get(),null);
            VariantInShop.Plain v = filtered_data.get(i);
            model.setShop(v);

            models.add(model);
        }
    }
}
