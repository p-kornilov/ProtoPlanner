package com.vividprojects.protoplanner.Widgets;

import android.databinding.BindingAdapter;
import android.media.Image;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.vividprojects.protoplanner.Adapters.SpinnerImageAdapter;
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
        //ArrayAdapter<Bundle2<String,Image>> dd = new ArrayAdapter<Bundle2<String,Image>>();
        spinnerAdapter.setDropDownViewResource(adapterDropItem);
        int p = spinner.getSelectedItemPosition();
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(p);
    }

    @BindingAdapter({"app:adapterImageItem","app:adapterImageDropItem","app:adapterTextViewId","app:adapterImageViewId","app:adapterTextItems","app:adapterImageItems"})
    public static void bindImageSpinnerAdapter(Spinner spinner, int adapterItem, int adapterDropItem, int textViewId, int imageViewId, String[] itemsText, int[] itemsImage) {
        if (itemsText == null || itemsImage == null || itemsText.length != itemsImage.length)
            return;

        int size = itemsText.length;
        Bundle2<Integer,String>[] al = new Bundle2[size];

        for (int i = 0; i < itemsText.length; i++) {
            Bundle2<Integer,String> b = new Bundle2<>();
            b.first = itemsImage[i];
            b.second = itemsText[i];
            al[i] = b;
        }

        SpinnerImageAdapter spinnerAdapter = new SpinnerImageAdapter(spinner.getContext(), adapterItem, adapterDropItem, textViewId, imageViewId, al);
        //ArrayAdapter<Bundle2<String,Image>> dd = new ArrayAdapter<Bundle2<String,Image>>();
        //spinnerAdapter.setDropDownViewResource(adapterDropItem);
        int p = spinner.getSelectedItemPosition();
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(p);
    }
}
