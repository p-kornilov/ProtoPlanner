package com.vividprojects.protoplanner.Utils;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helen on 04.01.2018.
 */

public class PriceFormatter {
    public static String createValue(Currency.Plain currency, double value){
        return createValue(currency.symbol,value,currency.pattern);
    }

    public static String createValue(String symbol, double value, int pattern){
        DecimalFormat formatter = new DecimalFormat("0.00");
        String svalue = formatter.format(value);
        switch (pattern) {
            case Currency.BEFORE:
                return symbol + svalue;
            case Currency.AFTER:
                return svalue + symbol;
            case Currency.AFTER_SPACE:
                return svalue+ " " + symbol;
            case Currency.WITHIN:
                if (symbol.equals("$"))
                    return svalue.replaceAll("[\\.,]","\\$");
                else
                    return svalue.replaceAll("[\\.,]",symbol);
            case Currency.BEFORE_SPACE:
            default:
                return symbol + " " + svalue;
        }
    }

    public static List<String> createListValue(String symbol, double value){
        ArrayList<String> list = new ArrayList<>();
        list.add(createValue(symbol,value,Currency.BEFORE_SPACE));
        list.add(createValue(symbol,value,Currency.BEFORE));
        list.add(createValue(symbol,value,Currency.WITHIN));
        list.add(createValue(symbol,value,Currency.AFTER));
        list.add(createValue(symbol,value,Currency.AFTER_SPACE));
        return list;
    }

    public static String createPrice(Currency.Plain currency, double price, Measure.Plain measure){
        DecimalFormat formatter = new DecimalFormat("0.00");
        return formatter.format(price) + " " + currency.symbol + "/" + measure.title;
    }

    public static String createCount(double count, Measure.Plain measure) {
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

    public static String extendUnicodes(String string) {
        StringBuilder convertedString = new StringBuilder();
        char c;
        String hexString;
        String hexPrepend;
        for (int i = 0; i < string.length();i++) {
            c = string.charAt(i);
            if (Character.isLetterOrDigit(c))
                convertedString.append(c);
            else {
                hexString = Integer.toHexString((int)c).toUpperCase();
                switch (hexString.length()) {
                    case 1:
                        hexPrepend = "\\U+000";
                        break;
                    case 2:
                        hexPrepend = "\\U+00";
                        break;
                    case 3:
                        hexPrepend = "\\U+0";
                        break;
                    default:
                        hexPrepend = "\\U+";
                }
                convertedString.append(hexPrepend);
                convertedString.append(hexString);
            }
        }

        return convertedString.toString();
    }

    public static String collapseUnicodes(String string) { //TODO Добавить проверку на корректность входа
        StringBuilder convertedString = new StringBuilder();
        char c;
        for (int i = 0; i < string.length();i++) {
            c = string.charAt(i);
            if (c != '\\')
                convertedString.append(c);
            else {
                if (string.substring(i,i+3).toUpperCase().equals("\\U+")) {
                    c = (char) Integer.parseInt(string.substring(i+3,i+7),16);
                    i += 6;
                    convertedString.append(c);
                }
            }
        }
        return convertedString.toString();
    }
}
