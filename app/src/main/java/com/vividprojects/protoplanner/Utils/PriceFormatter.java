package com.vividprojects.protoplanner.Utils;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;

import java.text.DecimalFormat;

/**
 * Created by Helen on 04.01.2018.
 */

public class PriceFormatter {
    public static String getValue(Currency.Plain currency, double value){
        DecimalFormat formatter = new DecimalFormat("0.00");
        String svalue = formatter.format(value);
        switch (currency.position) {
            case Currency.AFTER:
                return svalue+ " " + currency.symbol;
            case Currency.WITHIN:
                return svalue.replace('.',currency.symbol.charAt(0));
            case Currency.BEFORE:
            default:
                return currency.symbol + svalue;
        }
    }

    public static String getPrice(Currency.Plain currency, double price, Measure.Plain measure){
        DecimalFormat formatter = new DecimalFormat("0.00");
        return formatter.format(price) + " " + currency.symbol + "/" + measure.title;
    }

    public static String getCount(double count, Measure.Plain measure) {
        DecimalFormat formatter;
        switch (measure.part) {
            case Measure.ENTIRE:
                formatter = new DecimalFormat("0");
                break;
            case Measure.FRACTIONAL:
            default:
                formatter = new DecimalFormat("0.00");
        }
        return formatter.format(count) + " " + measure.title;
    }
}
