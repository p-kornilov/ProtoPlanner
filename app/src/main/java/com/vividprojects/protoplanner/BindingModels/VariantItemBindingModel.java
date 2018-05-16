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
    public String getVariantValue() {
        return PriceFormatter.createValue(variant.currency, variant.price * variant.count);
    }

    @Bindable
    public String getVariantPrice() {
        return PriceFormatter.createPrice(context.get(), variant.currency, variant.price, variant.measure);
    }

    @Bindable
    public String getVariantCount() {
        return PriceFormatter.createCount(context.get(), variant.count, variant.measure);
    }


    public void setVariant(Variant.Plain variant) {
        this.variant = variant;
        notifyPropertyChanged(BR.variantName);
        notifyPropertyChanged(BR.variantCount);
        notifyPropertyChanged(BR.variantPrice);
        notifyPropertyChanged(BR.variantValue);

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
