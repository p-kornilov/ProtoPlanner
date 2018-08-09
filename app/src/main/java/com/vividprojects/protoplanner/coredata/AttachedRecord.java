package com.vividprojects.protoplanner.coredata;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Smile on 27.10.2017.
 */

public class AttachedRecord extends RealmObject {
    @PrimaryKey
    private String id;
    private Record record;
    private Block block;
    private Date priorityDate;
    private int priorityQueue;

    public AttachedRecord() {
    }

    public AttachedRecord(Block block, Record record, int queue, Date date) {
        this.id = UUID.nameUUIDFromBytes((block.getId()+record.getId()).getBytes()).toString();
        this.block = block;
        this.record = record;
        this.priorityDate = date;
        this.priorityQueue = queue;
    }

    public AttachedRecord(Block block, Record record) {
        this(block, record, 0, new Date());
    }

    public AttachedRecord(Block block, Record record, int queue) {
        this(block, record, queue, new Date());
    }

    public AttachedRecord(Block block, Record record, Date date) {
        this(block, record,0, date);
    }

    public Record getRecord() { return record; }

    public Block getBlock() { return block; }

    public Date getPriorityDate() { return priorityDate; }

    public int getPriorityQueue() { return priorityQueue; }

    public void setPriorityDate(Date priorityDate) { this.priorityDate = priorityDate; }

    public void setPriorityQueue(int priorityQueue) { this.priorityQueue = priorityQueue; }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.id = id;
        plain.block = block.getPlain();
        plain.record = record.getPlain();
        plain.date = priorityDate;
        plain.priority = priorityQueue;
        return plain;
    }

    public class Plain {
        public String id;
        public Record.Plain record;
        public Block.Plain block;
        public Date date;
        public int priority;
    }
}
