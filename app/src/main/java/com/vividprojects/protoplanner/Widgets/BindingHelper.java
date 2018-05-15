package com.vividprojects.protoplanner.Widgets;

import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.vividprojects.protoplanner.Adapters.ListOutline;
import com.vividprojects.protoplanner.Adapters.SpinnerImageAdapter;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Bundle2;

import java.util.ArrayList;
import java.util.Arrays;

public class BindingHelper {
    @BindingAdapter({"app:adapterTextItem","app:adapterTextDropItem","app:adapterTextViewId","app:adapterTextItems"})
    public static void bindTextSpinnerAdapter(Spinner spinner, int adapterItem, int adapterDropItem, int textViewId, String[] items) {
        ArrayList<String> al = new ArrayList<>();

        if ((items == null || items.length == 0) && spinner.getAdapter() != null)
            for (int i = 0; i < spinner.getAdapter().getCount(); i++)
                al.add((String) spinner.getItemAtPosition(i));
        else if (items != null)
            al.addAll(Arrays.asList(items));
/*
            for (String item : items)
                al.add(item);
*/


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(spinner.getContext(), adapterItem, textViewId, al);
        spinnerAdapter.setDropDownViewResource(adapterDropItem);
        int p = spinner.getSelectedItemPosition();
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(p);
    }

    @BindingAdapter({"app:adapterImageItem","app:adapterImageDropItem","app:adapterTextViewId","app:adapterImageViewId","app:adapterTextItems","app:adapterImageItems"})
    public static void bindImageSpinnerAdapter(Spinner spinner, int adapterItem, int adapterDropItem, int textViewId, int imageViewId, String[] itemsText, TypedArray itemsImage) {
        if (itemsText == null || itemsImage == null || itemsText.length != itemsImage.length())
            return;

        int size = itemsText.length;
        Bundle2<Integer,String>[] al = new Bundle2[size];

        for (int i = 0; i < itemsText.length; i++) {
            Bundle2<Integer,String> b = new Bundle2<>();
            b.first = itemsImage.getResourceId(i,0);
            b.second = itemsText[i];
            al[i] = b;
        }

        SpinnerImageAdapter spinnerAdapter = new SpinnerImageAdapter(spinner.getContext(), adapterItem, adapterDropItem, textViewId, imageViewId, al);
        int p = spinner.getSelectedItemPosition();
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(p);
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
        view.setImageResource(getMeasureImageResource(measure));
    }

    @BindingAdapter("bind:srcVectorResource")
    public static void setSrcVectorResource(ImageView view, int flag) {
        if (flag != 0)
            view.setImageResource(flag);
    }

    @BindingAdapter("bind:srcImage")
    public static void setSrcImage(ImageView view, String image) {
        if (image != null)
            GlideApp.with(view)
                    .load(image)
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(view);
    }

    @BindingAdapter("bind:labelsLayoutMode")
    public static void setLabelsLayoutMode(ChipsLayout layout, int mode) {
        if (layout != null)
            layout.setMode(mode);
    }

    @BindingAdapter({"bind:labelsLayoutData","bind:labelsLayoutDataSelected"})
    public static void setLabelsLayoutData(ChipsLayout layout, Label.Plain[] labels, String[] selected) {
        if (layout != null) {
            layout.setData(Arrays.asList(labels), selected);
        }
    }

    public static int getMeasureImageResource(int measure) {
        switch (measure) {
            case Measure.MEASURE_UNIT:
                return R.drawable.measure_numeric;
            case Measure.MEASURE_MASS:
                return R.drawable.measure_mass;
            case Measure.MEASURE_LENGTH:
                return R.drawable.measure_length;
            case Measure.MEASURE_SQUARE:
                return R.drawable.measure_square;
            case Measure.MEASURE_VOLUME:
                return R.drawable.measure_volume;
            case Measure.MEASURE_LIQUIDDRY:
                return R.drawable.measure_ld;
            default:
                return R.drawable.measure_numeric;
        }
    }
}
