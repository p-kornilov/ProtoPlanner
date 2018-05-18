package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;
import android.widget.Spinner;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.Utils.PriceFormatter;

import java.lang.ref.WeakReference;
import java.util.List;

public class VariantEditBindingModel extends BaseObservable {
    private String price;
    private String count;
    private String name;
    private Currency.Plain currency;
    private Measure.Plain measure;
    private List<Currency.Plain> currencyList;
    private List<Measure.Plain> measureList;
    private int currencyCursor = 0;
    private int measureCursor = 0;

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
    public List<Currency.Plain> getVariantEditCurrencyList() {
        return currencyList;
    }

    @Bindable
    public void setCurrencyCursor(int cursor) {
        currency = currencyList.get(cursor);
        currencyCursor = cursor;
    }

    @Bindable
    public int getCurrencyCursor() {
        return currencyCursor;
    }

    @Bindable
    public int getMeasureCursor() {
        return measureCursor;
    }

    public void setVariantEditCurrencyList(List<Currency.Plain> currencyList) {
        this.currencyList = currencyList;
        notifyPropertyChanged(BR.variantEditCurrencyList);
    }

    @Bindable
    public Measure.Plain getVariantEditMeasure() {
        return measure;
    }

    @Bindable
    public List<Measure.Plain> getVariantEditMeasureList() {
        return measureList;
    }

    @Bindable
    public void setMeasureCursor(int cursor) {
        measure = measureList.get(cursor);
        measureCursor = cursor;
    }

    public void setVariantEditMeasureList(List<Measure.Plain> measureList) {
        this.measureList = measureList;
        checkMeasureList();
        notifyPropertyChanged(BR.variantEditMeasureList);
    }

    private void checkMeasureList() {
        if (measure != null && measureList != null) {
            for (Measure.Plain m : measureList)
                if (m.hash == measure.hash)
                    return;
            measureList.add(measure);
            measureList = Measure.Plain.sort(context.get(), measureList);
        }
    }

    public void setVariant(Variant.Plain variant) {
        this.name = variant.title;
        this.price = String.valueOf(variant.price);
        this.count = String.valueOf(variant.count);
        this.currency = variant.currency;
        this.measure = variant.measure;
        checkMeasureList();

        notifyPropertyChanged(BR.variantEditName);
        notifyPropertyChanged(BR.variantEditCount);
        notifyPropertyChanged(BR.variantEditPrice);
        notifyPropertyChanged(BR.variantEditCurrency);

        //...
    }

}
