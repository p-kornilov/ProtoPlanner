package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vividprojects.protoplanner.Adapters.HorizontalImagesListAdapter;
import com.vividprojects.protoplanner.Adapters.ShopListAdapter;
import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.ItemActionsShop;
import com.vividprojects.protoplanner.Utils.ItemActionsVariant;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Utils.RunnableParam;

import java.lang.ref.WeakReference;

public class VariantItemListBindingModel extends BaseObservable {
    private Variant.Plain variant;
    private String price;
    private String count;
    private String name;

    private WeakReference<Context> context;
    private ItemActionsVariant listAdapter;

    public VariantItemListBindingModel() {
    }

    public VariantItemListBindingModel(Context context, ItemActionsVariant listAdapter, Variant.Plain variant) {
        this.variant = variant;
        this.context = new WeakReference<>(context);
        this.listAdapter = listAdapter;
        notifyPropertyChanged(BR.variantName);
        notifyPropertyChanged(BR.variantCountDecor);
        notifyPropertyChanged(BR.variantPriceDecor);
        notifyPropertyChanged(BR.variantCount);
        notifyPropertyChanged(BR.variantPrice);
        notifyPropertyChanged(BR.variantValueDecor);
    }

    @Bindable
    public String getVariantName(){
        return variant.title;
    }

    @Bindable
    public void setVariantName(String name) {
        variant.title = name;
    }

    @Bindable
    public String getVariantValueDecor() {
        return PriceFormatter.createValue(variant.primaryShop.currency, variant.primaryShop.price * variant.count);
    }

    @Bindable
    public String getVariantPrice() {
        return String.valueOf(variant.primaryShop.price);
    }

    @Bindable
    public String getVariantCount() {
        return String.valueOf(variant.count);
    }

    @Bindable
    public void setVariantPrice(String price) {
        this.price = price;
    }

    @Bindable
    public void setVariantCount(String count) {
        this.count = count;
    }

    @Bindable
    public String getVariantPriceDecor() {
        return PriceFormatter.createPrice(context.get(), variant.primaryShop.currency, variant.primaryShop.price, variant.measure);
    }

    @Bindable
    public String getVariantCountDecor() {
        return PriceFormatter.createCount(context.get(), variant.count, variant.measure);
    }

    public String getVariantId() {
        return variant.id;
    }


}
