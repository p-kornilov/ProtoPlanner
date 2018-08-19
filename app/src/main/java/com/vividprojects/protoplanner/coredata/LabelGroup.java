package com.vividprojects.protoplanner.coredata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        plain.name = name != null ? name : "";
        plain.id = id;
        return plain;
    }

    public static class Plain {
        public String name;
        public int color;
        public String id;

        public static <T> List<String> sort(Set<String> list, Map<String, List<T>> map) {
            String[] holder_list = new String[list.size()];
            list.toArray(holder_list);

            Arrays.sort(holder_list,(x, y)->{
                int xLen = map.get(x) != null ? map.get(x).size() : 0;
                int yLen = map.get(y) != null ? map.get(y).size() : 0;
                return yLen - xLen;
            });

            return new ArrayList<>(Arrays.asList(holder_list));
        }
    }
}
