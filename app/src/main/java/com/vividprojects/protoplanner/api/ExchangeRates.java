package com.vividprojects.protoplanner.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by p.kornilov on 26.02.2018.
 */

public class ExchangeRates {
    private int default_currency;
    private Map<Integer,Float> rates;

    ExchangeRates() {
        rates = new HashMap<>();
    }

    public static Map<Integer,Float> getTestRates(List<Integer> currencies) {
        Map<Integer,Float> rates = new HashMap<>();
        Random rnd = new Random();
        for (Integer currency : currencies) {
            float rate = (float)rnd.nextInt(1000)/100;
            rates.put(currency, rate);
        }
        return rates;
    }

    public static float getTestRate() {
        Random rnd = new Random();
        float rate = (float)rnd.nextInt(1000)/100;
        return rate;
    }
}
