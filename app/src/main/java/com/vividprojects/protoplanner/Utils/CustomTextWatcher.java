package com.vividprojects.protoplanner.Utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by p.kornilov on 14.03.2018.
 */

public class CustomTextWatcher extends TextWatcher {
    private CharSequence text;

    public void setRunnable()

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int ff = 2;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int iii = 1;
        if (!charSequence.equals(text)) {
            text = charSequence;
            symbolEditFinish(currency_symbol.getText().toString());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        int i = 1;
    }
}
