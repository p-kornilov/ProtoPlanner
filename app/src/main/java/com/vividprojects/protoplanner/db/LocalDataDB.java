package com.vividprojects.protoplanner.db;

import android.content.Context;
import android.util.Log;

import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Measure;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.coredata.VariantInShop;
import com.vividprojects.protoplanner.utils.Bundle1;
import com.vividprojects.protoplanner.utils.Settings;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

//import io.realm.CurrencyRealmProxy;
import io.realm.ObjectChangeSet;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by p.kornilov on 25.12.2017.
 */

@Singleton
public class LocalDataDB {
    private Realm realm;
    private Context context;

    @Inject
    public LocalDataDB(Context context){
        this.context = context;
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

    }

    public void initDB(String imagesDirectory){
        InitHelperLocalDB.init(realm, context, imagesDirectory);
    }

    public Label.Plain createLabel(Label.Plain label) {
        Label newLabel = new Label(label.name, label.color,"",null);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(newLabel);
            }
        });
        return newLabel.getPlain();
    }

    public Label.Plain editLabel(Label.Plain label) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Label editLabel = realm.where(Label.class).equalTo("id",label.id).findFirst();
                if (editLabel != null) {
                    editLabel.setName(label.name);
                    editLabel.setColor(label.color);
                }
            }
        });
        return label;
    }

    public String setRecordName(String id, String name) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Record record = realm.where(Record.class).equalTo("id",id).findFirst();
                if (record != null) {
                    record.setName(name);
                }
            }
        });
        return name;
    }

    public String setRecordComment(String id, String comment) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Record record = realm.where(Record.class).equalTo("id",id).findFirst();
                if (record != null) {
                    record.setComment(comment);
                }
            }
        });
        return comment;
    }

    public void showDB(){
        /*        String measures = "[";

        RealmResults<Measure> ms = realm.where(Measure.class).findAll();
        for (Measure m : ms) {
            measures += m.getName() + ";";
        }

        measures = measures.substring(0,measures.length()-1)+"]";

        Log.d("Test", "------------------------------ "+measures);

        Log.d("Test", "------------------------------ Variants:");

        RealmResults<Variant> vs = realm.where(Variant.class).findAll();
        for (Variant v : vs) {
            Log.d("Test", v.getName());
        }
*/
        Log.d("Test", "------------------------------ Labels:");
        RealmResults<Label> ls = realm.where(Label.class).findAll();
        for (Label l : ls) {
            Log.d("Test", l.toString());
        }

        Log.d("Test", "------------------------------ Shops:");
        RealmResults<VariantInShop> vis = realm.where(VariantInShop.class).findAll();
        for (VariantInShop vi : vis) {
            Log.d("Test", vi.toString());
        }

/*        Log.d("Test", "------------------------------ Records:");
        RealmResults<Record> rs = realm.where(Record.class).findAll();
        for (Record r : rs) {
            Log.d("Test", r.toString());
        }

        Log.d("Test", "------------------------------ Blocks:");
        RealmResults<Block> bs = realm.where(Block.class).findAll();
        for (Block b : bs) {
            Log.d("Test", b.toString());
        }*/

    }

    public void deleteLabel(String id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Label v = realm.where(Label.class).contains("id",id).findFirst();
                if (v != null)
                    v.deleteFromRealm();
            }
        });
    }

    public void deleteCurrency(int iso_code_int) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Currency c = realm.where(Currency.class).equalTo("iso_code_int",iso_code_int).findFirst();
                if (c != null)
                    c.deleteFromRealm();
            }
        });
    }

    public void setDefaultCurrency(int iso_code_int) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Currency c = realm.where(Currency.class).equalTo("iso_code_int",iso_code_int).findFirst();
                RealmResults<Currency> cs = realm.where(Currency.class).findAll();
                if (cs != null && c != null) {
                    //Currency base = c;
                    for (Currency ci : cs)
                        if (ci != c) {
                            ci.setExchange_rate(ci.getExchange_rate() / c.getExchange_rate());
                            ci.setIsBase(false);
                        }
                    c.setExchange_rate(1);
                    c.setIsBase(true);
                }
            }
        });
    }

    public void addImageToVariant(String variant, String image) {
        final Integer result = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Variant v = realm.where(Variant.class).contains("id",variant).findFirst();
                if (v != null) {
                    v.addImage(image);
                }
            }
        });
    }

    public void saveLabelsForRecord(String recordId, String[] ids) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Record r = realm.where(Record.class).contains("id",recordId).findFirst();
                RealmResults<Label> labels = realm.where(Label.class).in("id",ids).findAll();
                if (r != null && labels != null) {
                    r.setLebels(labels);
                }
            }
        });
    }

    public int saveCurrency(Currency.Plain currency) {
        final Bundle1<Integer> id = new Bundle1<>();
        id.item = -1;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (currency.iso_code_int > 0) {
                    Currency c = realm.where(Currency.class).equalTo("iso_code_int", currency.iso_code_int).findFirst();
                    if (c != null) {
                        c.update(currency);
                        id.item = currency.iso_code_int;
                    }
                } else {
                    RealmResults<Currency> c = realm.where(Currency.class).greaterThan("iso_code_int", 1000).findAll();
                    int lastId = 1001;
                    if (c != null && c.size() > 0) {
                        Currency cc = c.sort("iso_code_int", Sort.DESCENDING).first();
                        lastId = cc.getIso_code_int()+1;
                    }
                    currency.iso_code_int = lastId;
                    id.item = lastId;
                    realm.insertOrUpdate(new Currency(currency));
                }
            }
        });
        return id.item;
    }

    //---------------- Records --------------------------------------------------------------
    public QueryRecords queryRecords() {
        return new QueryRecords();
    }

    public class QueryRecords {
        RealmQuery<Record> query;

        public QueryRecords() {
            query = realm.where(Record.class);
        }

        public QueryRecords id_equalTo(String id) {
            query = query.equalTo("id",id);
            return this;
        }

        public QueryRecords mainVariant_equalTo(String title) {
            query = query.equalTo("mainVariant.id",title);
            return this;
        }

        public QueryRecords block_equalTo(String name) {
            query = query.equalTo("block.name",name);
            return this;
        }

        public QueryRecords labels_equalTo(List<String> ids) {
            if (ids.size()>0) {
                query = query.beginGroup();
                for (int i = 0;i<ids.size()-1;i++) {
                    query = query.equalTo("labels.id",ids.get(i)).or();
                }
                query = query.equalTo("labels.id",ids.get(ids.size()-1));
                query = query.endGroup();
            }
            return this;
        }

        public List<Record> findAll() {
            RealmResults<Record> rr = query.findAll();
            ArrayList<Record> al = new ArrayList<>();
            al.addAll(rr);
            return al;
        }

        public Record findFirst() {
            Record rr = query.findFirst();
            return rr;
        }
    }

    //---------------- Variants -------------------------------------------------------------
    public QueryVariants queryVariants() {
        return new QueryVariants();
    }

    public class QueryVariants {
        RealmQuery<Variant> query;

        public QueryVariants () {
            query = realm.where(Variant.class);
        }

        public QueryVariants id_equalTo(String title) {
            query = query.equalTo("id",title);
            return this;
        }

        public List<Variant> findAll() {
            RealmResults<Variant> rr = query.findAll();
            ArrayList<Variant> al = new ArrayList<>();
            al.addAll(rr);
            return al;
        }

        public Variant findFirst() {
            Variant rr = query.findFirst();
            return rr;
        }
    }

    public String saveVariant(String id, String name, double price, double count, int currency, int measure) {
        Currency c = realm.where(Currency.class).equalTo("iso_code_int", currency).findFirst();
        Measure  m = realm.where(Measure.class ).equalTo("hash",         measure ).findFirst();
        if (c == null || m == null)
            return null;
        final Bundle1<String> bid = new Bundle1<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Variant v = null;
                if (id != null) {
                    v = realm.where(Variant.class).equalTo("id", id).findFirst();
                    if (v != null) {
                        v.setTitle(name);
                        v.setPrice(price);
                        v.setCount(count);
                        v.setCurrency(c);
                        v.setMeasure(m);
                    }
                }
                if (v == null) {
                    v = new Variant(name, m, count, price, "", c);
                    realm.insertOrUpdate(v);
                }
                bid.item = v.getId();
            }
        });
        return bid.item;
    }

    public void setDefaultImage(String variantId , int image) {
        final Bundle1<String> bid = new Bundle1<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (variantId != null) {
                    Variant v = realm.where(Variant.class).equalTo("id", variantId).findFirst();
                    if (v != null) {
                        v.setDefaultImage(image);
                    }
                }
            }
        });
    }


    public void saveMainVariantToRecord(String variantId, String recordId) {
        Variant v = realm.where(Variant.class).equalTo("id", variantId).findFirst();
        Record  r = realm.where(Record.class ).equalTo("id", recordId ).findFirst();
        if (v != null && r != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    r.setMainVariant(v);
                }
            });
        }
    }

    //---------------- Labels ---------------------------------------------------------------
    public QueryLabels queryLabels() {
        return new QueryLabels();
    }

    public class QueryLabels {
        RealmQuery<Label> query;

        public QueryLabels () {
            query = realm.where(Label.class);
        }

        public QueryLabels id_equalTo(String id) {
            query = query.equalTo("id",id);
            return this;
        }

        public List<Label> findAll() {
            RealmResults<Label> rr = query.findAll();
            ArrayList<Label> al = new ArrayList<>();
            al.addAll(rr);
            return al;
        }

        public Label findFirst() {
            Label rr = query.findFirst();
            return rr;
        }
    }

    //---------------- Currency -------------------------------------------------------------
    public QueryCurrency queryCurrency() {
        return new QueryCurrency();
    }

    public class QueryCurrency {
        RealmQuery<Currency> query;

        public QueryCurrency () {
            query = realm.where(Currency.class);
        }

        public QueryCurrency iso_code_equalTo(int iso_code) {
            query = query.equalTo("iso_code_int",iso_code);
            return this;
        }

        public Currency getBase() {
            return query.equalTo("isBase",true).findFirst();
        }

        public QueryCurrency iso_code_equalTo(String iso_code) {
            query = query.equalTo("iso_code_str",iso_code);
            return this;
        }

        public List<Currency> findAll() {
            RealmResults<Currency> rr = query.findAll();
            ArrayList<Currency> al = new ArrayList<>();
            al.addAll(rr);
            return al;
        }

        public Currency findFirst() {
            Currency rr = query.findFirst();
            return rr;
        }
    }

    //---------------- Measure --------------------------------------------------------------
    public QueryMeasure queryMeasure() {
        return new QueryMeasure();
    }

    public class QueryMeasure {
        RealmQuery<Measure> query;

        public QueryMeasure () {
            query = realm.where(Measure.class);
        }

        public QueryMeasure hashEqualTo(int hash) {
            query = query.equalTo("hash",hash);
            return this;
        }

        public QueryMeasure systemEqualTo(int system) {
            query = query.equalTo("system",system);
            return this;
        }

        public QueryMeasure measureEqualTo(int measure) {
            query = query.equalTo("measure", measure);
            return this;
        }

        public QueryMeasure isDefault() {
            query = query.equalTo("def", true);
            return this;
        }

        public List<Measure> findAll() {
            RealmResults<Measure> rr = query.findAll();
            ArrayList<Measure> al = new ArrayList<>();
            al.addAll(rr);
            return al;
        }

        public Measure findFirst() {
            return query.findFirst();
        }
    }

    public int saveMeasure(Measure.Plain measure) {
        final Bundle1<Integer> id = new Bundle1<>();
        id.item = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (measure.hash != 0) {
                    Measure m = realm.where(Measure.class).equalTo("hash",measure.hash).findFirst();
                    if (m != null) {
                        m.update(measure);
                        id.item = measure.hash;
                    }
                } else {
                    Measure m = new Measure(measure, Settings.getMeasureSystem(context));
                    id.item = m.getHash();
                }
            }
        });
        return id.item;
    }

    public void deleteMeasure(int hash) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Measure m = realm.where(Measure.class).equalTo("hash",hash).findFirst();
                if (m != null)
                    m.deleteFromRealm();
            }
        });
    }

    public void setDefaultMeasure(int hash) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Measure m = realm.where(Measure.class).equalTo("hash",hash).findFirst();
                if (m != null) {
                    RealmResults<Measure> ms = realm.where(Measure.class)
                            .beginGroup()
                            .equalTo("system",m.getSystem())
                            .and()
                            .equalTo("measure",m.getMeasure())
                            .endGroup()
                            .findAll();
                    if (ms != null && ms.size() > 0)
                        for (Measure mm : ms)
                            if (mm.getHash() == m.getHash())
                                mm.setDef(true);
                            else
                                mm.setDef(false);
                }
            }
        });
    }

    //---------------- Measure --------------------------------------------------------------
    public QueryShop queryShop() {
        return new QueryShop();
    }

    public class QueryShop {
        RealmQuery<VariantInShop> query;

        public QueryShop () {
            query = realm.where(VariantInShop.class);
        }

        public QueryShop id_equalTo(String id) {
            query = query.equalTo("id",id);
            return this;
        }

        public List<VariantInShop> findAll() {
            RealmResults<VariantInShop> rr = query.findAll();
            ArrayList<VariantInShop> al = new ArrayList<>();
            al.addAll(rr);
            return al;
        }

        public VariantInShop findFirst() {
            return query.findFirst();
        }
    }

    public String saveShop(VariantInShop.Plain shop, String variantId, boolean asPrimary) {
        Currency c = realm.where(Currency.class).equalTo("iso_code_int", shop.currency.iso_code_int).findFirst();
        if (c == null)
            return null;

        final Bundle1<String> bid = new Bundle1<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                VariantInShop s;
                if (shop.id == null) {
                    Variant v = realm.where(Variant.class).equalTo("id", variantId).findFirst();
                    if (v == null)
                        return;

                    s = new VariantInShop(shop, c);
                    realm.insert(s);
                    if (asPrimary)
                        v.setPrimaryShop(s);
                    else
                        v.addShop(s);
                    int i = 1;

                } else {
                    s = realm.where(VariantInShop.class).equalTo("id", shop.id).findFirst();
                    if (s == null)
                        return;

                    s.setPrice(shop.price);
                    s.setCurrency(c);
                    s.setAddress(shop.address);
                    s.setComment(shop.comment);
                    s.setTitle(shop.title);
                    s.setURL(shop.url);
                }
                bid.item = s.getId();
            }
        });
        return bid.item;
    }

    public void setShopPrimary(String shopId, String variantId) {
        Variant v = realm.where(Variant.class).equalTo("id", variantId).findFirst();
        final VariantInShop s = realm.where(VariantInShop.class).equalTo("id", shopId).findFirst();
        if (v == null || s == null)
            return;

        final Bundle1<String> bid = new Bundle1<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                VariantInShop oldPrimaryShop = v.getPrimaryShop();
                v.setPrimaryShop(s);
                v.deleteShop(s);
                v.addShop(oldPrimaryShop);
            }
        });
    }

    public void setBasicVariant(String recordId, String variantId) {
        Record r = realm.where(Record.class).equalTo("id", recordId).findFirst();
        Variant v = realm.where(Variant.class).equalTo("id", variantId).findFirst();
        if (v == null || r == null)
            return;

        final Bundle1<String> bid = new Bundle1<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Variant oldBasicVariant = r.getMainVariant();
                r.setMainVariant(v);
                r.deleteVariant(v);
                r.addVariant(oldBasicVariant);
            }


        });

        int i = 1;
    }

    public void deleteShop(String id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                VariantInShop s = realm.where(VariantInShop.class).equalTo("id", id).findFirst();
                if (s != null) {
                    //s.getVariant().
                    s.deleteFromRealm();
                }
            }
        });
    }

    public void deleteVariant(String id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Variant v = realm.where(Variant.class).equalTo("id", id).findFirst();
                if (v != null) {
                    //s.getVariant().
                    v.deleteFromRealm();
                }
            }
        });
    }

    public boolean attachVariantToRecord(String variantId, String recordId) {
        Variant v = realm.where(Variant.class).equalTo("id", variantId).findFirst();
        Record r = realm.where(Record.class).equalTo("id", recordId).findFirst();
        if (v == null || r == null)
            return false;

        final Bundle1<Boolean> bid = new Bundle1<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                bid.item = r.addVariant(v);
            }
        });

        return bid.item;
    }

}
