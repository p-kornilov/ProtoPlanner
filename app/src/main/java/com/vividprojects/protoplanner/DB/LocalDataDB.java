package com.vividprojects.protoplanner.DB;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.RelativeLayout;

import com.vividprojects.protoplanner.API.ExchangeRates;
import com.vividprojects.protoplanner.CoreData.Block;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Exchange;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Bundle1;
import com.vividprojects.protoplanner.Widgets.Pallet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

//import io.realm.CurrencyRealmProxy;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by p.kornilov on 25.12.2017.
 */

@Singleton
public class LocalDataDB {
    private Realm realm;
    private Context contetx;

    @Inject
    public LocalDataDB(Context context){
        this.contetx = context;
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();
    }

    public void initDB(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Currency c = new Currency("RUB",643,R.string.RUB,contetx.getResources().getString(R.string.RUB_symbol),Currency.AFTER_SPACE,true);
                realm.insertOrUpdate(c);
                Log.d("Test", "+++++++++++++++++++++++ "+c);
                c = new Currency("USD",840,R.string.USD,contetx.getResources().getString(R.string.USD_symbol),Currency.BEFORE,true);
                c.setSorting_weight(10);
                realm.insertOrUpdate(c);
                c = new Currency("EUR",978,R.string.EUR,contetx.getResources().getString(R.string.EUR_symbol),Currency.BEFORE,false);
                c.setSorting_weight(10);
                realm.insertOrUpdate(c);
                c = new Currency("XAF",950,R.string.XAF,contetx.getResources().getString(R.string.XAF_symbol),Currency.WITHIN,false);
                realm.insertOrUpdate(c);
                c = new Currency("ANG",532,R.string.ANG,contetx.getResources().getString(R.string.ANG_symbol),Currency.BEFORE_SPACE,false);
                realm.insertOrUpdate(c);
                c = new Currency("RUB",1,"Test 1",contetx.getResources().getString(R.string.RUB_symbol),Currency.AFTER_SPACE,false);
                realm.insertOrUpdate(c);
                Log.d("Test", "+++++++++++++++++++++++ "+c);
                c = new Currency("USD",2,"Test 2",contetx.getResources().getString(R.string.USD_symbol),Currency.BEFORE,false);
                realm.insertOrUpdate(c);
                c = new Currency("EUR",3,"Test 3",contetx.getResources().getString(R.string.EUR_symbol),Currency.BEFORE,false);
                realm.insertOrUpdate(c);
                c = new Currency("XAF",4,"Test 4",contetx.getResources().getString(R.string.XAF_symbol),Currency.WITHIN,false);
                realm.insertOrUpdate(c);
                c = new Currency("ANG",5,"Test 5",contetx.getResources().getString(R.string.ANG_symbol),Currency.BEFORE,false);
                realm.insertOrUpdate(c);
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Measure temp = new Measure("шт.",Measure.ENTIRE);
                realm.insertOrUpdate(temp);
                temp = new Measure("кг.",Measure.FRACTIONAL);
                realm.insertOrUpdate(temp);
                temp = new Measure("м.",Measure.FRACTIONAL);
                realm.insertOrUpdate(temp);
                temp = new Measure("кв.м.",Measure.FRACTIONAL);
                realm.insertOrUpdate(temp);
                temp = new Measure("л.",Measure.FRACTIONAL);
                realm.insertOrUpdate(temp);

/*                realm.insertOrUpdate(new Label("Red",1,"",null));
                realm.insertOrUpdate(new Label("Blue",2,"",null));
                realm.insertOrUpdate(new Label("Green",3,"",null));
                realm.insertOrUpdate(new Label("White",4,"",null));*/
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Measure m = realm.where(Measure.class).equalTo("title","шт.").findFirst();
                Measure m1 = realm.where(Measure.class).equalTo("title","кг.").findFirst();
                Currency cr = realm.where(Currency.class).equalTo("iso_code_str","RUB").findFirst();
                Currency cf = realm.where(Currency.class).equalTo("iso_code_str","XAF").findFirst();
                Currency cd = realm.where(Currency.class).equalTo("iso_code_str","USD").findFirst();
                Variant v = new Variant("Торт",m,7,100, "Мой торт и ссылка http://test.com",cr);
                Variant v2 = new Variant("Колбаса",m,3,50, "",cd);
                Variant v3 = new Variant("Хлеб",m1,5.01,60, "",cf);
                Variant v4 = new Variant("Фильтр для воды",m,5,60, "",cr);
                v4.addUrl("https://test.com");
                realm.insertOrUpdate(v);
                realm.insertOrUpdate(v2);
                realm.insertOrUpdate(v3);
                realm.insertOrUpdate(v4);

                realm.insertOrUpdate(new VariantInShop("Первый магазин","https://shop.ru df gsdgf s df gsdf gsdg f","Адрес первого магазина","Комментарий для первого магазина", 91.0));
                realm.insertOrUpdate(new VariantInShop("Второй магазин","https://shop2.ru","Адрес второго магазина","Комментарий для второго магазина", 101.0));
                realm.insertOrUpdate(new VariantInShop("Третий магазин","https://shop3.ru","Адрес третьего магазина","Комментарий для третьего магазина", 103.0));
                realm.insertOrUpdate(new VariantInShop("Четвертый магазин","https://shop4.ru","Адрес четвертого магазина","Комментарий для четвертого магазина hj hkhjk ghj ghj ghj", 104.0));

                RealmResults<VariantInShop> vsps = realm.where(VariantInShop.class).findAll();
                Variant vv1 = realm.where(Variant.class).contains("title","Фильтр для воды").findFirst();
                Variant vv2 = realm.where(Variant.class).contains("title","Хлеб").findFirst();
                Variant vv3 = realm.where(Variant.class).contains("title","Колбаса").findFirst();

                vv1.addImage("c3c59002-5a86-3c7e-b7ed-93f2c79255de");
                vv1.addImage("c3c59002-5a86-3c7e-b7ed-93f2c79255de");
                vv1.addImage("89d4f6cd-1f3b-382e-b4cd-38602710fc74");
                vv1.addImage("c3c59002-5a86-3c7e-b7ed-93f2c79255de");

                for (VariantInShop vis : vsps) {
                    vv1.addShop(vis);
                }

                Record r2 = new Record(vv1);
                r2.setName("Фильтр");
                r2.setComment("Комментарий для записи со ссылкой http://test.com");
                r2.addLabel(new Label("Green", Color.GREEN,"",null));
                r2.addLabel(new Label("Yellow", Color.YELLOW,"",null));
                r2.addLabel(new Label("Blue", Color.BLUE,"",null));
                r2.addLabel(new Label("Red", Color.RED,"",null));
                r2.addLabel(new Label("Magenta", Color.MAGENTA,"",null));
                realm.insertOrUpdate(r2);
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv2));
                realm.insertOrUpdate(new Record(vv3));

                int colorCount = Pallet.getColors().size();

                for (int i=0; i<colorCount; i++) {
                    realm.insertOrUpdate(new Label(Pallet.getNameColors().get(i), Pallet.getColors().get(i),"",null));
                }

                Record r = realm.where(Record.class).findFirst();
                r.addVariant(vv2);


/*                RealmResults<Label> ls = realm.where(Label.class).findAll();
                for (Label l : ls) {
                    r.addLabel(l);
                }*/

                realm.insertOrUpdate(new Block("Блокнот 1",Block.PRIORITY_OFF));
                Block b = realm.where(Block.class).contains("name","Блокнот 1").findFirst();
                b.addRecord(r);

            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Currency> crc = realm.where(Currency.class).findAll();

                for (Currency cur : crc) {
                    if (cur.getIso_code_int() == 643) {
                        //cur.setExchange_rate(1, 643);
                        cur.setExchange_rate(1);
                        cur.setIsBase(true);
                        realm.insertOrUpdate(cur);
                    } else {
                        //cur.setExchange_rate(ExchangeRates.getTestRate(), 643);
                        cur.setExchange_rate(ExchangeRates.getTestRate());
                        realm.insertOrUpdate(cur);
                    }
                }
            }
        });


        Log.d("Test", "------------------------------ onCreate - DB done");
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
                    realm.insertOrUpdate(editLabel);
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
                    realm.insertOrUpdate(record);
                }
            }
        });
        return name;
    }

    public void showDB(){
        /*        String measures = "[";

        RealmResults<Measure> ms = realm.where(Measure.class).findAll();
        for (Measure m : ms) {
            measures += m.getTitle() + ";";
        }

        measures = measures.substring(0,measures.length()-1)+"]";

        Log.d("Test", "------------------------------ "+measures);

        Log.d("Test", "------------------------------ Variants:");

        RealmResults<Variant> vs = realm.where(Variant.class).findAll();
        for (Variant v : vs) {
            Log.d("Test", v.getTitle());
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
                Variant v = realm.where(Variant.class).contains("title",variant).findFirst();
                if (v != null)
                    v.addImage(image);
            }
        });
    }

    public void saveLabelsForRecord(String recordId, String[] ids) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Record r = realm.where(Record.class).contains("id",recordId).findFirst();
                RealmResults<Label> labels = realm.where(Label.class).in("id",ids).findAll();
                if (r != null && labels != null)
                    r.setLebels(labels);
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

    public QueryRecords queryRecords() {
        return new QueryRecords();
    }
    public QueryVariants queryVariants() {
        return new QueryVariants();
    }
    public QueryLabels queryLabels() {
        return new QueryLabels();
    }
    public QueryCurrency queryCurrency() {
        return new QueryCurrency();
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
            query = query.equalTo("mainVariant.title",title);
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

    public class QueryVariants {
        RealmQuery<Variant> query;

        public QueryVariants () {
            query = realm.where(Variant.class);
        }

        public QueryVariants title_equalTo(String title) {
            query = query.equalTo("title",title);
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

}
