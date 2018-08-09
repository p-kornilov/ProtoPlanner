package com.vividprojects.protoplanner.coredata;

import com.vividprojects.protoplanner.utils.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Block extends RealmObject {
    public final static int PRIORITY_OFF = 0;
    public final static int PRIORITY_DATE = 1;
    public final static int PRIORITY_SIMPLE = 2;

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private Currency currency;
    private String comment;
    private int priorityType;
    //private RealmList<AttachedRecord> records;
    private RealmList<Label> localLabels;
    @LinkingObjects("block")
    private final RealmResults<AttachedRecord> records  = null;

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
        priorityType = Block.PRIORITY_OFF;
        name = new String("");
        comment = new String("");
        localLabels = new RealmList<>();
    }

    public Block(String name, int priority_type, Currency currency) {
        this.priorityType = priority_type;
        this.name = name;
        this.currency = currency;
        this.localLabels = new RealmList<>();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Block(String name, int priority_type, Currency currency, Record r) {
        this(name, priority_type, currency);

    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setPriorityType(int p) { priorityType = p;}

    public int getPriorityType() { return priorityType; }

    public List<AttachedRecord> getRecords() { return records; }

    private float calculateValue() {
        float totalValue = 0;
        boolean error = !currency.isExchangeable();
        for (AttachedRecord ar : records) {
            Variant mVariant = ar.getRecord().getMainVariant();
            if (ar.getRecord().getMainVariant().getCurrency().equals(currency))
                totalValue += mVariant.getValue();
            else {
                error = !mVariant.getCurrency().isExchangeable() || error;
                totalValue += mVariant.getValue()*currency.getExchange_rate()/mVariant.getCurrency().getExchange_rate();
            }
        }
        return error ? -1*totalValue : totalValue;
    }

    public List<Label> getLocalLabels() {
        return localLabels;
    }

    public boolean addLocalLabel(Label l) { return localLabels.add(l);}

    public boolean removeLocalLabel(Label l) { return localLabels.remove(l); }

    public boolean removeLocalLabel(int i) {
        localLabels.remove(i);
        return true;
    }

    public boolean removeLocalLabel(String name) {
        for (Label l : localLabels) {
            if (l.getName().equals(name)) return localLabels.remove(l);
        }
        return false;
    }

    public boolean attachRecord(Record r) {
        if (this.findRecord(r)) {
            return true;
        } else {

            if (!this.isManaged())
                return false;
            else {
                Realm realm = this.getRealm();
                realm.insertOrUpdate(new AttachedRecord(Block.this, r));
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String str = "--- BLOCK ---\nName: \"" + name;
        switch (priorityType) {
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

        if (!localLabels.isEmpty()) {
            for (Label l : localLabels) {
                str += "\tLabel [" + localLabels.indexOf(l) + "] - " + l.toString() + "\n";
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

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.id = id;
        plain.name = name;
        plain.comment = comment;
        plain.currency = currency.getPlain();
        plain.priorityType = priorityType;
        plain.value = calculateValue();
        plain.elementsCount = records != null ? records.size() : 0;
/*        plain.records = new ArrayList<>();
        for (AttachedRecord ar : records)
            plain.records.add(ar.getPlain());*/
/*        plain.localLabels = new ArrayList<>();
        for (Label l : localLabels)
            plain.localLabels.add(l.getPlain());*/
        return plain;
    }

    public class Plain implements Id {
        public String id;
        public String name;
        public String comment;
        public Currency.Plain currency;
        public float value;
        public int elementsCount;
        public int priorityType;
/*        public List<AttachedRecord.Plain> records;
        public List<Label.Plain> localLabels;*/

        @Override
        public String getId() {
            return id;
        }
    }
}
