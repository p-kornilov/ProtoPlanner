package com.vividprojects.protoplanner.CoreData;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Smile on 27.10.2017.
 */

public class AttachedRecord extends RealmObject {
    private Record record;
    private Date date;
    private int priority;

    private void init(Record r, int p, Date dt) {
        record = r;
        date = dt;
        priority = 0;
    }

    public AttachedRecord() {
        init(null,0, new Date());
    }

    public AttachedRecord(Record r) {
        init(r,0, new Date());
    }

    public AttachedRecord(Record r, int p) {
        init(r, p, new Date());
    }

    public AttachedRecord(Record r, Date dt) {
        init(r,0, dt);
    }

    public Record getRecord() { return record; }

    public Date getDate() { return date; }

    public int getPriority() { return priority; }

    public void setRecord(Record record) { this.record = record; }

    public void setDate(Date date) { this.date = date; }

    public void setPriority(int priority) { this.priority = priority; }
}
