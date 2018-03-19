package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
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

import java.util.List;

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
    private String collapsedSymbol;
    private boolean status = true;
    private int pattern;
    private List<String> pattern_entries;
    private String currencyCode;
    private int currencyNameId;
    private String currencyCustomName;

    private Context context;

    public CurrencyItemBindingModel(Context context) {
        this.context = context;
    }

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

        if (status)
            setPatternEntries(PriceFormatter.createListValue(collapsedSymbol, 100.00));
    }

    @Bindable
    public String getCurrencyCode() {
        return currencyCode;
    }

    @Bindable
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        notifyPropertyChanged(BR.currencyCode);
    }

    public void setCurrencyNameId(int currencyNameId) {
        this.currencyNameId = currencyNameId;
    }

    @Bindable
    public String getCurrencyName() {
        return currencyCustomName != null ? currencyCustomName : context.getResources().getString(currencyNameId);
    }

    @Bindable
    public void setCurrencyName(String name) {
        currencyCustomName = name;
        notifyPropertyChanged(BR.currencyName);
        notifyPropertyChanged(BR.currencyRateHint);
    }

    public void setCurrencyCustomName(String currencyCustomName) {
        this.currencyCustomName = currencyCustomName;
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

    @Bindable
    public void setPatternEntries(List<String> list) {
        pattern_entries = list;
        notifyPropertyChanged(BR.patternEntries);
    }

    @Bindable
    public List<String> getPatternEntries() {
        return pattern_entries;
    }

    @Bindable
    public String getCurrencyRateHint() {
        return currencyCustomName != null ? currencyCustomName + " (" + currencyCode + ")" : context.getResources().getString(currencyNameId)  + " (" + currencyCode + ")";
    }

    private boolean symbolCheck() {
        try {
            collapsedSymbol = PriceFormatter.collapseUnicodes(symbol);
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