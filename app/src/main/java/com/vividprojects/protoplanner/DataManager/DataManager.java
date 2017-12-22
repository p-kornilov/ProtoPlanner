package com.vividprojects.protoplanner.DataManager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vividprojects.protoplanner.CoreData.Block;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Smile on 05.12.2017.
 */

public class DataManager {
    private Realm realm;
    private Context context;

    public DataManager(Context context){
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();
        this.context = context;
    }

    public DataManager(){};

    public int getHeight() {return context.getResources().getConfiguration().screenHeightDp;}

    public Context getContext() {return context;}

    public void setContext(Context context) {
        this.context = context;
    }

    public String getType() {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        if (isTablet) return "This is tablet";
        else return "This is phone";
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
                Measure temp = new Measure("шт.");
                realm.insertOrUpdate(temp);
                temp = new Measure("кг.");
                realm.insertOrUpdate(temp);
                temp = new Measure("м.");
                realm.insertOrUpdate(temp);
                temp = new Measure("кв.м.");
                realm.insertOrUpdate(temp);
                temp = new Measure("л.");
                realm.insertOrUpdate(temp);

                realm.insertOrUpdate(new Label("Red",1,"",null));
                realm.insertOrUpdate(new Label("Blue",2,"",null));
                realm.insertOrUpdate(new Label("Green",3,"",null));
                realm.insertOrUpdate(new Label("White",4,"",null));
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Measure m = realm.where(Measure.class).equalTo("title","шт.").findFirst();
                Variant v = new Variant("Торт",m,7,100, "Мой торт");
                Variant v2 = new Variant("Колбаса",m,3,50, "");
                Variant v3 = new Variant("Хлеб",m,5,60, "");
                Variant v4 = new Variant("Фильтр для воды",m,5,60, "");
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

                for (VariantInShop vis : vsps) {
                    vv1.addShop(vis);
                }

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
                realm.insertOrUpdate(new Record(vv1));
                realm.insertOrUpdate(new Record(vv2));

                Record r = realm.where(Record.class).findFirst();
                r.addVariant(vv2);


                RealmResults<Label> ls = realm.where(Label.class).findAll();
                for (Label l : ls) {
                    r.addLabel(l);
                }

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

        Log.d("Test", "------------------------------ Labels:");
        RealmResults<Label> ls = realm.where(Label.class).findAll();
        for (Label l : ls) {
            Log.d("Test", l.toString());
        }*/

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

    public List<Record> getRecords() {
        return new ArrayList<Record>();
    }

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

    }
}

