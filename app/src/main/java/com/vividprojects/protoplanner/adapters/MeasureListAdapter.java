package com.vividprojects.protoplanner.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.vividprojects.protoplanner.bindingmodels.MeasureItemListBindingModel;
import com.vividprojects.protoplanner.coredata.Measure;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.ItemActions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by p.kornilov on 26.03.2018.
 */

public class MeasureListAdapter extends DataBindingAdapter implements ItemActions {
    private final int layoutId;
    private final int headerId;
    private List<Measure.Plain> data;
    private List<Measure.Plain> filtered_data = new ArrayList<>();
    private WeakReference<Context> context;
    private Map<Integer,Integer> measureGroups = new HashMap<>();
    private Map<Integer,String> names = new HashMap<>();
    private List<MeasureItemListBindingModel> models = new ArrayList<>();
    private String filter = "";
    private ItemActions master;

    public MeasureListAdapter(int layoutId, int headerId, Context context, ItemActions master) {
        this.context = new WeakReference<>(context);
        this.layoutId = layoutId;
        this.headerId = headerId;
        this.master = master;
        init(context, DataBindingAdapter.TYPE_ELEVATED);

        measureGroups.put(Measure.MEASURE_UNIT,-1);
        measureGroups.put(Measure.MEASURE_LENGTH,-1);
        measureGroups.put(Measure.MEASURE_SQUARE,-1);
        measureGroups.put(Measure.MEASURE_MASS,-1);
        measureGroups.put(Measure.MEASURE_LIQUIDDRY,-1);
        measureGroups.put(Measure.MEASURE_VOLUME,-1);

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

    public void refresh(Measure.Plain measure) {
        String measureName = measure.name != null ? measure.name : context.get().getString(measure.nameId);
        String measureNameUp = measureName.toUpperCase();
        int posInsert = 0;
        for (Measure.Plain m : this.filtered_data) {
            if (m.hash == measure.hash) {
                int pos = filtered_data.indexOf(m);
                data.remove(m);
                filtered_data.remove(m);
                models.remove(pos);
                data.add(measure);
                filtered_data.add(pos,measure);
                models.add(pos,createModel(measure,pos));
                names.put(measure.hash,measureName);
                notifyItemChanged(pos);
                return;
            }
            if (measure.measure > m.measure || (m.measure == measure.measure && (m.header || m.def || measureNameUp.compareTo(names.get(m.hash).toUpperCase()) > 0)))
                posInsert++;
            if (m.measure > measure.measure)
                break;
        }
        data.add(measure);
        names.put(measure.hash,measureName);
        filtered_data.add(posInsert,measure);
        models.add(posInsert,createModel(measure,posInsert));
        setBackground(posInsert-1);
        notifyItemInserted(posInsert);
        notifyItemChanged(posInsert-1);
    }

    public void setData(List<Measure.Plain> data) {
        this.data = data;
        Measure helper = new Measure();
        data.add(helper.createHeader(Measure.MEASURE_UNIT,R.string.measure_unit));
        data.add(helper.createHeader(Measure.MEASURE_MASS,R.string.measure_mass));
        data.add(helper.createHeader(Measure.MEASURE_LENGTH,R.string.measure_length));
        data.add(helper.createHeader(Measure.MEASURE_SQUARE,R.string.measure_square));
        data.add(helper.createHeader(Measure.MEASURE_VOLUME,R.string.measure_volume));
        data.add(helper.createHeader(Measure.MEASURE_LIQUIDDRY,R.string.measure_liquiddry));
        //this.filtered_data.clear();

        names.clear();
        Context ctx = context.get();
        if (ctx != null)
            for (Measure.Plain m : this.data)
                names.put(m.hash, m.name != null ? m.name : ctx.getResources().getString(m.nameId));

        this.filtered_data = new ArrayList<>(this.data);
        sortList();
        createModels();

        notifyDataSetChanged();
    }

    private void sortList() {
        this.filtered_data = Measure.Plain.sort(context.get(), this.filtered_data);
    }

    private void createModels() {
        models.clear();
        for (int i = 0; i < filtered_data.size(); i++) {

            MeasureItemListBindingModel model = new MeasureItemListBindingModel(context.get(),this);
            Measure.Plain m = filtered_data.get(i);
            model.setMeasure(m);

            setBackground(model,i);

            models.add(model);
        }
    }

    private MeasureItemListBindingModel createModel(Measure.Plain measure, int position) {
        MeasureItemListBindingModel model = new MeasureItemListBindingModel(context.get(),this);
        Measure.Plain m = measure;
        model.setMeasure(m);
        setBackground(model,position);

        return model;
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
            for (Measure.Plain m : data) {
                if (names.get(m.hash).toLowerCase().contains(filter))
                    filtered_data.add(m);
            }
            Measure helper = new Measure();
            filtered_data.add(helper.createHeader(Measure.MEASURE_UNIT,R.string.measure_unit));
            filtered_data.add(helper.createHeader(Measure.MEASURE_MASS,R.string.measure_mass));
            filtered_data.add(helper.createHeader(Measure.MEASURE_LENGTH,R.string.measure_length));
            filtered_data.add(helper.createHeader(Measure.MEASURE_SQUARE,R.string.measure_square));
            filtered_data.add(helper.createHeader(Measure.MEASURE_VOLUME,R.string.measure_volume));
            filtered_data.add(helper.createHeader(Measure.MEASURE_LIQUIDDRY,R.string.measure_liquiddry));
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
                        Measure.Plain m = null;
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
        Measure.Plain d = null;
        for (Measure.Plain m : filtered_data) {
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