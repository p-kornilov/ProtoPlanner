package com.vividprojects.protoplanner.Utils;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;

import java.text.DecimalFormat;

/**
 * Created by Helen on 04.01.2018.
 */

public class PriceFormatter {
    public static String getValue(Currency currency, double value){
        DecimalFormat formatter = new DecimalFormat("0.00");
        String svalue = formatter.format(value);
        switch (currency.getPosition()) {
            case Currency.AFTER:
                return svalue+ " " + currency.getSymbol();
            case Currency.WITHIN:
                return svalue.replace('.',currency.getSymbol().charAt(0));
            case Currency.BEFORE:
            default:
                return currency.getSymbol() + svalue;
        }
    }

    public static String getPrice(Currency currency, double price, Measure measure){
        DecimalFormat formatter = new DecimalFormat("0.00");
        return formatter.format(price) + " " + currency.getSymbol() + "/" + measure.getTitle();
    }

    public static String getCount(double count, Measure measure) {
        DecimalFormat formatter;
        switch (measure.getPart()) {
            case Measure.ENTIRE:
                formatter = new DecimalFormat("0");
                break;
            case Measure.FRACTIONAL:
            default:
                formatter = new DecimalFormat("0.00");
        }
        return formatter.format(count) + " " + measure.getTitle();
    }
}
