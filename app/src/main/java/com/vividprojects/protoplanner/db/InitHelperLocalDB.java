package com.vividprojects.protoplanner.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.vividprojects.protoplanner.api.ExchangeRates;
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Measure;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.coredata.VariantInShop;
import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.images.BitmapUtils;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.widgets.Pallet;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmResults;

public class InitHelperLocalDB {
    public static void createDefFile(Context context, String imagesDirectory, String fileName, int resource) {
        String full_name = imagesDirectory + DataRepository.IMAGES_FULL + fileName + ".jpg";
        String thumb_name = imagesDirectory + DataRepository.IMAGES_SMALL + fileName + ".jpg";

        File file = new File(full_name);
        if(!file.exists()) {
            Bitmap b = BitmapFactory.decodeResource(context.getResources(), resource);
            BitmapUtils.saveImage(context, b, full_name, false);
            BitmapUtils.saveImage(context, BitmapUtils.resamplePic(context, full_name, 256, 256), thumb_name, false);
        }
    }

    public static void init(Realm realm, Context context, String imagesDirectory) {
        int i = 1;
        //--------------- init images -----------------------------
        createDefFile(context, imagesDirectory, "00000000-def1-0000-0000-000000000000", R.raw.def1);
        createDefFile(context, imagesDirectory, "00000000-def2-0000-0000-000000000000", R.raw.def2);
        createDefFile(context, imagesDirectory, "00000000-def3-0000-0000-000000000000", R.raw.def3);
        createDefFile(context, imagesDirectory, "00000000-def4-0000-0000-000000000000", R.raw.def4);
        createDefFile(context, imagesDirectory, "00000000-def5-0000-0000-000000000000", R.raw.def5);
        createDefFile(context, imagesDirectory, DataRepository.IMAGE_DEFAULT_ALTERNATIVE, R.drawable.box3);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(new Currency("AED",784, R.string.AED, context.getResources().getString(R.string.AED_symbol),3,false,0,R.drawable.flag_aed));
                realm.insertOrUpdate(new Currency("AFN",971,R.string.AFN, context.getResources().getString(R.string.AFN_symbol),0,false,0,R.drawable.flag_afn));
                realm.insertOrUpdate(new Currency("ALL",8,R.string.ALL, context.getResources().getString(R.string.ALL_symbol),4,false,0,R.drawable.flag_all));
                realm.insertOrUpdate(new Currency("AMD",51,R.string.AMD, context.getResources().getString(R.string.AMD_symbol),1,false,0,R.drawable.flag_amd));
                realm.insertOrUpdate(new Currency("ANG",532,R.string.ANG, context.getResources().getString(R.string.ANG_symbol),2,false,0,R.drawable.flag_ang));
                realm.insertOrUpdate(new Currency("AOA",973,R.string.AOA, context.getResources().getString(R.string.AOA_symbol),4,false,0,R.drawable.flag_aoa));
                realm.insertOrUpdate(new Currency("ARS",32,R.string.ARS, context.getResources().getString(R.string.ARS_symbol),1,false,0,R.drawable.flag_ars));
                realm.insertOrUpdate(new Currency("AUD",36,R.string.AUD, context.getResources().getString(R.string.AUD_symbol),1,false,0,R.drawable.flag_aud));
                realm.insertOrUpdate(new Currency("AWG",533,R.string.AWG, context.getResources().getString(R.string.AWG_symbol),1,false,0,R.drawable.flag_awg));
                realm.insertOrUpdate(new Currency("AZN",944,R.string.AZN, context.getResources().getString(R.string.AZN_symbol),3,false,0,R.drawable.flag_azn));
                realm.insertOrUpdate(new Currency("BAM",977,R.string.BAM, context.getResources().getString(R.string.BAM_symbol),0,false,0,R.drawable.flag_bam));
                realm.insertOrUpdate(new Currency("BBD",52,R.string.BBD, context.getResources().getString(R.string.BBD_symbol),1,false,0,R.drawable.flag_bbd));
                realm.insertOrUpdate(new Currency("BDT",50,R.string.BDT, context.getResources().getString(R.string.BDT_symbol),0,false,0,R.drawable.flag_bdt));
                realm.insertOrUpdate(new Currency("BGN",975,R.string.BGN, context.getResources().getString(R.string.BGN_symbol),4,false,0,R.drawable.flag_bgn));
                realm.insertOrUpdate(new Currency("BHD",48,R.string.BHD, context.getResources().getString(R.string.BHD_symbol),0,false,0,R.drawable.flag_bhd));
                realm.insertOrUpdate(new Currency("BIF",108,R.string.BIF, context.getResources().getString(R.string.BIF_symbol),0,false,0,R.drawable.flag_bif));
                realm.insertOrUpdate(new Currency("BMD",60,R.string.BMD, context.getResources().getString(R.string.BMD_symbol),1,false,0,R.drawable.flag_bmd));
                realm.insertOrUpdate(new Currency("BND",96,R.string.BND, context.getResources().getString(R.string.BND_symbol),1,false,0,R.drawable.flag_bnd));
                realm.insertOrUpdate(new Currency("BRL",986,R.string.BRL, context.getResources().getString(R.string.BRL_symbol),1,false,0,R.drawable.flag_brl));
                realm.insertOrUpdate(new Currency("BSD",44,R.string.BSD, context.getResources().getString(R.string.BSD_symbol),1,false,0,R.drawable.flag_bsd));
                realm.insertOrUpdate(new Currency("BTN",64,R.string.BTN, context.getResources().getString(R.string.BTN_symbol),0,false,0,R.drawable.flag_btn));
                realm.insertOrUpdate(new Currency("BWP",72,R.string.BWP, context.getResources().getString(R.string.BWP_symbol),0,false,0,R.drawable.flag_bwp));
                realm.insertOrUpdate(new Currency("BYN",933,R.string.BYN, context.getResources().getString(R.string.BYN_symbol),3,false,0,R.drawable.flag_byn));
                realm.insertOrUpdate(new Currency("BZD",84,R.string.BZD, context.getResources().getString(R.string.BZD_symbol),1,false,0,R.drawable.flag_bzd));
                realm.insertOrUpdate(new Currency("CAD",124,R.string.CAD, context.getResources().getString(R.string.CAD_symbol),1,false,0,R.drawable.flag_cad));
                realm.insertOrUpdate(new Currency("CHF",756,R.string.CHF, context.getResources().getString(R.string.CHF_symbol),1,false,1,R.drawable.flag_chf));
                realm.insertOrUpdate(new Currency("CLP",152,R.string.CLP, context.getResources().getString(R.string.CLP_symbol),1,false,0,R.drawable.flag_clp));
                realm.insertOrUpdate(new Currency("CNY",156,R.string.CNY, context.getResources().getString(R.string.CNY_symbol),1,false,1,R.drawable.flag_cny));
                realm.insertOrUpdate(new Currency("COP",170,R.string.COP, context.getResources().getString(R.string.COP_symbol),1,false,0,R.drawable.flag_cop));
                realm.insertOrUpdate(new Currency("CRC",188,R.string.CRC, context.getResources().getString(R.string.CRC_symbol),1,false,0,R.drawable.flag_crc));
                realm.insertOrUpdate(new Currency("CUP",192,R.string.CUP, context.getResources().getString(R.string.CUP_symbol),1,false,0,R.drawable.flag_cup));
                realm.insertOrUpdate(new Currency("CVE",132,R.string.CVE, context.getResources().getString(R.string.CVE_symbol),1,false,0,R.drawable.flag_cve));
                realm.insertOrUpdate(new Currency("CZK",203,R.string.CZK, context.getResources().getString(R.string.CZK_symbol),4,false,0,R.drawable.flag_czk));
                realm.insertOrUpdate(new Currency("DKK",208,R.string.DKK, context.getResources().getString(R.string.DKK_symbol),0,false,0,R.drawable.flag_dkk));
                realm.insertOrUpdate(new Currency("DOP",214,R.string.DOP, context.getResources().getString(R.string.DOP_symbol),1,false,0,R.drawable.flag_dop));
                realm.insertOrUpdate(new Currency("DZD",12,R.string.DZD, context.getResources().getString(R.string.DZD_symbol),0,false,0,R.drawable.flag_dzd));
                realm.insertOrUpdate(new Currency("EGP",818,R.string.EGP, context.getResources().getString(R.string.EGP_symbol),1,false,0,R.drawable.flag_egp));
                realm.insertOrUpdate(new Currency("ERN",232,R.string.ERN, context.getResources().getString(R.string.ERN_symbol),0,false,0,R.drawable.flag_ern));
                realm.insertOrUpdate(new Currency("ETB",230,R.string.ETB, context.getResources().getString(R.string.ETB_symbol),0,false,0,R.drawable.flag_etb));
                realm.insertOrUpdate(new Currency("EUR",978,R.string.EUR, context.getResources().getString(R.string.EUR_symbol),0,false,10,R.drawable.flag_eur));
                realm.insertOrUpdate(new Currency("FJD",242,R.string.FJD, context.getResources().getString(R.string.FJD_symbol),1,false,0,R.drawable.flag_fjd));
                realm.insertOrUpdate(new Currency("FKP",238,R.string.FKP, context.getResources().getString(R.string.FKP_symbol),1,false,0,R.drawable.flag_fkp));
                realm.insertOrUpdate(new Currency("GBP",826,R.string.GBP, context.getResources().getString(R.string.GBP_symbol),1,false,1,R.drawable.flag_gbp));
                realm.insertOrUpdate(new Currency("GEL",981,R.string.GEL, context.getResources().getString(R.string.GEL_symbol),3,false,0,R.drawable.flag_gel));
                realm.insertOrUpdate(new Currency("GHS",936,R.string.GHS, context.getResources().getString(R.string.GHS_symbol),0,false,0,R.drawable.flag_ghs));
                realm.insertOrUpdate(new Currency("GIP",292,R.string.GIP, context.getResources().getString(R.string.GIP_symbol),1,false,0,R.drawable.flag_gip));
                realm.insertOrUpdate(new Currency("GMD",270,R.string.GMD, context.getResources().getString(R.string.GMD_symbol),0,false,0,R.drawable.flag_gmd));
                realm.insertOrUpdate(new Currency("GYD",328,R.string.GYD, context.getResources().getString(R.string.GYD_symbol),1,false,0,R.drawable.flag_gyd));
                realm.insertOrUpdate(new Currency("HKD",344,R.string.HKD, context.getResources().getString(R.string.HKD_symbol),1,false,0,R.drawable.flag_hkd));
                realm.insertOrUpdate(new Currency("HNL",340,R.string.HNL, context.getResources().getString(R.string.HNL_symbol),1,false,0,R.drawable.flag_hnl));
                realm.insertOrUpdate(new Currency("HRK",191,R.string.HRK, context.getResources().getString(R.string.HRK_symbol),4,false,0,R.drawable.flag_hrk));
                realm.insertOrUpdate(new Currency("HTG",332,R.string.HTG, context.getResources().getString(R.string.HTG_symbol),0,false,0,R.drawable.flag_htg));
                realm.insertOrUpdate(new Currency("HUF",348,R.string.HUF, context.getResources().getString(R.string.HUF_symbol),4,false,0,R.drawable.flag_huf));
                realm.insertOrUpdate(new Currency("IDR",360,R.string.IDR, context.getResources().getString(R.string.IDR_symbol),0,false,0,R.drawable.flag_idr));
                realm.insertOrUpdate(new Currency("ILS",376,R.string.ILS, context.getResources().getString(R.string.ILS_symbol),1,false,1,R.drawable.flag_ils));
                realm.insertOrUpdate(new Currency("INR",356,R.string.INR, context.getResources().getString(R.string.INR_symbol),1,false,0,R.drawable.flag_inr));
                realm.insertOrUpdate(new Currency("IQD",368,R.string.IQD, context.getResources().getString(R.string.IQD_symbol),0,false,0,R.drawable.flag_iqd));
                realm.insertOrUpdate(new Currency("IRR",364,R.string.IRR, context.getResources().getString(R.string.IRR_symbol),1,false,0,R.drawable.flag_irr));
                realm.insertOrUpdate(new Currency("ISK",352,R.string.ISK, context.getResources().getString(R.string.ISK_symbol),1,false,0,R.drawable.flag_isk));
                realm.insertOrUpdate(new Currency("JMD",388,R.string.JMD, context.getResources().getString(R.string.JMD_symbol),1,false,0,R.drawable.flag_jmd));
                realm.insertOrUpdate(new Currency("JOD",400,R.string.JOD, context.getResources().getString(R.string.JOD_symbol),0,false,0,R.drawable.flag_jod));
                realm.insertOrUpdate(new Currency("JPY",392,R.string.JPY, context.getResources().getString(R.string.JPY_symbol),1,false,1,R.drawable.flag_jpy));
                realm.insertOrUpdate(new Currency("KES",404,R.string.KES, context.getResources().getString(R.string.KES_symbol),0,false,0,R.drawable.flag_kes));
                realm.insertOrUpdate(new Currency("KGS",417,R.string.KGS, context.getResources().getString(R.string.KGS_symbol),4,false,0,R.drawable.flag_kgs));
                realm.insertOrUpdate(new Currency("KHR",116,R.string.KHR, context.getResources().getString(R.string.KHR_symbol),3,false,0,R.drawable.flag_khr));
                realm.insertOrUpdate(new Currency("KPW",408,R.string.KPW, context.getResources().getString(R.string.KPW_symbol),1,false,0,R.drawable.flag_kpw));
                realm.insertOrUpdate(new Currency("KRW",410,R.string.KRW, context.getResources().getString(R.string.KRW_symbol),1,false,0,R.drawable.flag_krw));
                realm.insertOrUpdate(new Currency("KYD",136,R.string.KYD, context.getResources().getString(R.string.KYD_symbol),1,false,0,R.drawable.flag_kyd));
                realm.insertOrUpdate(new Currency("KZT",398,R.string.KZT, context.getResources().getString(R.string.KZT_symbol),4,false,0,R.drawable.flag_kzt));
                realm.insertOrUpdate(new Currency("LAK",418,R.string.LAK, context.getResources().getString(R.string.LAK_symbol),1,false,0,R.drawable.flag_lak));
                realm.insertOrUpdate(new Currency("LBP",422,R.string.LBP, context.getResources().getString(R.string.LBP_symbol),0,false,0,R.drawable.flag_lbp));
                realm.insertOrUpdate(new Currency("LKR",144,R.string.LKR, context.getResources().getString(R.string.LKR_symbol),0,false,0,R.drawable.flag_lkr));
                realm.insertOrUpdate(new Currency("LTL",440,R.string.LTL, context.getResources().getString(R.string.LTL_symbol),0,false,0,R.drawable.flag_ltl));
                realm.insertOrUpdate(new Currency("LYD",434,R.string.LYD, context.getResources().getString(R.string.LYD_symbol),0,false,0,R.drawable.flag_lyd));
                realm.insertOrUpdate(new Currency("MAD",504,R.string.MAD, context.getResources().getString(R.string.MAD_symbol),0,false,0,R.drawable.flag_mad));
                realm.insertOrUpdate(new Currency("MDL",498,R.string.MDL, context.getResources().getString(R.string.MDL_symbol),4,false,0,R.drawable.flag_mdl));
                realm.insertOrUpdate(new Currency("MNT",496,R.string.MNT, context.getResources().getString(R.string.MNT_symbol),3,false,0,R.drawable.flag_mnt));
                realm.insertOrUpdate(new Currency("MOP",446,R.string.MOP, context.getResources().getString(R.string.MOP_symbol),0,false,0,R.drawable.flag_mop));
                realm.insertOrUpdate(new Currency("MUR",480,R.string.MUR, context.getResources().getString(R.string.MUR_symbol),0,false,0,R.drawable.flag_mur));
                realm.insertOrUpdate(new Currency("MXN",484,R.string.MXN, context.getResources().getString(R.string.MXN_symbol),1,false,0,R.drawable.flag_mxn));
                realm.insertOrUpdate(new Currency("NAD",516,R.string.NAD, context.getResources().getString(R.string.NAD_symbol),0,false,0,R.drawable.flag_nad));
                realm.insertOrUpdate(new Currency("NGN",566,R.string.NGN, context.getResources().getString(R.string.NGN_symbol),1,false,0,R.drawable.flag_ngn));
                realm.insertOrUpdate(new Currency("NIO",558,R.string.NIO, context.getResources().getString(R.string.NIO_symbol),0,false,0,R.drawable.flag_nio));
                realm.insertOrUpdate(new Currency("NOK",578,R.string.NOK, context.getResources().getString(R.string.NOK_symbol),1,false,0,R.drawable.flag_nok));
                realm.insertOrUpdate(new Currency("NZD",554,R.string.NZD, context.getResources().getString(R.string.NZD_symbol),1,false,0,R.drawable.flag_nzd));
                realm.insertOrUpdate(new Currency("OMR",512,R.string.OMR, context.getResources().getString(R.string.OMR_symbol),1,false,0,R.drawable.flag_omr));
                realm.insertOrUpdate(new Currency("PHP",608,R.string.PHP, context.getResources().getString(R.string.PHP_symbol),1,false,0,R.drawable.flag_php));
                realm.insertOrUpdate(new Currency("PKR",586,R.string.PKR, context.getResources().getString(R.string.PKR_symbol),0,false,0,R.drawable.flag_pkr));
                realm.insertOrUpdate(new Currency("PLN",985,R.string.PLN, context.getResources().getString(R.string.PLN_symbol),1,false,0,R.drawable.flag_pln));
                realm.insertOrUpdate(new Currency("PYG",600,R.string.PYG, context.getResources().getString(R.string.PYG_symbol),1,false,0,R.drawable.flag_pyg));
                realm.insertOrUpdate(new Currency("QAR",634,R.string.QAR, context.getResources().getString(R.string.QAR_symbol),1,false,0,R.drawable.flag_qar));
                realm.insertOrUpdate(new Currency("RSD",941,R.string.RSD, context.getResources().getString(R.string.RSD_symbol),0,false,0,R.drawable.flag_rsd));
                realm.insertOrUpdate(new Currency("RUB",643,R.string.RUB, context.getResources().getString(R.string.RUB_symbol),4,false,10,R.drawable.flag_rub));
                realm.insertOrUpdate(new Currency("SAR",682,R.string.SAR, context.getResources().getString(R.string.SAR_symbol),1,false,0,R.drawable.flag_sar));
                realm.insertOrUpdate(new Currency("SBD",90,R.string.SBD, context.getResources().getString(R.string.SBD_symbol),1,false,0,R.drawable.flag_sbd));
                realm.insertOrUpdate(new Currency("SCR",690,R.string.SCR, context.getResources().getString(R.string.SCR_symbol),0,false,0,R.drawable.flag_scr));
                realm.insertOrUpdate(new Currency("SEK",752,R.string.SEK, context.getResources().getString(R.string.SEK_symbol),4,false,0,R.drawable.flag_sek));
                realm.insertOrUpdate(new Currency("SGD",702,R.string.SGD, context.getResources().getString(R.string.SGD_symbol),1,false,1,R.drawable.flag_sgd));
                realm.insertOrUpdate(new Currency("THB",764,R.string.THB, context.getResources().getString(R.string.THB_symbol),1,false,0,R.drawable.flag_thb));
                realm.insertOrUpdate(new Currency("TMT",934,R.string.TMT, context.getResources().getString(R.string.TMT_symbol),4,false,0,R.drawable.flag_tmt));
                realm.insertOrUpdate(new Currency("TRY",949,R.string.TRY, context.getResources().getString(R.string.TRY_symbol),1,false,0,R.drawable.flag_try));
                realm.insertOrUpdate(new Currency("TTD",780,R.string.TTD, context.getResources().getString(R.string.TTD_symbol),1,false,0,R.drawable.flag_ttd));
                realm.insertOrUpdate(new Currency("TWD",901,R.string.TWD, context.getResources().getString(R.string.TWD_symbol),1,false,0,R.drawable.flag_twd));
                realm.insertOrUpdate(new Currency("TZS",834,R.string.TZS, context.getResources().getString(R.string.TZS_symbol),4,false,0,R.drawable.flag_tzs));
                realm.insertOrUpdate(new Currency("UAH",980,R.string.UAH, context.getResources().getString(R.string.UAH_symbol),1,false,0,R.drawable.flag_uah));
                realm.insertOrUpdate(new Currency("USD",840,R.string.USD, context.getResources().getString(R.string.USD_symbol),1,false,10,R.drawable.flag_usd));
                realm.insertOrUpdate(new Currency("UYU",858,R.string.UYU, context.getResources().getString(R.string.UYU_symbol),1,false,0,R.drawable.flag_uyu));
                realm.insertOrUpdate(new Currency("VND",704,R.string.VND, context.getResources().getString(R.string.VND_symbol),4,false,0,R.drawable.flag_vnd));
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_1, Measure.MEASURE_UNIT,R.string.Measure_SHS_1, Measure.SYSTEM_METRIC, Measure.PATTERN_ENTIRE,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_2, Measure.MEASURE_UNIT,R.string.Measure_SHS_2, Measure.SYSTEM_METRIC, Measure.PATTERN_ENTIRE,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_3, Measure.MEASURE_UNIT,R.string.Measure_SHS_3, Measure.SYSTEM_BRITAIN, Measure.PATTERN_ENTIRE,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_4, Measure.MEASURE_MASS,R.string.Measure_SHS_4, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_5, Measure.MEASURE_MASS,R.string.Measure_SHS_5, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_6, Measure.MEASURE_MASS,R.string.Measure_SHS_6, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_7, Measure.MEASURE_MASS,R.string.Measure_SHS_7, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_8, Measure.MEASURE_MASS,R.string.Measure_SHS_8, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_9, Measure.MEASURE_MASS,R.string.Measure_SHS_9, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_10, Measure.MEASURE_MASS,R.string.Measure_SHS_10, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_11, Measure.MEASURE_MASS,R.string.Measure_SHS_11, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_12, Measure.MEASURE_MASS,R.string.Measure_SHS_12, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_13, Measure.MEASURE_MASS,R.string.Measure_SHS_13, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_14, Measure.MEASURE_MASS,R.string.Measure_SHS_14, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_15, Measure.MEASURE_MASS,R.string.Measure_SHS_15, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_16, Measure.MEASURE_LENGTH,R.string.Measure_SHS_16, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_17, Measure.MEASURE_LENGTH,R.string.Measure_SHS_17, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_18, Measure.MEASURE_LENGTH,R.string.Measure_SHS_18, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_19, Measure.MEASURE_LENGTH,R.string.Measure_SHS_19, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_20, Measure.MEASURE_LENGTH,R.string.Measure_SHS_20, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_21, Measure.MEASURE_LENGTH,R.string.Measure_SHS_21, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_22, Measure.MEASURE_LENGTH,R.string.Measure_SHS_22, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_23, Measure.MEASURE_LENGTH,R.string.Measure_SHS_23, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_24, Measure.MEASURE_SQUARE,R.string.Measure_SHS_24, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_25, Measure.MEASURE_SQUARE,R.string.Measure_SHS_25, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_26, Measure.MEASURE_SQUARE,R.string.Measure_SHS_26, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_27, Measure.MEASURE_SQUARE,R.string.Measure_SHS_27, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_28, Measure.MEASURE_SQUARE,R.string.Measure_SHS_28, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_29, Measure.MEASURE_SQUARE,R.string.Measure_SHS_29, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_30, Measure.MEASURE_SQUARE,R.string.Measure_SHS_30, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_31, Measure.MEASURE_SQUARE,R.string.Measure_SHS_31, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_32, Measure.MEASURE_SQUARE,R.string.Measure_SHS_32, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_33, Measure.MEASURE_VOLUME,R.string.Measure_SHS_33, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_34, Measure.MEASURE_VOLUME,R.string.Measure_SHS_34, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_35, Measure.MEASURE_VOLUME,R.string.Measure_SHS_35, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_36, Measure.MEASURE_VOLUME,R.string.Measure_SHS_36, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_37, Measure.MEASURE_VOLUME,R.string.Measure_SHS_37, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_38, Measure.MEASURE_VOLUME,R.string.Measure_SHS_38, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_39, Measure.MEASURE_VOLUME,R.string.Measure_SHS_39, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_40, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_40, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_41, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_41, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_42, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_42, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_43, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_43, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_44, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_44, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_45, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_45, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_46, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_46, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_47, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_47, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_48, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_48, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_49, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_49, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_50, Measure.MEASURE_LIQUIDDRY,R.string.Measure_SHS_50, Measure.SYSTEM_METRIC, Measure.PATTERN_FRACTIONAL,true));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_51, Measure.MEASURE_MASS,R.string.Measure_SHS_51, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
                realm.insertOrUpdate(new Measure(R.string.Measure_SHN_52, Measure.MEASURE_MASS,R.string.Measure_SHS_52, Measure.SYSTEM_BRITAIN, Measure.PATTERN_FRACTIONAL,false));
            }
        });

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Measure m = realm.where(Measure.class).equalTo("symbolId",R.string.Measure_SHS_1).findFirst();
                Measure m1 = realm.where(Measure.class).equalTo("symbolId",R.string.Measure_SHS_4).findFirst();
                Currency cr = realm.where(Currency.class).equalTo("iso_code_str","RUB").findFirst();
                Currency cf = realm.where(Currency.class).equalTo("iso_code_str","XAF").findFirst();
                Currency cd = realm.where(Currency.class).equalTo("iso_code_str","USD").findFirst();
                Variant v = new Variant("Торт",m1,7,100, "Мой торт и ссылка http://test.com",cr);
                String s = v.toString();
                Variant v2 = new Variant("Колбаса",m,3,50, "",cr);
                Variant v3 = new Variant("Хлеб",m1,5.01,60, "",cr);
                Variant v4 = new Variant("Фильтр для воды",m1,5,60, "",cr);
                v4.addUrl("https://test.com");
                realm.insertOrUpdate(v);
                realm.insertOrUpdate(v2);
                realm.insertOrUpdate(v3);
                realm.insertOrUpdate(v4);

                VariantInShop vis;
                vis = new VariantInShop("Первый магазин","https://shop.ru df gsdgf s df gsdf gsdg f","Адрес первого магазина","Комментарий для первого магазина", 91.0, cr);
                realm.insertOrUpdate(vis);
                v4.addShop(vis);
                vis = new VariantInShop("Второй магазин","https://shop2.ru","Адрес второго магазина","Комментарий для второго магазина", 101.0, cr);
                realm.insertOrUpdate(vis);
                v4.addShop(vis);
                vis = new VariantInShop("Третий магазин","https://shop3.ru","Адрес третьего магазина","Комментарий для третьего магазина", 103.0, cr);
                realm.insertOrUpdate(vis);
                v4.addShop(vis);
                vis = new VariantInShop("Четвертый магазин","https://shop4.ru","Адрес четвертого магазина","Комментарий для четвертого магазина hj hkhjk ghj ghj ghj", 104.0, cr);
                realm.insertOrUpdate(vis);
                v4.addShop(vis);
                v4.addImage("00000000-def1-0000-0000-000000000000");
                v4.addImage("00000000-def2-0000-0000-000000000000");
                v4.addImage("00000000-def3-0000-0000-000000000000");
                v4.addImage("00000000-def4-0000-0000-000000000000");
                v4.addImage("00000000-def5-0000-0000-000000000000");

                Record r2 = new Record(v4);
                r2.setName("Фильтр");
                r2.setComment("Комментарий для записи со ссылкой http://test.com");
                r2.addLabel(new Label("Green", Color.GREEN,"",null));
                r2.addLabel(new Label("Yellow", Color.YELLOW,"",null));
                r2.addLabel(new Label("Blue", Color.BLUE,"",null));
                r2.addLabel(new Label("Red", Color.RED,"",null));
                r2.addLabel(new Label("Magenta", Color.MAGENTA,"",null));
                r2.addVariant(v);
                r2.addVariant(v2);
                r2.addVariant(v3);
                realm.insertOrUpdate(r2);
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v4));
                realm.insertOrUpdate(new Record(v3));
                realm.insertOrUpdate(new Record(v2));

                int colorCount = Pallet.getColors().size();

                for (int i=0; i<colorCount; i++) {
                    realm.insertOrUpdate(new Label(Pallet.getNameColors().get(i), Pallet.getColors().get(i),"",null));
                }

/*                Record r = realm.where(Record.class).findFirst();
                r.addVariant(vv2);*/


/*                RealmResults<Label> ls = realm.where(Label.class).findAll();
                for (Label l : ls) {
                    r.addLabel(l);
                }*/

                realm.insertOrUpdate(new Block("Блокнот 1",Block.PRIORITY_OFF));
                Block b = realm.where(Block.class).contains("name","Блокнот 1").findFirst();
                //   b.addRecord(r);

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
}
