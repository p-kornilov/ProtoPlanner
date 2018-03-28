package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.vividprojects.protoplanner.Adapters.ListOutline;
import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure_;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Utils.TextInputError;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Smile on 18.03.2018.
 */

public class MeasureItemListBindingModel extends BaseObservable {
    private String name;
    private String symbol;

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

/*
    public Currency.Plain getCurrency() {
        return currency;
    }
*/

    @Bindable
    public String getSymbol() {
        return measure.symbol != null ? measure.symbol : context.getResources().getString(measure.symbolId);
    }

    @Bindable
    public String getName() {
        return measure.name != null ? measure.name : context.getResources().getString(measure.nameId);
    }

    @BindingAdapter({"app:mybackground"})
    public static void bindAdapter(View view, int mybackground) {
/*        view.setBackground(ContextCompat.getDrawable(view.getContext(),mybackground));

        ViewCompat.setElevation(view,view.getResources().getDimension(R.dimen.cardElevation));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            view.setOutlineProvider(new ListOutline());*/
    }
};