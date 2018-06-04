package com.vividprojects.protoplanner.CoreData;

import java.util.List;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Smile on 26.10.2017.
 */

public class VariantInShop extends RealmObject {
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String title;
    private String url;
    private String address;
    private String comment;
    private double price;
    private Currency currency;
    @LinkingObjects("shops")
    private final RealmResults<Variant> variant = null;
    @LinkingObjects("primaryShop")
    private final RealmResults<Variant> variantPrimary = null;

    public VariantInShop(){
        price = 0;
        comment = "";
        url = "";
        title = "";
        address = "";
        currency = new Currency();
    }

    public VariantInShop(double price, Currency currency) {
        this.price = price;
        this.currency = currency;
        comment = "";
        url = "";
        title = "";
        address = "";
    }

    public VariantInShop(String title, String url, String address, String comment, double price, Currency currency) {
        this.title = title;
        this.url = url;
        this.comment = comment;
        this.price = price;
        this.address = address;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public String getTitle() { return title;}

    public String getAddress() { return address;}

    public String getURL() { return url; }

    public String getComment() { return comment; }

    public double getPrice() { return price; }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(String address) { this.address = address; }

    public void setURL(String url) {
        this.url = url;
    }

    public void setComment(String comment) { this.comment= comment; }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "" + title + " (" + url + "), Price: " + price;
    }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.id = id;
        plain.title = title;
        plain.url = url;
        plain.address = address;
        plain.comment = comment;
        plain.price = price;
        plain.currency = currency.getPlain();
        if (variantPrimary != null && !variantPrimary.isEmpty())
            plain.measure = variantPrimary.first().getMeasure().getPlain();
        if (plain.measure == null && variant != null && !variant.isEmpty())
            plain.measure = variant.first().getMeasure().getPlain();
        return plain;
    }

    public class Plain {
        public String id;
        public String title;
        public String url;
        public String address;
        public String comment;
        public double price;
        public Currency.Plain currency;
        public Measure.Plain measure;
    }

}
