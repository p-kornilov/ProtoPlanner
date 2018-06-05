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
    private String count;
    private Variant.Plain variant;
    private List<Measure.Plain> measureList;
    private int measureCursor = 0;
    private boolean countError = false;

    private WeakReference<Context> context;
    private WeakReference<RunnableParam<Integer>> enableCheck;

    public void setEnableCheck(RunnableParam<Integer> f) {
        this.enableCheck = new WeakReference<>(f);
    }

    public void enableCheck() {
        if (enableCheck == null || enableCheck.get() == null)
            return;
        if (countError)
            enableCheck.get().run(1);
        else
            enableCheck.get().run(0);

    }

    public double getCountNum() {
        return variant.count;
    }

    public VariantEditBindingModel(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Bindable
    public boolean getCountError() {
        return countError;
    }

    @Bindable
    public String getVariantEditName(){
        return variant.title;
    }

    @Bindable
    public void setVariantEditName(String name) {
        this.variant.title = name;
    }

    @Bindable
    public String getVariantEditCount() {
        return count;
    }

    @Bindable
    public void setVariantEditCount(String count) {
        try {
            variant.count = Double.parseDouble(count);
            countError = false;
        } catch (NumberFormatException e) {
            countError = true;
        }
        this.count = count;
        enableCheck();
        notifyPropertyChanged(BR.countError);
    }

    @Bindable
    public int getMeasureCursor() {
        return measureCursor;
    }

    @Bindable
    public Measure.Plain getVariantEditMeasure() {
        return variant.measure;
    }

    @Bindable
    public List<Measure.Plain> getVariantEditMeasureList() {
        return measureList;
    }

    @Bindable
    public void setMeasureCursor(int cursor) {
        variant.measure = measureList.get(cursor);
        measureCursor = cursor;
    }

    public void setVariantEditMeasureList(List<Measure.Plain> measureList) {
        this.measureList = measureList;
        checkMeasureList();
        notifyPropertyChanged(BR.variantEditMeasureList);
    }

    private void checkMeasureList() {
        if (variant.measure != null && measureList != null) {
            for (Measure.Plain m : measureList)
                if (m.hash == variant.measure.hash)
                    return;
            measureList.add(variant.measure);
            measureList = Measure.Plain.sort(context.get(), measureList);
        }
    }

    public String getId() {
        return variant.id;
    }

    public void setVariant(Variant.Plain variant) {
        this.variant = variant;
        this.count = String.valueOf(variant.count);
        checkMeasureList();

        notifyPropertyChanged(BR.variantEditName);
        notifyPropertyChanged(BR.variantEditCount);

        //...
    }

}
