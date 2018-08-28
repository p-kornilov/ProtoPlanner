package com.vividprojects.protoplanner.coredata;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Label extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private LabelGroup group;
    private int color;
    private Block block;

    public Label() {
        this("", 0, null, null);
    }

    public Label(String name, int color, LabelGroup group, Block block){
        this.name = name;
        this.color = color;
        this.block = block;
        this.group = group;
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
        String block_name = ", block: ";
        if (block==null) block_name += "none";
        else block_name += block.getName();
        return "Label name: " + name + ", color: " + color + block_name;
    }

    public Plain getPlain(){
        Plain plain = new Plain();
        plain.color = color;
        plain.name = name;
        plain.group = group.getPlain();
        plain.id = id;
        return plain;
    }

    public static Plain getPlain(int color, String name, LabelGroup.Plain group, String id){
        Plain plain = new Plain();
        plain.color = color;
        plain.name = name;
        plain.group = group;
        plain.id = id;
        return plain;
    }

    public static Plain getPlain(int color, String name, String groupId, String id){
        Plain plain = new Plain();
        plain.color = color;
        plain.name = name;
        plain.group = new LabelGroup.Plain();
        plain.group.id = groupId;
        plain.id = id;
        return plain;
    }

    public static class Plain {
        public String name;
        public LabelGroup.Plain group;
        public int color;
        public String id;
    }
}
