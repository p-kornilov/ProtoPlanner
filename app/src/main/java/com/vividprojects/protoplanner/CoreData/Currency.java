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

    public Currency() {
    }

    public Currency(String iso_code_str, int iso_code_int, int iso_name_id, String symbol, int position){
        this.iso_code_str = iso_code_str;
        this.iso_code_int = iso_code_int;
        this.iso_name_id = iso_name_id;
        this.symbol = symbol;
        this.position = position;
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
        return "Currency : "+symbol+" iso code - " + iso_code_str + "(" + iso_code_int + ")" + ", name id - " + iso_name_id;
    }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.iso_code_int = iso_code_int;
        plain.iso_name_id = iso_name_id;
        plain.iso_code_str = iso_code_str;
        plain.symbol = symbol;
        plain.position = position;
        return plain;
    }

    public class Plain {
        public int iso_code_int;
        public String iso_code_str;
        public int iso_name_id;
        public String symbol;
        public int position;
    }

}
