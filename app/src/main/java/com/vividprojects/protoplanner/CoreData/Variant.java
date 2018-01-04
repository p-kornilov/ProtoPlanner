package com.vividprojects.protoplanner.CoreData;

import com.vividprojects.protoplanner.Utils.PriceFormatter;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
//import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Variant extends RealmObject {
    @PrimaryKey
    private String title;
    private int picture;  // может быть ссылка на одну или несколько картинок
    private Measure measure;
    private double count;
    private double price;
//    private double value;
    private String comment;
    private RealmList<String> urls;
    private RealmList<VariantInShop> shops;
    private Currency currency;

    public Variant() {};

    public Variant(String title, Measure measure, double count, double price, String comment, Currency currency) {
        this.title = title;
        this.measure = measure;
        this.count = count;
        this.price = price;
//        this.value = count*price;
        this.comment = comment;
        this.urls = new RealmList<>();
        this.currency = currency;
    }

    public String getFormattedValue() {
        return PriceFormatter.getValue(currency,price*count);
    }

    public String getFormattedPriceShort() {
        return PriceFormatter.getValue(currency,price);
    }

    public String getFormattedPriceFull() {
        return PriceFormatter.getPrice(currency,price,measure);
    }

    public String getFormattedCount() {
        return PriceFormatter.getCount(count,measure);
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getTitle() {
        return title;
    }

    public boolean setMeasure(Measure measure) {
        this.measure = measure;
        return true;
    }

    public Measure getMeasure() { return measure; }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCount() { return count; }

    public double getPrice() { return price; }

    public double getValue() { return price*count; }

    public void setComment(String comment) { this.comment = comment; }

    public String getComment() { return comment; }

    public boolean addUrl(String url) { return urls.add(url); }

    public List<String> getUrls() { return urls; }

    public boolean addShop(VariantInShop v) { return shops.add(v); }

//    public List<String> getShops() { return urls; }

    @Override
    public String toString() {
        String str= "Title: " + title + " (comment: " + comment + ")" + ", count: " + count + " " + measure.getTitle() + ", for " + price + " each, with total value: " + getFormattedValue() + "\n\tURLS:\n";


        for (String s : urls) {
            str += "\t\t- "+s+"\n";
        }

        str += "\tShops:\n";

        for (VariantInShop v : shops) {
            str += "\t\t- "+v+"\n";
        }

        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        Variant other = (Variant) obj;

        return Objects.equals(title,other.title) && Objects.equals(measure,other.measure) && count == other.count && price == other.price;
    }

}
