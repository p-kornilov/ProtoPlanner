package com.vividprojects.protoplanner.bindingmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Spinner;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.utils.PriceFormatter;
import com.vividprojects.protoplanner.utils.TextInputError;

import java.text.DecimalFormat;

/**
 * Created by Smile on 18.03.2018.
 */

public class CurrencyItemBindingModel extends BaseObservable {
    private boolean status = true;
    private boolean checkCode = true;
    private String collapsedSymbol;
    private String[] pattern_entries;
    private boolean onCodeChange = false;
    private String imageDirectory;

    private Currency.Plain currency;
    private Currency.Plain base;

    private Context context;

    private Runnable onNewImage;

    public void setImageAction(Runnable action) {
        onNewImage = action;
    }

    public void onImageClick() {
        if (onNewImage != null)
            onNewImage.run();
    }

    public void setImage(String imageName) {
        this.currency.flag_file = imageName;
        this.currency.flag_id = 0;
        notifyPropertyChanged(BR.flagImage);
    }

    public CurrencyItemBindingModel(Context context, String imagesDirectory) {
        this.imageDirectory = imagesDirectory;
        currency = (new Currency()).getPlain();
        base = (new Currency()).getPlain();
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

    public void onCodeChanged(EditText editText) {
        if (!onCodeChange) {
            onCodeChange = true;
            int pos = editText.getSelectionStart();
            if (pos > 3) pos = 3;
            String text = editText.getText().toString();
            int maxLen = text.length() > 3 ? 3 : text.length();
            editText.setText(text.toUpperCase().substring(0,maxLen));
            editText.setSelection(pos);
        }
        onCodeChange = false;
    }

    public void setCurrency(Currency.Plain currency) {
        this.currency = currency;
        setPatternEntries(PriceFormatter.createListValue(currency.symbol, 100.00));
        notifyPropertyChanged(BR.currencyCode);
        notifyPropertyChanged(BR.currencyName);
        notifyPropertyChanged(BR.symbol);
        notifyPropertyChanged(BR.pattern);
       // notifyPropertyChanged(BR.patternEntries);
        notifyPropertyChanged(BR.exchangeRateS);
        notifyPropertyChanged(BR.autoUpdate);
        notifyPropertyChanged(BR.currencyRateHint);
        notifyPropertyChanged(BR.isBase);
    }

    public void setBase(Currency.Plain currency) {
        this.base = currency;
        notifyPropertyChanged(BR.baseNameHint);
    }

    public Currency.Plain getCurrency() {
        return currency;
    }

    public String getSymbol() {
        return currency.symbol;
    }

    @Bindable
    public void setSymbol(String symbol) {
        currency.symbol = symbol;
        notifyPropertyChanged(BR.symbol);
        status = symbolCheck();
        notifyPropertyChanged(BR.status);

        if (status)
            setPatternEntries(PriceFormatter.createListValue(collapsedSymbol, 100.00));
    }

    @Bindable
    public boolean getFlagVisible() {
        return currency.flag_id != 0 || currency.flag_file != null;
    }

    @Bindable
    public int getFlagResource() {
        return currency.flag_id;
    }

    @Bindable
    public String getFlagImage() {
        return currency.flag_file == null || imageDirectory == null  ? null : imageDirectory + currency.flag_file + ".jpg";
    }

    @Bindable
    public String getCurrencyCode() {
        return currency.iso_code_str;
    }

    @Bindable
    public void setCurrencyCode(String currencyCode) {
        currency.iso_code_str = currencyCode;
        checkCode = currencyCode.length() == 3;

       // notifyPropertyChanged(BR.currencyCode);
        notifyPropertyChanged(BR.currencyRateHint);
        notifyPropertyChanged(BR.checkCode);
    }

    @Bindable
    public boolean getCheckCode() {
        return checkCode;
    }

   /* public void setCurrencyNameId(int currencyNameId) {
        this.currencyNameId = currencyNameId;
    }*/

    @Bindable
    public String getCurrencyName() {
        if (currency.custom_name != null)
            return currency.custom_name;
        else if (currency.iso_name_id != 0)
            return context.getResources().getString(currency.iso_name_id);
        else
            return null;
        //return currency.custom_name != null ? currency.custom_name : context.getResources().getString(currency.iso_name_id);
    }

    @Bindable
    public void setCurrencyName(String name) {
        currency.custom_name = name;
        notifyPropertyChanged(BR.currencyName);
        notifyPropertyChanged(BR.currencyRateHint);
    }

    @Bindable
    public void setCurrencyCustomName(String currencyCustomName) {
        currency.custom_name = currencyCustomName;
    }

    @Bindable
    public boolean getStatus() {
        return status;
    }

    @Bindable
    public int getPattern() {
        return currency.pattern;
    }

    @Bindable
    public void setPattern(int pattern) {
        currency.pattern = pattern;
        notifyPropertyChanged(BR.pattern);
    }

    public void setPatternEntries(String[] list) {
        pattern_entries = list;
        notifyPropertyChanged(BR.patternEntries);
    }

    @Bindable
    public String[] getPatternEntries() {
        return pattern_entries;
    }

    @Bindable
    public String getCurrencyRateHint() {
        if (currency.custom_name != null)
            return currency.custom_name + " (" + currency.iso_code_str + ")";
        else if (currency.iso_name_id != 0)
            return context.getResources().getString(currency.iso_name_id)  + " (" + currency.iso_code_str + ")";
        else
            return null;
        //return currency.custom_name != null ? currency.custom_name + " (" + currency.iso_code_str + ")" : context.getResources().getString(currency.iso_name_id)  + " (" + currency.iso_code_str + ")";
    }

    @Bindable
    public String getExchangeRateS() {
        DecimalFormat formatter = new DecimalFormat("0.000000");
        return formatter.format(currency.exchange_rate);
    }

    @Bindable
    public void setExchangeRateS(String exchangeRate) {
        currency.exchange_rate = Float.parseFloat(exchangeRate.replace(',', '.'));
    }

    @Bindable
    public String getBaseRateS() {
        DecimalFormat formatter = new DecimalFormat("0.000000");
        return formatter.format(1.00);
    }

    @Bindable
    public boolean getAutoUpdate() {
        return currency.auto_update;
    }

    @Bindable
    public void setAutoUpdate(boolean autoUpdate) {
        currency.auto_update = autoUpdate;
        notifyPropertyChanged(BR.autoUpdate);
    }

    @Bindable
    public String getBaseNameHint() {
        if (base.custom_name != null)
            return base.custom_name + " (" + base.iso_code_str + ")";
        else if (base.iso_name_id != 0)
            return context.getResources().getString(base.iso_name_id)  + " (" + base.iso_code_str + ")";
        else
            return null;
        //return base.custom_name != null ? base.custom_name + " (" + base.iso_code_str + ")" : context.getResources().getString(base.iso_name_id)  + " (" + base.iso_code_str + ")";
    }

    @Bindable
    public boolean getIsBase() {
        return currency.isBase;
    }

    private boolean symbolCheck() {
        try {
            collapsedSymbol = PriceFormatter.collapseUnicodes(currency.symbol);
        } catch (TextInputError error) {
            return false;
        }
        return true;
    }

};