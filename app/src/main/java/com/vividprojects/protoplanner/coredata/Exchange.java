package com.vividprojects.protoplanner.coredata;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Smile on 26.02.2018.
 */

public class Exchange extends RealmObject {
    private int base_currency;
    @PrimaryKey
    private int currency;
    private float rate;

    public Exchange(){}

    public Exchange(int base_currency, int currency, float rate) {
        this.base_currency = base_currency;
        this.currency = currency;
        this.rate = rate;
    }

    public int getBase_currency() {
        return base_currency;
    }

    public void setBase_currency(int base_currency) {
        this.base_currency = base_currency;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "base_currency=" + base_currency +
                ", currency=" + currency +
                ", rate=" + rate +
                '}';
    }

    public Plain getPlain() {
        Plain plain = new Plain();
        plain.base_currency = base_currency;
        plain.currency = currency;
        plain.rate = rate;
        return plain;
    }

    public class Plain {
        public int base_currency;
        public int currency;
        public float rate;
    }
}
