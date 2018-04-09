package com.vividprojects.protoplanner.Widgets;

import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.vividprojects.protoplanner.Adapters.ListOutline;
import com.vividprojects.protoplanner.Adapters.SpinnerImageAdapter;
import com.vividprojects.protoplanner.CoreData.Measure_;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Bundle2;
import com.vividprojects.protoplanner.Utils.PriceFormatter;

import java.util.ArrayList;
import java.util.List;

public class BindingHelper {
    @BindingAdapter({"app:adapterTextItem","app:adapterTextDropItem","app:adapterTextViewId","app:adapterTextItems"})
    public static void bindTextSpinnerAdapter(Spinner spinner, int adapterItem, int adapterDropItem, int textViewId, String[] items) {
        ArrayList<String> al = new ArrayList<>();

        if ((items == null || items.length == 0) && spinner.getAdapter() != null)
            for (int i = 0; i < spinner.getAdapter().getCount(); i++)
                al.add((String) spinner.getItemAtPosition(i));
        else
            for (int i = 0; i < items.length; i++)
                al.add(items[i]);

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
}
