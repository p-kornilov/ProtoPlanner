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
import com.vividprojects.protoplanner.Adapters.SpinnerCurrencyAdapter;
import com.vividprojects.protoplanner.Adapters.SpinnerImageAdapter;
import com.vividprojects.protoplanner.Adapters.SpinnerMeasureAdapter;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Bundle2;
import com.vividprojects.protoplanner.Utils.Bundle3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @BindingAdapter({"bind:adapterSpinnerCurrencyItem","bind:adapterSpinnerCurrencyList"})
    public static void bindCurrencySpinnerAdapter(Spinner spinner, Currency.Plain adapterItem, List<Currency.Plain> adapterList) {
        if (adapterItem == null || adapterList == null || adapterList.size() == 0)
            return;

        Bundle3<Integer,String,String>[] al = new Bundle3[adapterList.size()];
        int i = 0;
        int cursor = 0;
        for (Currency.Plain c : adapterList) {
            if (adapterItem.iso_code_int == c.iso_code_int)
                cursor = i;
            Bundle3<Integer,String,String> b = new Bundle3<>();
            b.first = c.flag_id;
            b.second = c.symbol;
            b.third = c.iso_code_str;
            al[i] = b;
            i++;
        }
        SpinnerCurrencyAdapter spinnerAdapter = new SpinnerCurrencyAdapter(spinner.getContext(), al);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(cursor);
    }

    @BindingAdapter({"bind:adapterSpinnerMeasureItem","bind:adapterSpinnerMeasureList"})
    public static void bindMeasureSpinnerAdapter(Spinner spinner, Measure.Plain adapterItem, List<Measure.Plain> adapterList) {
        if (adapterItem == null || adapterList == null || adapterList.size() == 0)
            return;

        int i = 0;
        int cursor = -1;
        Bundle2<Integer,String>[] al = new Bundle2[adapterList.size()];
        for (Measure.Plain m : adapterList) {
            if (adapterItem.hash == m.hash)
                cursor = i;
            Bundle2<Integer,String> b = new Bundle2<>();
            b.first = BindingHelper.getMeasureImageResource(m.measure);
            b.second = Measure.Plain.getString(spinner.getContext(),m.symbol,m.symbolId);
            al[i] = b;
            i++;
        }

        if (cursor == -1) {
            adapterList.add(adapterItem);
            adapterList = Measure.Plain.sort(spinner.getContext(),adapterList);
            i = 0;
            cursor = 0;
            al = new Bundle2[adapterList.size()];
            for (Measure.Plain m : adapterList) {
                if (adapterItem.hash == m.hash)
                    cursor = i;
                Bundle2<Integer,String> b = new Bundle2<>();
                b.first = BindingHelper.getMeasureImageResource(m.measure);
                b.second = Measure.Plain.getString(spinner.getContext(),m.symbol,m.symbolId);
                al[i] = b;
                i++;
            }

        }

        SpinnerMeasureAdapter spinnerAdapter = new SpinnerMeasureAdapter(spinner.getContext(), al);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(cursor);
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
