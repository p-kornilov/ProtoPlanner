package com.vividprojects.protoplanner.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.vividprojects.protoplanner.BindingModels.MeasureItemListBindingModel;
import com.vividprojects.protoplanner.CoreData.Measure_;
import com.vividprojects.protoplanner.Interface.Fragments.MeasureListFragment;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.ItemActions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by p.kornilov on 26.03.2018.
 */

public class MeasureListAdapter_ extends DataBindingAdapter implements ItemActions {
    private final int layoutId;
    private final int headerId;
    private List<Measure_.Plain> data;
    private List<Measure_.Plain> filtered_data = new ArrayList<>();
    private WeakReference<Context> context;
    private Map<Integer,Integer> measureGroups = new HashMap<>();
    private Map<Integer,String> names = new HashMap<>();
    private List<MeasureItemListBindingModel> models = new ArrayList<>();
    private String filter = "";
    private ItemActions master;

    public MeasureListAdapter_(int layoutId, int headerId, Context context, ItemActions master) {
        this.context = new WeakReference<>(context);
        this.layoutId = layoutId;
        this.headerId = headerId;
        this.master = master;
        init(context);

        measureGroups.put(Measure_.MEASURE_UNIT,-1);
        measureGroups.put(Measure_.MEASURE_LENGTH,-1);
        measureGroups.put(Measure_.MEASURE_SQUARE,-1);
        measureGroups.put(Measure_.MEASURE_MASS,-1);
        measureGroups.put(Measure_.MEASURE_LIQUIDDRY,-1);
        measureGroups.put(Measure_.MEASURE_VOLUME,-1);

    }

    @Override
    public int getItemCount() {
        if (filtered_data != null)
            return filtered_data.size();
        else
            return 0;
    }

    @Override
    public Object getObjForPosition(int position) {
        return models.get(position);
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
        Context ctx = context.get();
        if (ctx != null)
            for (Measure_.Plain m : this.data)
                names.put(m.hash, m.name != null ? m.name : ctx.getResources().getString(m.nameId));

        this.filtered_data = this.data;
        sortList();
        createModels();

        notifyDataSetChanged();
    }

    private void sortList() {
        Measure_.Plain[] holder_list = new Measure_.Plain[this.filtered_data.size()];
        this.filtered_data.toArray(holder_list);

        Arrays.sort(holder_list,(x, y)->{
            if (x.measure == y.measure) {
                if (x.header)
                    return -10000;
                if (y.header)
                    return 10000;
                if (x.def)
                    return -1000;
                if (y.def)
                    return 1000;
                return names.get(x.hash).toLowerCase().compareTo(names.get(y.hash).toLowerCase());
            } else
                return (x.measure - y.measure) * 100;
        });

        this.filtered_data.clear();
        this.filtered_data.addAll(Arrays.asList(holder_list));
    }

    private void createModels() {
        models.clear();
        for (int i = 0; i < filtered_data.size(); i++) {

            MeasureItemListBindingModel model = new MeasureItemListBindingModel(context.get(),this);
            Measure_.Plain m = filtered_data.get(i);
            model.setMeasure(m);

            setBackground(model,i);

            models.add(model);
        }
    }

    private void setBackground(MeasureItemListBindingModel model, int position) {
        int listSize = getItemCount();

        Context ctx = context.get();
        if (ctx != null) {
            Drawable drawableResource;
            if (listSize == 1)
                drawableResource = ContextCompat.getDrawable(ctx, R.drawable.list_item_background_single);
            else if (position == 0 || filtered_data.get(position).header)
                drawableResource = ContextCompat.getDrawable(ctx, R.drawable.list_item_background_top);
            else if (position == listSize - 1 || (position < listSize - 1 && filtered_data.get(position + 1).header))
                drawableResource = ContextCompat.getDrawable(ctx, R.drawable.list_item_background_bottom);
            else
                drawableResource = ContextCompat.getDrawable(ctx, R.drawable.list_item_background);
            model.setBackground(drawableResource);
        }

        if (listSize == 1 || position == 0)
            model.setOutlineType(1);
        else
            model.setOutlineType(0);
    }

    private void setBackground(int position) {
        setBackground(models.get(position),position);
    }

    public void setFilter(String filter) {
        this.filter = filter;
        if (filter != null && filter.length()>0) {
            filter = filter.toLowerCase();
            filtered_data = new ArrayList<>();
            for (Measure_.Plain m : data) {
                if (names.get(m.hash).toLowerCase().contains(filter))
                    filtered_data.add(m);
            }
            Measure_ helper = new Measure_();
            filtered_data.add(helper.createHeader(Measure_.MEASURE_UNIT,R.string.measure_unit));
            filtered_data.add(helper.createHeader(Measure_.MEASURE_MASS,R.string.measure_mass));
            filtered_data.add(helper.createHeader(Measure_.MEASURE_LENGTH,R.string.measure_length));
            filtered_data.add(helper.createHeader(Measure_.MEASURE_SQUARE,R.string.measure_square));
            filtered_data.add(helper.createHeader(Measure_.MEASURE_VOLUME,R.string.measure_volume));
            filtered_data.add(helper.createHeader(Measure_.MEASURE_LIQUIDDRY,R.string.measure_liquiddry));
            sortList();
            notifyDataSetChanged();
        } else
            filtered_data = data;
            notifyDataSetChanged();
    }

    @Override
    public void itemDelete(int item) {
        AlertDialog alert = new AlertDialog.Builder(context.get()).create();
        alert.setTitle("Delete");
        alert.setMessage("Delete " + names.get(item) + "?");
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int pos = 0;
                        Measure_.Plain m = null;
                        for (pos = 0; pos < filtered_data.size(); pos++)
                            if (filtered_data.get(pos).hash == item) {
                                m = filtered_data.get(pos);
                                break;
                            }
                        data.remove(m);
                        filtered_data.remove(m);
                        models.remove(pos);
                        setBackground(pos-1);
                        master.itemDelete(item);
                        notifyItemRemoved(pos);
                        dialog.dismiss();
                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void itemEdit(int item) {
        master.itemEdit(item);
    }

    @Override
    public void itemDefault(int item) {
        int dpos = 0;
        Measure_.Plain d = null;
        for (Measure_.Plain m : filtered_data) {
            if (m.def)
                dpos = filtered_data.indexOf(m);
            if (m.hash == item) {
                d = m;
                break;
            }
        }

        d.def = true;
        filtered_data.get(dpos).def = false;
        int pos = filtered_data.indexOf(d);
        itemMove(pos,dpos);

        for (int i = dpos+2; i < filtered_data.size(); i++)
            if (filtered_data.get(i).measure == d.measure && !filtered_data.get(i).header) {
                if (names.get(filtered_data.get(dpos+1).hash).toLowerCase().compareTo(names.get(filtered_data.get(i).hash).toLowerCase()) < 0) {
                    itemMove(dpos+1,i-1);
                    break;
                }
            } else
                break;

        master.itemDefault(item);
    }

    private void itemMove(int from, int to) {
        if (from > getItemCount() || to > getItemCount())
            return;

        filtered_data.add(to,filtered_data.remove(from));
        models.add(to,models.remove(from));
        if (to < from) {
            setBackground(to);
            setBackground(from);
        } else {
            setBackground(to);
        }

        notifyItemMoved(from,to);
        notifyItemChanged(to);
        notifyItemChanged(from);
    }
}