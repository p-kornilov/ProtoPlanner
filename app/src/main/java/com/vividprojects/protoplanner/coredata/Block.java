package com.vividprojects.protoplanner.coredata;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Block extends RealmObject {
    public final static int PRIORITY_OFF = 0;
    public final static int PRIORITY_DATE = 1;
    public final static int PRIORITY_SIMPLE = 2;

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private Currency currency;
    private int priority_type;
    private RealmList<AttachedRecord> records;
    private RealmList<Label> local_labels;

    @Override
    public int hashCode() {

//        // Start with a non-zero constant. Prime is preferred
//        int result = 17;
//
//        // Include a hash for each field.
//
//        // Primatives
//
//        result = 31 * result + (booleanField ? 1 : 0);                   // 1 bit   » 32-bit
//
//        result = 31 * result + byteField;                                // 8 bits  » 32-bit
//        result = 31 * result + charField;                                // 16 bits » 32-bit
//        result = 31 * result + shortField;                               // 16 bits » 32-bit
//        result = 31 * result + intField;                                 // 32 bits » 32-bit
//
//        result = 31 * result + (int)(longField ^ (longField >>> 32));    // 64 bits » 32-bit
//
//        result = 31 * result + Float.floatToIntBits(floatField);         // 32 bits » 32-bit
//
//        long doubleFieldBits = Double.doubleToLongBits(doubleField);     // 64 bits (double) » 64-bit (long) » 32-bit (int)
//        result = 31 * result + (int)(doubleFieldBits ^ (doubleFieldBits >>> 32));
//
//        // Objects
//
//        result = 31 * result + Arrays.hashCode(arrayField);              // var bits » 32-bit
//
//        result = 31 * result + referenceField.hashCode();                // var bits » 32-bit (non-nullable)
//        result = 31 * result +                                           // var bits » 32-bit (nullable)
//                (nullableReferenceField == null
//                        ? 0
//                        : nullableReferenceField.hashCode());
//
//        return result;
//
        return 31 * 17 + name.hashCode();
    }

    public String getId() {
        return id;
    }

    private boolean findRecord(Record r) {
        for (AttachedRecord pr : records) {
            if (pr.getRecord().equals(r)) {
                return true;
            }
        }
        return false;
    }

    public Block() {
        priority_type = Block.PRIORITY_OFF;
        name = new String("");
        records = new RealmList<>();
        local_labels = new RealmList<>();
    }

    public Block(String name, int priority_type, Currency currency) {
        this.priority_type = priority_type;
        this.name = name;
        this.currency = currency;
        this.records = new RealmList<>();
        this.local_labels = new RealmList<>();
    }

    public Block(String name, int priority_type, Currency currency, Record r) {
        this(name, priority_type, currency);
        this.records.add(new AttachedRecord(r));
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setPriorityType(int p) { priority_type = p;}

    public int getPriority_type() { return priority_type; }

    public List<AttachedRecord> getRecords() { return records; }

    private double calculateValue() {
        сделать рассчет
        double totalValue = 0;
        for (AttachedRecord pr : records) {
            totalValue += pr.getRecord().getMainVariant().getValue();
        }

        return totalValue;
    }

    public List<Label> getLocalLabels() {
        return local_labels;
    }

    public boolean addLocalLabel(Label l) { return local_labels.add(l);}

    public boolean removeLocalLabel(Label l) { return local_labels.remove(l); }

    public boolean removeLocalLabel(int i) {
        local_labels.remove(i);
        return true;
    }

    public boolean removeLocalLabel(String name) {
        for (Label l : local_labels) {
            if (l.getName().equals(name)) return local_labels.remove(l);
        }
        return false;
    }

    public boolean addRecord(Record r) {
        if (this.findRecord(r)) {
            return true;
        } else {
            if (records.add(new AttachedRecord(r))) {
                return r.addToBlock(this);
            } else
                return false;
        }
    }

    @Override
    public String toString() {
        String str = "--- BLOCK ---\nName: \"" + name;
        switch (priority_type) {
            case Block.PRIORITY_OFF:
                str += "; Priority is OFF";
                break;
            case Block.PRIORITY_DATE:
                str += "; Priority is Date";
                break;
            case Block.PRIORITY_SIMPLE:
                str += "; Priority is Simple";
                break;
        }

        str +="\nLocal labels:\n";

        if (!local_labels.isEmpty()) {
            for (Label l : local_labels) {
                str += "\tLabel [" + local_labels.indexOf(l) + "] - " + l.toString() + "\n";
            }
        } else { str += "\tNo Local labels\n";}

        str +="Records:\n";

        if (records.isEmpty()) {
            str += "\tNo records";
        } else {
            for (AttachedRecord pr : records) {
                str += "\t" + pr.getRecord().getMainVariant() + "\n";
            }
        }

        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Block other = (Block) obj;
        return Objects.equals(id,other.id);
    }

    public class Plain {

    }
}
