package com.vividprojects.protoplanner.CoreData;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Smile on 26.10.2017.
 */

public class VariantInShop extends RealmObject {
    @PrimaryKey
    private int hash;
    private String title;
    private String url;
    private String address;
    private String comment;
    private double price;
    @LinkingObjects("shops")
    private final RealmResults<Variant> variant = null;

    public VariantInShop(){
        price = 0;
        comment = "";
        url = "";
        title = "";
        address = "";
        hash = 0;
    }

    public VariantInShop(String title, String url, String address, String comment, double price) {
        this.title = title;
        this.url = url;
        this.comment = comment;
        this.price = price;
        this.address = address;
        hash = hashCode();
    }

    @Override
    public int hashCode() {
        int result = 15;

        result = 31*result + title.hashCode();
        result = 31*result + url.hashCode();

        return result;
    }

    public int getHash() {
        return hash;
    }

    public String getTitle() { return title;}

    public String getAddress() { return address;}

    public String getURL() { return url; }

    public String getComment() { return comment; }

    public double getPrice() { return price; }

    public void setTitle(String title) {
        this.title = title;
//        hash = hashCode();
    }

    public void setAddress(String address) { this.address = address; }

    public void setURL(String url) {
        this.url = url;
//        hash = hashCode();
    }

    public void setComment(String comment) { this.comment= comment; }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "" + title + " (" + url + "), Price: " + price;
    }

}
