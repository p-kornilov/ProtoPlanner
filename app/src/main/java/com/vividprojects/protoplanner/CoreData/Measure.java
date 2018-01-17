package com.vividprojects.protoplanner.CoreData;


import java.util.Objects;
import java.util.StringTokenizer;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Measure extends RealmObject{
    public static final int FRACTIONAL = 1;
    public static final int ENTIRE = 2;
    @PrimaryKey
    private String title;
    private int part;

    public Measure () {

    }

    public Measure(String title, int part) {
        this.title = title;
        this.part = part;
    }

    public String getTitle() {
        return title;
    }

    public int getPart() { return part;}

    @Override
    public String toString() {
        return this.getTitle();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        Measure other = (Measure) obj;

        return Objects.equals(title,other.title);
    }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.title = title;
        plain.part = part;
        return plain;
    }

    public class Plain {
        public String title;
        public int part;
    }
}

