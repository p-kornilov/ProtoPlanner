package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.Utils.ItemActions;
import com.vividprojects.protoplanner.Utils.PriceFormatter;

public class ShopItemListBindingModel extends BaseObservable {
    private VariantInShop.Plain shop;

    private Context context;
    private ItemActions listAdapter;

    public ShopItemListBindingModel(Context context, ItemActions listAdapter) {
        shop = (new VariantInShop()).getPlain();
        this.context = context;
        this.listAdapter = listAdapter;
    }
/*    private String title;
    private String url;
    private String address;
    private String comment;
    private double price;*/

    @Bindable
    public String getShopTitle() {
        return shop.title;
    }

    @Bindable
    public String getShopURL() {
        return shop.url;
    }
    @Bindable
    public String getShopAddress() {
        return shop.address;
    }
    @Bindable
    public String getShopComment() {
        return shop.comment;
    }
    @Bindable
    public String getShopPrice() {
        return PriceFormatter.createPrice(context,c, shop.price, m);
    }
}
