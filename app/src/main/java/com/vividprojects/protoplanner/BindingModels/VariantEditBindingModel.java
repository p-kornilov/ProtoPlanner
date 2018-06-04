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
import com.vividprojects.protoplanner.Utils.RunnableParam;

import java.lang.ref.WeakReference;
import java.util.List;

public class VariantEditBindingModel extends BaseObservable {
    private String id;
    private String price;
    private String count;
    private String name;
    private double priceNum;
    private double countNum;
    private Currency.Plain currency;
    private Measure.Plain measure;
    private List<Currency.Plain> currencyList;
    private List<Measure.Plain> measureList;
    private int currencyCursor = 0;
    private int measureCursor = 0;
    private boolean priceError = false;
    private boolean countError = false;

    private WeakReference<Context> context;
    private WeakReference<RunnableParam<Integer>> enableCheck;

    public void setEnableCheck(RunnableParam<Integer> f) {
        this.enableCheck = new WeakReference<>(f);
    }

    public void enableCheck() {
        if (enableCheck == null || enableCheck.get() == null)
            return;
        if (priceError || countError)
            enableCheck.get().run(1);
        else
            enableCheck.get().run(0);

    }

    public double getPriceNum() {
        return priceNum;
    }

    public double getCountNum() {
        return countNum;
    }

    public VariantEditBindingModel(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Bindable
    public boolean getPriceError() {
        return priceError;
    }

    @Bindable
    public boolean getCountError() {
        return countError;
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
        try {
            priceNum = Double.parseDouble(price);
            priceError = false;
        } catch (NumberFormatException e) {
            priceError = true;
        }
        this.price = price;
        enableCheck();
        notifyPropertyChanged(BR.priceError);
    }

    @Bindable
    public void setVariantEditCount(String count) {
        try {
            countNum = Double.parseDouble(count);
            countError = false;
        } catch (NumberFormatException e) {
            countError = true;
        }
        this.count = count;
        enableCheck();
        notifyPropertyChanged(BR.countError);
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

    public String getId() {
        return id;
    }

    public void setVariant(Variant.Plain variant) {
        this.id = variant.id;
        this.name = variant.title;
        this.price = String.valueOf(variant.primaryShop.price);
        this.count = String.valueOf(variant.count);
        this.priceNum = variant.primaryShop.price;
        this.countNum = variant.count;
        this.currency = variant.primaryShop.currency;
        this.measure = variant.measure;
        checkMeasureList();

        notifyPropertyChanged(BR.variantEditName);
        notifyPropertyChanged(BR.variantEditCount);
        notifyPropertyChanged(BR.variantEditPrice);
        notifyPropertyChanged(BR.variantEditCurrency);

        //...
    }

}
