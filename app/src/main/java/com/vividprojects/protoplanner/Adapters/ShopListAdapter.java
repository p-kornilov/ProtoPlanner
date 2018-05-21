package com.vividprojects.protoplanner.Adapters;

import com.vividprojects.protoplanner.BindingModels.ShopItemListBindingModel;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.R;

import java.util.ArrayList;
import java.util.List;

public class ShopListAdapter extends DataBindingAdapter {
    private List<VariantInShop.Plain> data;
    private List<VariantInShop.Plain> filtered_data = new ArrayList<>();
    private List<ShopItemListBindingModel> models = new ArrayList<>();

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


}
