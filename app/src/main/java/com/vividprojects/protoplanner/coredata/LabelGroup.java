package com.vividprojects.protoplanner.coredata;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LabelGroup extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private int color;

    public LabelGroup() {
        this("",0);
    }

    public LabelGroup(String name, int color){
        this.name = name;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Label group name: " + name + ", color: " + color;
    }

    public Plain getPlain(){
        Plain plain = new Plain();
        plain.color = color;
        plain.name = name;
        plain.id = id;
        return plain;
    }

    public static Plain getPlain(int color, String name, String id){
        Plain plain = new Plain();
        plain.color = color;
        plain.name = name;
        plain.id = id;
        return plain;
    }

    public static class Plain {
        public String name;
        public int color;
        public String id;
    }
}
