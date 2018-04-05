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
import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Measure_;
import com.vividprojects.protoplanner.Interface.Fragments.MeasureListFragment;
import com.vividprojects.protoplanner.R;


/**
 * Created by Smile on 18.03.2018.
 */

public class MeasureItemListBindingModel extends BaseObservable {
    private Drawable background;

    private Measure_.Plain measure;

    private MeasureListFragment context;

    private int outlineType = 0;

    public MeasureItemListBindingModel(MeasureListFragment context) {
        measure = (new Measure_()).getPlain();
        this.context = context;
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

    @BindingAdapter("bind:customOutline")
    public static void setCustomOutline(View view, int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (type == 0)
                view.setOutlineProvider(new ListOutline());
            else
                view.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        }
    }

    @BindingAdapter("bind:srcVector")
    public static void setSrcVector(ImageView view, int measure) {
        int image = R.drawable.measure_numeric;
        switch (measure) {
            case Measure_.MEASURE_UNIT:
                image = R.drawable.measure_numeric;
                break;
            case Measure_.MEASURE_MASS:
                image = R.drawable.measure_mass;
                break;
            case Measure_.MEASURE_LENGTH:
                image = R.drawable.measure_length;
                break;
            case Measure_.MEASURE_SQUARE:
                image = R.drawable.measure_square;
                break;
            case Measure_.MEASURE_VOLUME:
                image = R.drawable.measure_volume;
                break;
            case Measure_.MEASURE_LIQUIDDRY:
                image = R.drawable.measure_ld;
        }
        view.setImageResource(image);
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
                        context.editItem(measure.hash);
                        return true;
                    case R.id.mce_default:
                        setDefaultItem();
                        return true;
                    case R.id.mce_delete:
                        deleteItem();
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

    private void setDefaultItem() {

    }

    private void deleteItem() {

    }
};