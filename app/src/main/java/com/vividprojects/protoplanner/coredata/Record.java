package com.vividprojects.protoplanner.coredata;

import com.vividprojects.protoplanner.utils.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Record extends RealmObject{
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private Variant mainVariant;
    private RealmList<Variant> variants;
    private RealmList<Label> labels;
    private String comment;
    @LinkingObjects("record")
    private final RealmResults<AttachedRecord> attachedRecords  = null;

    public Record() {
        mainVariant = null;
        variants = new RealmList<>();
        labels = new RealmList<>();
    }

    public Record(Variant variant) {
        name = variant.getTitle();
        mainVariant = variant;
        variants = new RealmList<>();
        labels = new RealmList<>();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

  /*  public Block getBlock() {
        return block;
    }
*/
    public boolean attachToBlock(Block b) {
/*        if (block.equals(b)) {
            return true;
        } else if (block==null) {
            if (b.addRecord(this)) {
                block = b;
                return true;
            } else
                return false;
        } else*/
            return false;
    }

    public String getId() {
        return id;
    }

    public Variant getMainVariant() { return mainVariant; }

    public List<Variant> getVariants() {
        return variants;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void deleteVariant(Variant variant) {
        String id = variant.getId();
        int i = 0;
        for (Variant v : variants) {
            if (id.equals(v.getId())) {
                variants.remove(i);
                break;
            }
            i++;
        }
    }


    public boolean setMainVariant(Variant v) {
        mainVariant = v; // проверить, можно ли так делать или нужно создать новый объект
        return true;
    }

    public boolean switchMainVariant(Variant v) {
        variants.add(0,mainVariant); // проверить, можно ли так делать или нужно создать новый объект
        mainVariant = v;

        return true;
    }

    public boolean removeVariant(Variant v) { return variants.remove(v); }

    public boolean removeVariant(int i) {
        variants.remove(i);
        return true;
    }

    public boolean removeVariant(String title) {
        for (Variant v : variants) {
            if (v.getTitle().equals(title)) return variants.remove(v);
        }

        return false;
    }

    public boolean addVariant(Variant v) {
        return variants.add(v);
    }

    public boolean addLabel(Label l) { return labels.add(l);}

    public boolean setLebels(List<Label> labels) {
        this.labels.clear();
        return this.labels.addAll(labels);
    }

    public boolean removeLabel(Label l) { return labels.remove(l); }

    public boolean removeLabel(int i) {
        labels.remove(i);
        return true;
    }

    public boolean removeLabel(String name) {
        for (Label l : labels) {
            if (l.getName().equals(name)) return labels.remove(l);
        }

        return false;
    }

    @Override
    public String toString() {
        String str = "--- RECORD ---\n";

        str += "Name - " + name + "\n";

        if (mainVariant!=null) {
            str += "Main variant - " + mainVariant.toString() + "\n";
        } else {
            str += "Main variant is empty\n";
        }

/*        if (block!=null) {
            str += "\tBlock - " + block.getName() + "\n";
        } else { str += "\tNot in block\n";}*/

        if (!labels.isEmpty()) {
            for (Label l : labels) {
                str += "\tLabel [" + labels.indexOf(l) + "] - " + l.toString() + "\n";
            }
        } else { str += "\tNo Labels\n";}

        if (!variants.isEmpty()) {
            for (Variant v : variants) {
                str += "\tAlternative variant [" + variants.indexOf(v) + "] - " + v.toString() + "\n";
            }
        } else { str += "\tAlternative variants is empty\n";}

        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        Record other = (Record) obj;

        return Objects.equals(id,other.id);
    }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.id = id;
        plain.name = name;
        if (mainVariant != null)
            plain.mainVariant = mainVariant.getPlain();
        plain.variants = new ArrayList<>();
        for (Variant variant : variants) {
            plain.variants.add(variant.getPlain());
        }
        plain.labels = new ArrayList<>();
        for (Label label: labels) {
            plain.labels.add(label.getPlain());
        }
        //plain.block = block.getName();
        plain.comment = comment;
        return plain;
    }

    public class Plain implements Id {
        public String id;
        public String name;
        public Variant.Plain mainVariant;
        public List<Variant.Plain> variants;
        public List<Label.Plain> labels;
        public String block;
        public String comment;

        @Override
        public String getId() {
            return id;
        }
    }
}
