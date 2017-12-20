package com.vividprojects.protoplanner.CoreData;

import java.util.List;

public class Measures {
    private List<Measure> measures;

    public Measures() {
        Measure m = new Measure("шт.");
        measures.add(m);
        measures.add(new Measure("кг."));
        measures.add(new Measure("м."));
        measures.add(new Measure("кв.м."));
        measures.add(new Measure("л."));
    }

    public void loadMeasures() {
    }

    public boolean addMeasure(Measure m) {
        return measures.add(m);
    }

    public List<Measure> getMeasures() {
        return measures;
    }
}
