package com.vividprojects.protoplanner.Utils;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;

import com.vividprojects.protoplanner.BR;

import java.util.Objects;

/**
 * Created by p.kornilov on 16.03.2018.
 */

public class SymbolChecker extends BaseObservable {
    private String symbol;
    private boolean status;

    public TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String charSequenceS = charSequence.toString();

            if (symbol == null)
                symbol = charSequenceS;
            if (!charSequenceS.equals(symbol)) {
                symbol = charSequenceS;
                status = symbolCheck(charSequenceS);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    @Bindable
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
        notifyPropertyChanged(BR.symbol);
    }

    @Bindable
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public TextWatcher getWatcher() {
        return watcher;
    }

    private boolean symbolCheck(String s) {
        try {
            symbol = PriceFormatter.collapseUnicodes(s);
        } catch (TextInputError error) {
            return false;
        }
        //forceRippleAnimation(currency_pattern);
        return true;
    }

};