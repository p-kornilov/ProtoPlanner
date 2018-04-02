package com.vividprojects.protoplanner.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.ViewOutlineProvider;

import com.vividprojects.protoplanner.BindingModels.MeasureItemListBindingModel;
import com.vividprojects.protoplanner.CoreData.Measure_;
import com.vividprojects.protoplanner.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by p.kornilov on 26.03.2018.
 */

public class MeasureListAdapter_ extends DataBindingAdapter {
    private final int layoutId;
    private final int headerId;
    private List<Measure_.Plain> data;
    private List<Measure_.Plain> filtered_data = new ArrayList<>();
    private Context context;
    private Map<Integer,Integer> measureGroups = new HashMap<>();
    private Map<Integer,String> names = new HashMap<>();

    public MeasureListAdapter_(int layoutId, int headerId, Context context) {
        this.context = context;
        this.layoutId = layoutId;
        this.headerId = headerId;
        setBackgrounds(context);

        measureGroups.put(Measure_.MEASURE_UNIT,-1);
        measureGroups.put(Measure_.MEASURE_LENGTH,-1);
        measureGroups.put(Measure_.MEASURE_SQUARE,-1);
        measureGroups.put(Measure_.MEASURE_MASS,-1);
        measureGroups.put(Measure_.MEASURE_LIQUIDDRY,-1);
        measureGroups.put(Measure_.MEASURE_VOLUME,-1);

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
        if (data != null) {
            MeasureItemListBindingModel model = new MeasureItemListBindingModel(context);
            model.setMeasure(data.get(position));

            int listSize = getItemCount();

            Drawable drawableResource;
            if (listSize == 1)
                drawableResource = ContextCompat.getDrawable(context, R.drawable.list_item_background_single);
            else if (position == 0 || data.get(position).header)
                drawableResource = ContextCompat.getDrawable(context, R.drawable.list_item_background_top);
            else if (position == listSize - 1 || (position < listSize - 1 && data.get(position+1).header))
                drawableResource = ContextCompat.getDrawable(context, R.drawable.list_item_background_bottom);
            else
                drawableResource = ContextCompat.getDrawable(context, R.drawable.list_item_background);

            /*if (position < listSize - 1 && data.get(position+1).header)
                drawableResource = itemBackgroundBottom;
            if (data.get(position).header)
                drawableResource = itemBackgroundTop;*/

            model.setBackground(drawableResource);

            return model;
        }
        else
            return null;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        if (filtered_data.get(position).header)
            return headerId;
        else
            return layoutId;
    }

    public void setData(List<Measure_.Plain> data) {
        this.data = data;
        Measure_ helper = new Measure_();
        data.add(helper.createHeader(Measure_.MEASURE_UNIT,R.string.measure_unit));
        data.add(helper.createHeader(Measure_.MEASURE_MASS,R.string.measure_mass));
        data.add(helper.createHeader(Measure_.MEASURE_LENGTH,R.string.measure_length));
        data.add(helper.createHeader(Measure_.MEASURE_SQUARE,R.string.measure_square));
        data.add(helper.createHeader(Measure_.MEASURE_VOLUME,R.string.measure_volume));
        data.add(helper.createHeader(Measure_.MEASURE_LIQUIDDRY,R.string.measure_liquiddry));
        this.filtered_data.clear();

        names.clear();
        for (Measure_.Plain m : this.data) {
            names.put(m.hash, m.name != null ? m.name : context.getResources().getString(m.nameId));
        }

        sortList();
        this.filtered_data = this.data;

        notifyDataSetChanged();
    }

    private void sortList() {
        Measure_.Plain[] holder_list = new Measure_.Plain[this.data.size()];
        this.data.toArray(holder_list);

        Arrays.sort(holder_list,(x, y)->{
            if (x.measure == y.measure) {
                if (x.header)
                    return -99;
                if (y.header)
                    return 99;
                if (x.def)
                    return 50;
                return names.get(x.hash).toLowerCase().compareTo(names.get(y.hash).toLowerCase());
            } else
                return (x.measure - y.measure) * 100;
        });

        this.data.clear();
        this.data.addAll(Arrays.asList(holder_list));
    }
}