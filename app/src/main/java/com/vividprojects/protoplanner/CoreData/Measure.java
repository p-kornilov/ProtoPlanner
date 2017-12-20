package com.vividprojects.protoplanner.CoreData;


import java.util.Objects;
import java.util.StringTokenizer;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Measure extends RealmObject{
    @PrimaryKey
    private String title;

    public Measure () {

    }

    public Measure(String t) {
        title = new String(t);
    }

    public String getTitle() {
        return title;
    }

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
}

