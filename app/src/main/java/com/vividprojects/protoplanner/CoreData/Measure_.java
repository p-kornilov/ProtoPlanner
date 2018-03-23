package com.vividprojects.protoplanner.CoreData;


import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Measure_ extends RealmObject{
    public static final int PATTERN_ENTIRE = 0;
    public static final int PATTERN_FRACTIONAL = 1;

    public static final int MEASURE_UNIT = 0;
    public static final int MEASURE_MASS = 1;
    public static final int MEASURE_LENGTH = 2;
    public static final int MEASURE_SQUARE = 3;
    public static final int MEASURE_VOLUME = 4;

    public static final int SYSTEM_METRIC = 0;
    public static final int SYSTEM_BRITAIN = 1;
    public static final int SYSTEM_JAPAN = 2;
    public static final int SYSTEM_CHINA = 3;
    public static final int SYSTEM_CUSTOM = 4;


    @PrimaryKey
    private int hash;
    private String title;
    private int title_id;
    private int measure;
    private String symbol;
    private int system;
    private int pattern;

    public Measure_() {
    }

    public Measure_(int title_id, int measure, String symbol, int system, int pattern) {
        this.title_id = title_id;
        this.measure = measure;
        this.symbol = symbol;
        this.system = system;
        this.pattern = pattern;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitle_id() {
        return title_id;
    }

    public void setTitle_id(int title_id) {
        this.title_id = title_id;
    }

    public int getMeasure() {
        return measure;
    }

    public void setMeasure(int measure) {
        this.measure = measure;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public int getPattern() {
        return pattern;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measure_ measure_ = (Measure_) o;

        if (hash != measure_.hash) return false;
        if (title_id != measure_.title_id) return false;
        if (measure != measure_.measure) return false;
        if (system != measure_.system) return false;
        if (pattern != measure_.pattern) return false;
        if (title != null ? !title.equals(measure_.title) : measure_.title != null) return false;
        return symbol.equals(measure_.symbol);
    }

    @Override
    public int hashCode() {
        int result = measure;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + title_id;
        result = 31 * result + symbol.hashCode();
        result = 31 * result + system;
        result = 31 * result + pattern;
        return result;
    }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.title = title;

        return plain;
    }

    public class Plain {
        public String title;
        public int part;
    }
}

