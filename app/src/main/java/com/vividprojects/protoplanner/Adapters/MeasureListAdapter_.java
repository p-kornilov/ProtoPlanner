package com.vividprojects.protoplanner.Adapters;

import com.vividprojects.protoplanner.CoreData.Measure_;

import java.util.List;

/**
 * Created by p.kornilov on 26.03.2018.
 */

public class MeasureListAdapter_ extends DataBindingAdapter {
    private final int layoutId;
    private List<Measure_.Plain> data;
    private List<Measure_.Plain> filtered_data;

    public MeasureListAdapter_(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else
            return 0;
    }

    @Override
    public Object getObjForPosition(int position) {
        if (data != null)
            return data.get(position);
        else
            return null;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    public void setData(List<Measure_.Plain> data) {
        this.data = data;
        this.filtered_data = this.data;
    }
}