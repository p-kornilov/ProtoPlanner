package com.vividprojects.protoplanner.coredata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
//import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Variant extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String title;
    private Measure measure;
    private double count;
   // private double price;
    private String comment;
    private VariantInShop primaryShop;
    private RealmList<String> urls;
    private RealmList<String> images;
    private int defaultImage = 0;
    private RealmList<VariantInShop> shops;
   // private Currency currency;

    public Variant() {};

    public String getId() {
        return id;
    }

    public Variant(String title, Measure measure, double count, double price, String comment, Currency currency) {
        this.title = title;
        this.measure = measure;
        this.count = count;
        this.comment = comment;
        this.urls = new RealmList<>();
        this.images = new RealmList<>();
        this.shops = new RealmList<>();

        this.primaryShop = new VariantInShop(price, currency);
    }

    public void setPrimaryShop(VariantInShop primaryShop) {
        this.primaryShop = primaryShop;
    }

    public Currency getCurrency() {
        return primaryShop.getCurrency();
    }

    public String getTitle() {
        return title;
    }

    public boolean setMeasure(Measure measure) {
        this.measure = measure;
        return true;
    }

    public void setDefaultImage(int defaultImage) {
        this.defaultImage = defaultImage;
    }

    public VariantInShop getPrimaryShop() {
        return primaryShop;
    }

    public void deleteShop(VariantInShop shop) {
        shops.remove(shop);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCurrency(Currency currency) {
        this.primaryShop.setCurrency(currency);
    }

    public Measure getMeasure() { return measure; }

    public void setCount(double count) {
        this.count = count;
    }

    public void setPrice(double price) {
        this.primaryShop.setPrice(price);
    }

    public double getCount() { return count; }

    public double getPrice() { return primaryShop.getPrice(); }

    public double getValue() { return primaryShop.getPrice()*count; }

    public void setComment(String comment) { this.comment = comment; }

    public String getComment() { return comment; }

    public boolean addUrl(String url) { return urls.add(url); }

    public List<String> getUrls() { return urls; }

    public boolean addShop(VariantInShop v) { return shops.add(v); }

    public List<String> getImages() {
        return images;
    }

    public boolean addImage(String image) {
        return images.add(image);
    }

//    public List<String> getShops() { return urls; }

/*    @Override
    public String toString() {
        String str= "Title: " + title + " (comment: " + comment + ")" + ", count: " + count + " " + measure.getSymbol() + ", for " + price + " each, with total value: " + PriceFormatter.createValue(currency.getPlain(),price*count) + "\n\tURLS:\n";


        for (String s : urls) {
            str += "\t\t- "+s+"\n";
        }

        str += "\tShops:\n";

        for (VariantInShop v : shops) {
            str += "\t\t- "+v+"\n";
        }

        return str;
    }*/

    @Override
    public String toString() {
        return "Variant{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", measure=" + measure +
                ", count=" + count +
                ", price=" + primaryShop.getPrice() +
                ", comment='" + comment + '\'' +
                ", urls=" + urls +
                ", images=" + images +
                ", shops=" + shops +
                ", currency=" + primaryShop.getCurrency() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        Variant other = (Variant) obj;

        return id == other.id;
    }

    public Plain getPlain(){
        Plain plain = new Plain();
        plain.id = id;
        plain.title = title;
        plain.measure = measure.getPlain();
        plain.count = count;
        plain.comment = comment;
        plain.defaultImage = defaultImage;
        plain.urls = new ArrayList<>(urls);
        plain.small_images = new ArrayList<>(images);
        plain.full_images = new ArrayList<>(images);
        plain.shops = new ArrayList<>();
        for (VariantInShop shop : shops) {
            plain.shops.add(shop.getPlain());
        }
        plain.primaryShop = primaryShop.getPlain();
        return plain;
    }

    public static class Plain {
        public String id;
        public String title;
        public Measure.Plain  measure;
        public double count;
        public String comment;
        public int defaultImage;
        public List<String> urls;
        public List<String> small_images;
        public List<String> full_images;
        public List<VariantInShop.Plain> shops;
        public VariantInShop.Plain primaryShop;

        public static List<Plain> sort(List<Plain> list) {
            Plain[] holder_list = new Plain[list.size()];
            list.toArray(holder_list);

            Arrays.sort(holder_list,(x, y)->{
                return (int) ((x.primaryShop.price*x.count - y.primaryShop.price*y.count)*100);
            });

            return new ArrayList<>(Arrays.asList(holder_list));
        }

        public static Plain createPlain(Currency.Plain currency, Measure.Plain measure) {
            Plain plain = new Plain();
            VariantInShop.Plain shop = VariantInShop.Plain.createPlain(currency);
            plain.primaryShop = shop;
            plain.measure = measure;
            plain.urls = new ArrayList<>();
            plain.small_images = new ArrayList<>();
            plain.full_images = new ArrayList<>();
            plain.shops = new ArrayList<>();

            return plain;
        }
    }

}
