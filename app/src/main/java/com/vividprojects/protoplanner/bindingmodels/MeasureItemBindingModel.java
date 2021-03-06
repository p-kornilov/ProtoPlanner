package com.vividprojects.protoplanner.bindingmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.coredata.Measure;


/**
 * Created by Smile on 18.03.2018.
 */

public class MeasureItemBindingModel extends BaseObservable {
    private Measure.Plain measure;

    private Context context;

    public MeasureItemBindingModel(Context context) {
        measure = (new Measure()).getPlain();
        this.context = context;
    }

    public Measure.Plain getMeasure() {
        return measure;
    }

    public void setMeasure(Measure.Plain measure) {
        this.measure = measure;
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.symbol);
    }

    @Bindable
    public String getMeasureSymbol() {
        return measure.symbol != null ? measure.symbol : context.getResources().getString(measure.symbolId);
    }

    @Bindable
    public void setMeasureSymbol(String symbol) {
        measure.symbol = symbol;
        notifyPropertyChanged(BR.measureSymbol);
    }

    @Bindable
    public String getMeasureName() {
        return measure.name != null ? measure.name : context.getResources().getString(measure.nameId);
    }

    @Bindable
    public void setMeasureName(String name) {
        measure.name = name;
        notifyPropertyChanged(BR.measureName);
    }

    @Bindable
    public int getMeasureMeasure() {
        return measure.measure;
    }

    @Bindable
    public void setMeasureMeasure(int measure) {
        this.measure.measure = measure;
        notifyPropertyChanged(BR.measureMeasure);
    }

    @Bindable
    public boolean getMeasureFractional() {
        return measure.pattern == Measure.PATTERN_FRACTIONAL;
    }

    @Bindable
    public void setMeasureFractional(boolean b) {
        measure.pattern = b ? Measure.PATTERN_FRACTIONAL : Measure.PATTERN_ENTIRE;
    }

};