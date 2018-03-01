package com.vividprojects.protoplanner.CoreData;

import java.text.DecimalFormat;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Helen on 03.01.2018.
 */

public class Currency extends RealmObject {
    public static final int BEFORE = 1;
    public static final int AFTER = 2;
    public static final int WITHIN = 3;

    @PrimaryKey
    private int iso_code_int;
    private String iso_code_str;
    private int iso_name_id;
    private String symbol;
    private int position;
    private int exchange_base = 0;
    private float exchange_rate = 0;
    private int sorting_weight = 0;
    private String custom_name;

    public Currency() {
    }

    public Currency(String iso_code_str, int iso_code_int, int iso_name_id, String symbol, int position){
        this.iso_code_str = iso_code_str;
        this.iso_code_int = iso_code_int;
        this.iso_name_id = iso_name_id;
        this.symbol = symbol;
        this.position = position;
    }

    public Currency(String iso_code_str, int iso_code_int, String custom_name, String symbol, int position){
        this.iso_code_str = iso_code_str;
        this.iso_code_int = iso_code_int;
        this.iso_name_id = 0;
        this.symbol = symbol;
        this.position = position;
        this.custom_name = custom_name;
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

    public int getPosition() {
        return position;
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

    @Override
    public String toString() {
        return "Currency{" +
                "iso_code_int=" + iso_code_int +
                ", iso_code_str='" + iso_code_str + '\'' +
                ", iso_name_id=" + iso_name_id +
                ", symbol='" + symbol + '\'' +
                ", position=" + position +
                ", exchange_base=" + exchange_base +
                ", exchange_rate=" + exchange_rate +
                '}';
    }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.iso_code_int = iso_code_int;
        plain.iso_name_id = iso_name_id;
        plain.iso_code_str = iso_code_str;
        plain.symbol = symbol;
        plain.position = position;
        plain.exchange_base = exchange_base;
        plain.exchange_rate = exchange_rate;
        plain.sorting_weight= sorting_weight;
        plain.custom_name = custom_name;
        return plain;
    }

    public class Plain {
        public int iso_code_int;
        public String iso_code_str;
        public int iso_name_id;
        public String symbol;
        public int position;
        public int exchange_base;
        public float exchange_rate;
        public int sorting_weight;
        public String custom_name;
    }

}
