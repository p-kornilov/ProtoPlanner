package com.vividprojects.protoplanner.coredata;

import android.content.Context;

import com.vividprojects.protoplanner.utils.PriceFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Helen on 03.01.2018.
 */

public class Currency extends RealmObject {
    public static final int BEFORE_SPACE = 0;
    public static final int BEFORE = 1;
    public static final int WITHIN = 2;
    public static final int AFTER = 3;
    public static final int AFTER_SPACE = 4;

    @PrimaryKey
    private int iso_code_int = -1;
    private String iso_code_str;
    private int iso_name_id = 0;
    private String symbol = "¤";
    private int pattern = 0;
    private int exchange_base = 0;
    private float exchange_rate = 0;
    private int sorting_weight = 0;
    private String custom_name;
    private boolean auto_update = false;
    private boolean isBase = false;
    private int flag_id = 0;
    private String flag_file;

    public Currency() {
    }

    public Currency(String iso_code_str, int iso_code_int, int iso_name_id, String symbol, int pattern, boolean auto_update, int sorting_weight, int flag_id){
        this.iso_code_str = iso_code_str;
        this.iso_code_int = iso_code_int;
        this.iso_name_id = iso_name_id;
        this.symbol = symbol;
        this.pattern = pattern;
        this.auto_update = auto_update;
        this.sorting_weight = sorting_weight;
        this.flag_id = flag_id;
    }

/*    public Currency(String iso_code_str, int iso_code_int, int iso_name_id, String symbol, int pattern, boolean auto_update){
        this.iso_code_str = iso_code_str;
        this.iso_code_int = iso_code_int;
        this.iso_name_id = iso_name_id;
        this.symbol = symbol;
        this.pattern = pattern;
        this.auto_update = auto_update;
    }*/

/*
    public Currency(String iso_code_str, int iso_code_int, String custom_name, String symbol, int pattern, boolean auto_update){
        this.iso_code_str = iso_code_str;
        this.iso_code_int = iso_code_int;
        this.iso_name_id = 0;
        this.symbol = symbol;
        this.pattern = pattern;
        this.custom_name = custom_name;
        this.auto_update = auto_update;
    }
*/

    public boolean isExchangeable() {
        return exchange_base != 0 && exchange_rate != 0;
    }

    public Currency(Currency.Plain plain) {
        iso_code_int = plain.iso_code_int;
        iso_name_id = 0;
        iso_code_str = plain.iso_code_str;
        symbol = PriceFormatter.collapseUnicodes(plain.symbol);
        pattern = plain.pattern;
        exchange_base = plain.exchange_base;
        exchange_rate = plain.exchange_rate;
        sorting_weight = 0;
        custom_name = plain.custom_name;
        auto_update = plain.auto_update;
        isBase = plain.isBase;
    }

    public void setIsBase(boolean b) {
        isBase = b;
    }

    public boolean isBase() {
        return isBase;
    }

    public void setExchange_rate(float exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public void setExchange_base(int exchange_base) {
        this.exchange_base = exchange_base;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public int getExchange_base() {
        return exchange_base;
    }

    public float getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(float exchange_rate, int exchange_base) {
        this.exchange_rate = exchange_rate;
        this.exchange_base = exchange_base;
    }

    public int getSorting_weight() {
        return sorting_weight;
    }

    public void setSorting_weight(int sorting_weight) {
        this.sorting_weight = sorting_weight;
    }

    public int getPattern() {
        return pattern;
    }

    public String getIso_code_str() {
        return iso_code_str;
    }

    public int getIso_code_int() {
        return iso_code_int;
    }

    public int getIso_name_id() {
        return iso_name_id;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isAuto_update() {
        return auto_update;
    }

    public void setAuto_update(boolean auto_update) {
        this.auto_update = auto_update;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "iso_code_int=" + iso_code_int +
                ", iso_code_str='" + iso_code_str + '\'' +
                ", iso_name_id=" + iso_name_id +
                ", symbol='" + symbol + '\'' +
                ", pattern=" + pattern +
                ", exchange_base=" + exchange_base +
                ", exchange_rate=" + exchange_rate +
                ", auto_update=" + auto_update +
                ", is base=" + isBase +
                ", flag id=" + flag_id +
                ", flag file=" + flag_file +
                '}';
    }

    public void update(Currency.Plain plain) {
        iso_name_id = plain.iso_name_id;
        iso_code_str = plain.iso_code_str;
        symbol = PriceFormatter.collapseUnicodes(plain.symbol);
        pattern = plain.pattern;
        exchange_base = plain.exchange_base;
        exchange_rate = plain.exchange_rate;
        sorting_weight = plain.sorting_weight;
        custom_name = plain.custom_name;
        auto_update = plain.auto_update;
        isBase = plain.isBase;
        flag_id = plain.flag_id;
        flag_file = plain.flag_file;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Currency other = (Currency) obj;
        return iso_code_int == other.iso_code_int;
    }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.iso_code_int = iso_code_int;
        plain.iso_name_id = iso_name_id;
        plain.iso_code_str = iso_code_str;
        plain.symbol = symbol;
        plain.pattern = pattern;
        plain.exchange_base = exchange_base;
        plain.exchange_rate = exchange_rate;
        plain.sorting_weight= sorting_weight;
        plain.custom_name = custom_name;
        plain.auto_update = auto_update;
        plain.isBase = isBase;
        plain.flag_id = flag_id;
        plain.flag_file = flag_file;
        return plain;
    }

    public static class Plain {
        public int iso_code_int;
        public String iso_code_str;
        public int iso_name_id;
        public String symbol;
        public int pattern;
        public int exchange_base;
        public float exchange_rate;
        public int sorting_weight;
        public String custom_name;
        public boolean auto_update;
        public boolean isBase;
        public int flag_id;
        public String flag_file;

        public static String getString(Context context, String string, int stringId) {
            return string != null ? string : context.getResources().getString(stringId);
        }

        public static List<Plain> sort(Context context, List<Plain> list) {
            Map<Integer,String> names = new HashMap<>();
            for (Plain c : list)
                names.put(c.iso_code_int,getString(context,c.custom_name,c.iso_name_id).toLowerCase());

            Plain[] holder_list = new Plain[list.size()];
            list.toArray(holder_list);

            Arrays.sort(holder_list,(x, y)->{
                if (x.sorting_weight == y.sorting_weight)
                    return names.get(x.iso_code_int).compareTo(names.get(y.iso_code_int));
                else
                    return (y.sorting_weight - x.sorting_weight)*100;
            });

            return new ArrayList<>(Arrays.asList(holder_list));
        }
    }

}
