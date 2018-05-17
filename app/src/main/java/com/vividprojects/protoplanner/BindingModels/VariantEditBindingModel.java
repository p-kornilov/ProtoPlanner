package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.Utils.PriceFormatter;

import java.lang.ref.WeakReference;

public class VariantEditBindingModel extends BaseObservable {
    private String price;
    private String count;
    private String name;
    private Currency.Plain currency;
    private Measure.Plain measure;
    private Currency.Plain[] currencyList;

    private WeakReference<Context> context;

    public VariantEditBindingModel(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Bindable
    public String getVariantEditName(){
        return name;
    }

    @Bindable
    public void setVariantEditName(String name) {
        this.name = name;
    }

    @Bindable
    public String getVariantEditPrice() {
        return price;
    }

    @Bindable
    public String getVariantEditCount() {
        return count;
    }

    @Bindable
    public void setVariantEditPrice(String price) {
        this.price = price;
    }

    @Bindable
    public void setVariantEditCount(String count) {
        this.count = count;
    }

    @Bindable
    public Currency.Plain getVariantEditCurrency() {
        return currency;
    }

    @Bindable
    public Currency.Plain[] getVariantEditCurrencyList() {
        return currencyList;
    }

    public void setVariantEditCurrencyList(Currency.Plain[] currencyList) {
        this.currencyList = currencyList;
        notifyPropertyChanged(BR.variantEditCurrencyList);
    }


    public void setVariant(Variant.Plain variant) {
        this.name = variant.title;
        this.price = String.valueOf(variant.price);
        this.count = String.valueOf(variant.count);
        this.currency = variant.currency;
        this.measure = variant.measure;

        notifyPropertyChanged(BR.variantEditName);
        notifyPropertyChanged(BR.variantEditCount);
        notifyPropertyChanged(BR.variantEditPrice);
        notifyPropertyChanged(BR.variantEditCurrency);

        //...
    }

}
