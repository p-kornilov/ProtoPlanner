package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.Utils.PriceFormatter;

import java.lang.ref.WeakReference;
import java.util.List;

public class VariantItemBindingModel extends BaseObservable {
    private Variant.Plain variant;
    private String price;
    private String count;

    private WeakReference<Runnable> onEditClick;
    private WeakReference<Context> context;

    public VariantItemBindingModel( Context context) {
        this.context = new WeakReference<>(context);
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
        return PriceFormatter.createValue(variant.currency, variant.price * variant.count);
    }

    @Bindable
    public String getVariantPrice() {
        return String.valueOf(variant.price);
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
        return PriceFormatter.createPrice(context.get(), variant.currency, variant.price, variant.measure);
    }

    @Bindable
    public String getVariantCountDecor() {
        return PriceFormatter.createCount(context.get(), variant.count, variant.measure);
    }


    public void setVariant(Variant.Plain variant) {
        this.variant = variant;
        this.price = String.valueOf(variant.price);
        this.count = String.valueOf(variant.count);
        notifyPropertyChanged(BR.variantName);
        notifyPropertyChanged(BR.variantCountDecor);
        notifyPropertyChanged(BR.variantPriceDecor);
        notifyPropertyChanged(BR.variantCount);
        notifyPropertyChanged(BR.variantPrice);
        notifyPropertyChanged(BR.variantValueDecor);

        //...
    }

    public void setOnEditClick(Runnable func) {
        this.onEditClick = new WeakReference<>(func);
    }

    public void onEditClick() {
        if (onEditClick != null && onEditClick.get() != null)
            onEditClick.get().run();
    }

}
