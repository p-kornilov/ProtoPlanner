package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.vividprojects.protoplanner.Adapters.ListOutline;
import com.vividprojects.protoplanner.Adapters.MeasureListAdapter_;
import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Measure_;
import com.vividprojects.protoplanner.Interface.Fragments.MeasureListFragment;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.ItemActions;


/**
 * Created by Smile on 18.03.2018.
 */

public class MeasureItemListBindingModel extends BaseObservable {
    private Drawable background;

    private Measure_.Plain measure;

    private Context context;
    private ItemActions listAdapter;

    private int outlineType = 0;

    public MeasureItemListBindingModel(Context context, ItemActions listAdapter) {
        measure = (new Measure_()).getPlain();
        this.context = context;
        this.listAdapter = listAdapter;
    }

    public void setMeasure(Measure_.Plain measure) {
        this.measure = measure;
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.symbol);
    }

    public void setOutlineType(int type) {
        this.outlineType = type;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    @Bindable
    public int getOutlineType() {
        return outlineType;
    }

    @Bindable
    public Drawable getBackground() {
        return background;
    }

    @Bindable
    public String getSymbol() {
        return measure.symbol != null ? measure.symbol : context.getResources().getString(measure.symbolId);
    }

    @Bindable
    public String getName() {
        return measure.name != null ? measure.name : context.getResources().getString(measure.nameId);
    }

    @Bindable
    public int getMeasure() {
        return measure.measure;
    }

    public void onMClicked() {
        String n = "";
    }

    public void onMenuClicked(View view) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mce_edit:
                        listAdapter.itemEdit(measure.hash);
                        return true;
                    case R.id.mce_default:
                        if (!measure.def)
                            listAdapter.itemDefault(measure.hash);
                        return true;
                    case R.id.mce_delete:
                        listAdapter.itemDelete(measure.hash);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_currency_edit, popup.getMenu());
        popup.show();
    }
};