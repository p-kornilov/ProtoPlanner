package com.vividprojects.protoplanner.CoreData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helen on 31.12.2017.
 */

public class Filter {
    private List<String> labels_id;

    public Filter(List<String> labels) {
        this.labels_id = labels == null ? new ArrayList<String>() : labels;
    }

    public boolean isEmpty() {
        return labels_id == null || labels_id.isEmpty();
    }

    public void addLabel(String id) {
        labels_id.add(id);
    }

    public void setFilter(List<String> labels) {
        if (labels != null) {
            this.labels_id = labels;
        }
    }

    public List<String> getFilter(){
        return labels_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Filter filter = (Filter) o;

        return labels_id.equals(filter.labels_id);
    }
}
