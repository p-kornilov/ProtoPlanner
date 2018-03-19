package com.vividprojects.protoplanner.BindingModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Utils.TextInputError;

/**
 * Created by Smile on 18.03.2018.
 */

/*@BindingMethods({
        @BindingMethod(type = "Integer",
                attribute = "android:selectedItemPosition",
                method = "setSelection"),
})*/

public class CurrencyItemBindingModel extends BaseObservable {
    private String symbol;
    private boolean status = true;
    private int pattern;

    public void onTextChanged(Spinner spinner) {

        Drawable background = spinner.getBackground();
        if (Build.VERSION.SDK_INT >= 21 && background instanceof RippleDrawable) {
            final RippleDrawable rippleDrawable = (RippleDrawable) background;
            rippleDrawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rippleDrawable.setState(new int[]{});
                }
            }, 100);
        }
    }

    @Bindable
    public String getSymbol() {
        return symbol;
    }

    @Bindable
    public void setSymbol(String symbol) {
        this.symbol = symbol;
        notifyPropertyChanged(BR.symbol);
        status = symbolCheck();
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public boolean getStatus() {
        return status;
    }

    @Bindable
    public int getPattern() {
        return pattern;
    }

    @Bindable
    public void setPattern(int pattern) {
        this.pattern = pattern;
        notifyPropertyChanged(BR.pattern);
    }

    private boolean symbolCheck() {
        try {
            PriceFormatter.collapseUnicodes(symbol);
        } catch (TextInputError error) {
            return false;
        }
        return true;
    }

    @BindingAdapter({"app:adapterItem","app:adapterDropItem","app:textViewId"})
    public static void bindAdapter(Spinner spinner, int adapterItem, int adapterDropItem, int textViewId) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(spinner.getContext(), adapterItem, textViewId, PriceFormatter.createListValue("T", 100.00));
        spinnerAdapter.setDropDownViewResource(adapterDropItem);
        int p = spinner.getSelectedItemPosition();
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(p);
    }

};