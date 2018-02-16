package com.vividprojects.protoplanner.DB;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.RelativeLayout;

import com.vividprojects.protoplanner.CoreData.Block;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

//import io.realm.CurrencyRealmProxy;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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
                Currency c = new Currency("RUB",643,"Russian Ruble",contetx.getResources().getString(R.string.RUB),Currency.AFTER);
                realm.insertOrUpdate(c);
                Log.d("Test", "+++++++++++++++++++++++ "+c);
                c = new Currency("USD",840,"US Dollar",contetx.getResources().getString(R.string.USD),Currency.BEFORE);
                realm.insertOrUpdate(c);
                c = new Currency("EUR",978,"Euro",contetx.getResources().getString(R.string.EUR),Currency.BEFORE);
                realm.insertOrUpdate(c);
                c = new Currency("XAF",950,"Franc",contetx.getResources().getString(R.string.XAF),Currency.WITHIN);
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


                Label l1 = new Label("Ltgray", contetx.getResources().getColor(R.color.Red),"",null);
                Label l2 = new Label("Ltgray", contetx.getResources().getColor(R.color.Pink),"",null);
                Label l3 = new Label("Ltgray", contetx.getResources().getColor(R.color.Purple),"",null);
                Label l4 = new Label("Ltgray", contetx.getResources().getColor(R.color.Deep_Purple),"",null);
                Label l5 = new Label("Ltgray", contetx.getResources().getColor(R.color.Indigo),"",null);
                Label l6 = new Label("Ltgray", contetx.getResources().getColor(R.color.Blue),"",null);
                Label l7 = new Label("Ltgray", contetx.getResources().getColor(R.color.Light_Blue),"",null);
                Label l8 = new Label("Ltgray", contetx.getResources().getColor(R.color.Cyan),"",null);
                Label l9 = new Label("Ltgray", contetx.getResources().getColor(R.color.Teal),"",null);
                Label l10 = new Label("Ltgray", contetx.getResources().getColor(R.color.Green),"",null);
                Label l11 = new Label("Ltgray", contetx.getResources().getColor(R.color.Light_Green),"",null);
                Label l12 = new Label("Ltgray", contetx.getResources().getColor(R.color.Lime),"",null);
                Label l13 = new Label("Ltgray", contetx.getResources().getColor(R.color.Yellow),"",null);
                Label l14 = new Label("Ltgray", contetx.getResources().getColor(R.color.Amber),"",null);
                Label l15 = new Label("Ltgray", contetx.getResources().getColor(R.color.Orange),"",null);
                Label l16 = new Label("Ltgray", contetx.getResources().getColor(R.color.Deep_Orange),"",null);
                Label l17 = new Label("Ltgray", contetx.getResources().getColor(R.color.Brown),"",null);
                Label l18 = new Label("Ltgray", contetx.getResources().getColor(R.color.Grey),"",null);
                Label l19 = new Label("Ltgray", contetx.getResources().getColor(R.color.Blue_Grey),"",null);
                Label l20 = new Label("Ltgray", contetx.getResources().getColor(R.color.Black),"",null);

                realm.insertOrUpdate(l1);
                realm.insertOrUpdate(l2);
                realm.insertOrUpdate(l3);
                realm.insertOrUpdate(l4);
                realm.insertOrUpdate(l5);
                realm.insertOrUpdate(l6);
                realm.insertOrUpdate(l7);
                realm.insertOrUpdate(l8);
                realm.insertOrUpdate(l9);
                realm.insertOrUpdate(l10);
                realm.insertOrUpdate(l11);
                realm.insertOrUpdate(l12);
                realm.insertOrUpdate(l13);
                realm.insertOrUpdate(l14);
                realm.insertOrUpdate(l15);
                realm.insertOrUpdate(l16);
                realm.insertOrUpdate(l17);
                realm.insertOrUpdate(l18);
                realm.insertOrUpdate(l19);
                realm.insertOrUpdate(l20);

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

        Log.d("Test", "------------------------------ onCreate - DB done");
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
                v.deleteFromRealm();
            }
        });
    }

    public void addImageToVariant(String variant, String image) {
        final Integer result = 0;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Variant v = realm.where(Variant.class).contains("title",variant).findFirst();
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
                r.setLebels(labels);
            }
        });
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

}
