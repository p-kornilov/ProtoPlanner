package com.vividprojects.protoplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Bundle2;
import com.vividprojects.protoplanner.Utils.Bundle3;

public class SpinnerCurrencyAdapter extends ArrayAdapter<Bundle3<Integer,String,String>> {
    private LayoutInflater inflater;
    private int resourceId;
    private int resourceDropId;
    private int textViewId;
    private int imageViewId;

    public SpinnerCurrencyAdapter(Context context, Bundle3<Integer,String,String>[] list) {
        super(context, R.layout.spinner_measure_item, R.id.spinner_item,list);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resourceId = R.layout.spinner_measure_item;
        this.resourceDropId = R.layout.spinner_measure_item_dropdown;
        this.textViewId = R.id.spinner_item;
        this.imageViewId = R.id.spinner_image;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Bundle3<Integer,String,String> item = getItem(position);

        if(convertView == null) {
            convertView = inflater.inflate(resourceId, parent, false);
        }

        TextView text = convertView.findViewById(textViewId);
        text.setText(item.second);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(resourceDropId,parent, false);
        }

        Bundle3<Integer,String,String> item = getItem(position);

        TextView text = convertView.findViewById(textViewId);
        text.setText(item.second + " (" + item.third + ")");

        ImageView image = convertView.findViewById(imageViewId);
        image.setImageResource(item.first);

        return convertView;
    }
}
