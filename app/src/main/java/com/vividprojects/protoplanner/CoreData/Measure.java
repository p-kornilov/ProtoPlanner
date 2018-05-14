package com.vividprojects.protoplanner.CoreData;


import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Measure extends RealmObject{
    public static final int PATTERN_ENTIRE = 0;
    public static final int PATTERN_FRACTIONAL = 1;

    public static final int MEASURE_UNIT = 0;
    public static final int MEASURE_MASS = 1;
    public static final int MEASURE_LENGTH = 2;
    public static final int MEASURE_SQUARE = 3;
    public static final int MEASURE_VOLUME = 4;
    public static final int MEASURE_LIQUIDDRY = 5;

    public static final int SYSTEM_METRIC = 0;
    public static final int SYSTEM_BRITAIN = 1;
    public static final int SYSTEM_CHINA = 2;
    public static final int SYSTEM_JAPAN = 3;
    public static final int SYSTEM_CUSTOM = 4;


    @PrimaryKey
    private int hash;
    private String name;
    private int nameId = 0;
    private int measure = MEASURE_UNIT;
    private String symbol;
    private int symbolId = 0;
    private int system;
    private int pattern = PATTERN_FRACTIONAL;
    private boolean def = false;

    public Measure() {
        name = "";
        symbol = "";
    }

    public Measure(int nameId, int measure, int symbolId, int system, int pattern, boolean def) {
        this.nameId = nameId;
        this.measure = measure;
        this.symbolId = symbolId;
        this.system = system;
        this.pattern = pattern;
        this.def = def;
        this.hash = hashCode();
    }

    public Measure(String name, int measure, String symbol, int system, int pattern, boolean def) {
        this.name = name;
        this.measure = measure;
        this.symbol = symbol;
        this.system = system;
        this.pattern = pattern;
        this.def = def;
        this.hash = hashCode();
    }

    public Measure(Plain measure, int system) {
        this.name = measure.name;
        this.measure = measure.measure;
        this.symbol = measure.symbol;
        this.system = system;
        this.pattern = measure.pattern;
        this.def = measure.def;
        this.hash = hashCode();
    }

    public void update(Plain m) {
        this.def = m.def;
        this.name = m.name;
        this.nameId = m.nameId;
        this.measure = m.measure;
        this.symbol = m.symbol;
        this.symbolId = m.symbolId;
        this.system = m.system;
        this.pattern = m.pattern;
       // this.hash = m.hash;
    }

    public boolean isDef() {
        return def;
    }

    public void setDef(boolean def) {
        this.def = def;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
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

    public int getHash() {
        return this.hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measure measure_ = (Measure) o;

        return hash == measure_.hash;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "hash=" + hash +
                ", name='" + name + '\'' +
                ", nameId=" + nameId +
                ", measure=" + measure +
                ", symbol='" + symbol + '\'' +
                ", symbolId='" + symbolId + '\'' +
                ", system=" + system +
                ", pattern=" + pattern +
                ", default=" + def +
                '}';
    }

    @Override
    public int hashCode() {
/*        int result = measure;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + nameId;
        result = 31 * result + symbolId;
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + system;
        result = 31 * result + pattern;
        return result;*/
        return UUID.randomUUID().toString().hashCode();
    }

    public Plain createHeader(int measure, int nameResource) {
        Plain plain = new Plain();
        plain.nameId = nameResource;
        plain.measure = measure;
        plain.header = true;
        return plain;
    }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.name = name;
        plain.nameId = nameId;
        plain.measure = measure;
        plain.symbol = symbol;
        plain.symbolId = symbolId;
        plain.system = system;
        plain.pattern = pattern;
        plain.def = def;
        plain.hash = hash;
        return plain;
    }

    public static class Plain {
        public String name;
        public int nameId = 0;
        public int measure;
        public String symbol;
        public int symbolId = 0;
        public int system;
        public int pattern;
        public boolean def;
        public int hash;
        public boolean header = false;

        public static String getString(Context context, String string, int stringId) {
            return string != null ? string : context.getResources().getString(stringId);
        }

        public static List<Plain> sort(Context context, List<Plain> list) {
            Map<Integer,String> names = new HashMap<>();
            for (Plain m : list)
                names.put(m.hash,getString(context,m.name,m.nameId).toLowerCase());

            Plain[] holder_list = new Plain[list.size()];
            list.toArray(holder_list);

            Arrays.sort(holder_list,(x, y)->{
                if (x.measure == y.measure) {
                    if (x.header)
                        return -10000;
                    if (y.header)
                        return 10000;
                    if (x.def)
                        return -1000;
                    if (y.def)
                        return 1000;
                    return names.get(x.hash).compareTo(names.get(y.hash));
                } else
                    return (x.measure - y.measure) * 100;
            });

            return new ArrayList<>(Arrays.asList(holder_list));
        }
    }
}

