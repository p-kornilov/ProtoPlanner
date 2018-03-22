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
/*
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
*/

                realm.insertOrUpdate(new Currency("AED",784,R.string.AED,contetx.getResources().getString(R.string.AED_symbol),3,false,0));
                realm.insertOrUpdate(new Currency("AFN",971,R.string.AFN,contetx.getResources().getString(R.string.AFN_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("ALL",8,R.string.ALL,contetx.getResources().getString(R.string.ALL_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("AMD",51,R.string.AMD,contetx.getResources().getString(R.string.AMD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("ANG",532,R.string.ANG,contetx.getResources().getString(R.string.ANG_symbol),2,false,0));
                realm.insertOrUpdate(new Currency("AOA",973,R.string.AOA,contetx.getResources().getString(R.string.AOA_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("ARS",32,R.string.ARS,contetx.getResources().getString(R.string.ARS_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("AUD",36,R.string.AUD,contetx.getResources().getString(R.string.AUD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("AWG",533,R.string.AWG,contetx.getResources().getString(R.string.AWG_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("AZN",944,R.string.AZN,contetx.getResources().getString(R.string.AZN_symbol),3,false,0));
                realm.insertOrUpdate(new Currency("BAM",977,R.string.BAM,contetx.getResources().getString(R.string.BAM_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("BBD",52,R.string.BBD,contetx.getResources().getString(R.string.BBD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("BDT",50,R.string.BDT,contetx.getResources().getString(R.string.BDT_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("BGN",975,R.string.BGN,contetx.getResources().getString(R.string.BGN_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("BHD",48,R.string.BHD,contetx.getResources().getString(R.string.BHD_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("BIF",108,R.string.BIF,contetx.getResources().getString(R.string.BIF_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("BMD",60,R.string.BMD,contetx.getResources().getString(R.string.BMD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("BND",96,R.string.BND,contetx.getResources().getString(R.string.BND_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("BRL",986,R.string.BRL,contetx.getResources().getString(R.string.BRL_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("BSD",44,R.string.BSD,contetx.getResources().getString(R.string.BSD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("BTN",64,R.string.BTN,contetx.getResources().getString(R.string.BTN_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("BWP",72,R.string.BWP,contetx.getResources().getString(R.string.BWP_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("BYN",933,R.string.BYN,contetx.getResources().getString(R.string.BYN_symbol),3,false,0));
                realm.insertOrUpdate(new Currency("BZD",84,R.string.BZD,contetx.getResources().getString(R.string.BZD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("CAD",124,R.string.CAD,contetx.getResources().getString(R.string.CAD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("CHF",756,R.string.CHF,contetx.getResources().getString(R.string.CHF_symbol),1,false,1));
                realm.insertOrUpdate(new Currency("CLP",152,R.string.CLP,contetx.getResources().getString(R.string.CLP_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("CNY",156,R.string.CNY,contetx.getResources().getString(R.string.CNY_symbol),1,false,1));
                realm.insertOrUpdate(new Currency("COP",170,R.string.COP,contetx.getResources().getString(R.string.COP_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("CRC",188,R.string.CRC,contetx.getResources().getString(R.string.CRC_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("CUP",192,R.string.CUP,contetx.getResources().getString(R.string.CUP_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("CVE",132,R.string.CVE,contetx.getResources().getString(R.string.CVE_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("CZK",203,R.string.CZK,contetx.getResources().getString(R.string.CZK_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("DKK",208,R.string.DKK,contetx.getResources().getString(R.string.DKK_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("DOP",214,R.string.DOP,contetx.getResources().getString(R.string.DOP_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("DZD",12,R.string.DZD,contetx.getResources().getString(R.string.DZD_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("EGP",818,R.string.EGP,contetx.getResources().getString(R.string.EGP_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("ERN",232,R.string.ERN,contetx.getResources().getString(R.string.ERN_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("ETB",230,R.string.ETB,contetx.getResources().getString(R.string.ETB_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("EUR",978,R.string.EUR,contetx.getResources().getString(R.string.EUR_symbol),0,false,10));
                realm.insertOrUpdate(new Currency("FJD",242,R.string.FJD,contetx.getResources().getString(R.string.FJD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("FKP",238,R.string.FKP,contetx.getResources().getString(R.string.FKP_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("GBP",826,R.string.GBP,contetx.getResources().getString(R.string.GBP_symbol),1,false,1));
                realm.insertOrUpdate(new Currency("GEL",981,R.string.GEL,contetx.getResources().getString(R.string.GEL_symbol),3,false,0));
                realm.insertOrUpdate(new Currency("GHS",936,R.string.GHS,contetx.getResources().getString(R.string.GHS_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("GIP",292,R.string.GIP,contetx.getResources().getString(R.string.GIP_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("GMD",270,R.string.GMD,contetx.getResources().getString(R.string.GMD_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("GYD",328,R.string.GYD,contetx.getResources().getString(R.string.GYD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("HKD",344,R.string.HKD,contetx.getResources().getString(R.string.HKD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("HNL",340,R.string.HNL,contetx.getResources().getString(R.string.HNL_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("HRK",191,R.string.HRK,contetx.getResources().getString(R.string.HRK_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("HTG",332,R.string.HTG,contetx.getResources().getString(R.string.HTG_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("HUF",348,R.string.HUF,contetx.getResources().getString(R.string.HUF_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("IDR",360,R.string.IDR,contetx.getResources().getString(R.string.IDR_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("ILS",376,R.string.ILS,contetx.getResources().getString(R.string.ILS_symbol),1,false,1));
                realm.insertOrUpdate(new Currency("INR",356,R.string.INR,contetx.getResources().getString(R.string.INR_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("IQD",368,R.string.IQD,contetx.getResources().getString(R.string.IQD_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("IRR",364,R.string.IRR,contetx.getResources().getString(R.string.IRR_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("ISK",352,R.string.ISK,contetx.getResources().getString(R.string.ISK_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("JMD",388,R.string.JMD,contetx.getResources().getString(R.string.JMD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("JOD",400,R.string.JOD,contetx.getResources().getString(R.string.JOD_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("JPY",392,R.string.JPY,contetx.getResources().getString(R.string.JPY_symbol),1,false,1));
                realm.insertOrUpdate(new Currency("KES",404,R.string.KES,contetx.getResources().getString(R.string.KES_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("KGS",417,R.string.KGS,contetx.getResources().getString(R.string.KGS_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("KHR",116,R.string.KHR,contetx.getResources().getString(R.string.KHR_symbol),3,false,0));
                realm.insertOrUpdate(new Currency("KPW",408,R.string.KPW,contetx.getResources().getString(R.string.KPW_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("KRW",410,R.string.KRW,contetx.getResources().getString(R.string.KRW_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("KYD",136,R.string.KYD,contetx.getResources().getString(R.string.KYD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("KZT",398,R.string.KZT,contetx.getResources().getString(R.string.KZT_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("LAK",418,R.string.LAK,contetx.getResources().getString(R.string.LAK_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("LBP",422,R.string.LBP,contetx.getResources().getString(R.string.LBP_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("LKR",144,R.string.LKR,contetx.getResources().getString(R.string.LKR_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("LTL",440,R.string.LTL,contetx.getResources().getString(R.string.LTL_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("LYD",434,R.string.LYD,contetx.getResources().getString(R.string.LYD_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("MAD",504,R.string.MAD,contetx.getResources().getString(R.string.MAD_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("MDL",498,R.string.MDL,contetx.getResources().getString(R.string.MDL_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("MNT",496,R.string.MNT,contetx.getResources().getString(R.string.MNT_symbol),3,false,0));
                realm.insertOrUpdate(new Currency("MOP",446,R.string.MOP,contetx.getResources().getString(R.string.MOP_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("MUR",480,R.string.MUR,contetx.getResources().getString(R.string.MUR_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("MXN",484,R.string.MXN,contetx.getResources().getString(R.string.MXN_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("NAD",516,R.string.NAD,contetx.getResources().getString(R.string.NAD_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("NGN",566,R.string.NGN,contetx.getResources().getString(R.string.NGN_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("NIO",558,R.string.NIO,contetx.getResources().getString(R.string.NIO_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("NOK",578,R.string.NOK,contetx.getResources().getString(R.string.NOK_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("NZD",554,R.string.NZD,contetx.getResources().getString(R.string.NZD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("OMR",512,R.string.OMR,contetx.getResources().getString(R.string.OMR_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("PHP",608,R.string.PHP,contetx.getResources().getString(R.string.PHP_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("PKR",586,R.string.PKR,contetx.getResources().getString(R.string.PKR_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("PLN",985,R.string.PLN,contetx.getResources().getString(R.string.PLN_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("PYG",600,R.string.PYG,contetx.getResources().getString(R.string.PYG_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("QAR",634,R.string.QAR,contetx.getResources().getString(R.string.QAR_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("RSD",941,R.string.RSD,contetx.getResources().getString(R.string.RSD_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("RUB",643,R.string.RUB,contetx.getResources().getString(R.string.RUB_symbol),4,false,10));
                realm.insertOrUpdate(new Currency("SAR",682,R.string.SAR,contetx.getResources().getString(R.string.SAR_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("SBD",90,R.string.SBD,contetx.getResources().getString(R.string.SBD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("SCR",690,R.string.SCR,contetx.getResources().getString(R.string.SCR_symbol),0,false,0));
                realm.insertOrUpdate(new Currency("SEK",752,R.string.SEK,contetx.getResources().getString(R.string.SEK_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("SGD",702,R.string.SGD,contetx.getResources().getString(R.string.SGD_symbol),1,false,1));
                realm.insertOrUpdate(new Currency("THB",764,R.string.THB,contetx.getResources().getString(R.string.THB_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("TMT",934,R.string.TMT,contetx.getResources().getString(R.string.TMT_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("TRY",949,R.string.TRY,contetx.getResources().getString(R.string.TRY_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("TTD",780,R.string.TTD,contetx.getResources().getString(R.string.TTD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("TWD",901,R.string.TWD,contetx.getResources().getString(R.string.TWD_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("TZS",834,R.string.TZS,contetx.getResources().getString(R.string.TZS_symbol),4,false,0));
                realm.insertOrUpdate(new Currency("UAH",980,R.string.UAH,contetx.getResources().getString(R.string.UAH_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("USD",840,R.string.USD,contetx.getResources().getString(R.string.USD_symbol),1,false,10));
                realm.insertOrUpdate(new Currency("UYU",858,R.string.UYU,contetx.getResources().getString(R.string.UYU_symbol),1,false,0));
                realm.insertOrUpdate(new Currency("VND",704,R.string.VND,contetx.getResources().getString(R.string.VND_symbol),4,false,0));





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
                        //realm.insertOrUpdate(cur);
                    } else {
                        //cur.setExchange_rate(ExchangeRates.getTestRate(), 643);
                        cur.setExchange_rate(ExchangeRates.getTestRate());
                        //realm.insertOrUpdate(cur);
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
