package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Measure_;
import com.vividprojects.protoplanner.R;


/**
 * Created by Smile on 18.03.2018.
 */

public class MeasureItemListBindingModel extends BaseObservable {
    private Drawable background;

    private Measure_.Plain measure;

    private Context context;

    public MeasureItemListBindingModel(Context context) {
        measure = (new Measure_()).getPlain();
        this.context = context;
    }

    public void setMeasure(Measure_.Plain measure) {
        this.measure = measure;
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.symbol);
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }


    @Bindable
    public Drawable getBackground() {
        return background;
    }

    @Bindable
    public String getSymbol() {
        return measure.symbol != null ? measure.symbol : context.getResources().getString(measure.symbolId);
    }

    @Bindable
    public String getName() {
        return measure.name != null ? measure.name : context.getResources().getString(measure.nameId);
    }

    @Bindable
    public int getMeasure() {
        return measure.measure;
    }

    @BindingAdapter("bind:srcVector")
    public static void setSrcVector(ImageView view, int measure) {
        int image = R.drawable.measure_numeric;
        switch (measure) {
            case Measure_.MEASURE_UNIT:
                image = R.drawable.measure_numeric;
                break;
            case Measure_.MEASURE_MASS:
                image = R.drawable.measure_mass;
                break;
            case Measure_.MEASURE_LENGTH:
                image = R.drawable.measure_length;
                break;
            case Measure_.MEASURE_SQUARE:
                image = R.drawable.measure_square;
                break;
            case Measure_.MEASURE_VOLUME:
                image = R.drawable.measure_volume;
                break;
            case Measure_.MEASURE_LIQUIDDRY:
                image = R.drawable.measure_ld;
        }
        view.setImageResource(image);
    }

    public void onMClicked() {
        String n = "";
    }
};