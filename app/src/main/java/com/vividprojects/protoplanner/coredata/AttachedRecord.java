package com.vividprojects.protoplanner.coredata;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Smile on 27.10.2017.
 */

public class AttachedRecord extends RealmObject {
    private Record record;
    private Date priorityDate;
    private int priorityQueue;

    public AttachedRecord(Record record, int queue, Date date) {
        this.record = record;
        this.priorityDate = date;
        this.priorityQueue = queue;
    }

    public AttachedRecord() {
        this(null,0, new Date());
    }

    public AttachedRecord(Record record) {
        this(record,0, new Date());
    }

    public AttachedRecord(Record record, int queue) {
        this(record, queue, new Date());
    }

    public AttachedRecord(Record record, Date date) {
        this(record,0, date);
    }

    public Record getRecord() { return record; }

    public Date getPriorityDate() { return priorityDate; }

    public int getPriorityQueue() { return priorityQueue; }

    public void setRecord(Record record) { this.record = record; }

    public void setPriorityDate(Date priorityDate) { this.priorityDate = priorityDate; }

    public void setPriorityQueue(int priorityQueue) { this.priorityQueue = priorityQueue; }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.record = record.getPlain();
        plain.date = priorityDate;
        plain.priority = priorityQueue;
        return plain;
    }

    public class Plain {
        public Record.Plain record;
        public Date date;
        public int priority;
    }
}
