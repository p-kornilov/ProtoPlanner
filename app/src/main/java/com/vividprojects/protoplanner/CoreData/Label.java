package com.vividprojects.protoplanner.CoreData;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Label extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private int color;
    private String comment;
    private Block block;

    public Label() {
        name = "";
        color = 0;
        comment = "";
        block = null;
    }

    public Label(String name, int color, String comment, Block block){
        this.name = name;
        this.color = color;
        this.comment = comment;
        this.block = block;
    }

    public String getId() {
        return id;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
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
        return "Label name: " + name + ", color: " + color + ", comment: " + comment + block_name;
    }

    public Plain getPlain(){
        Plain plain = new Plain();
        plain.color = color;
        plain.name = name;
        plain.id = id;
        return plain;
    }

    public class Plain {
        public String name;
        public int color;
        public String id;
    }
}
