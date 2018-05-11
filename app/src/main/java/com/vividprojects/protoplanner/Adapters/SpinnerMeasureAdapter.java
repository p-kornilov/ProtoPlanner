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

import com.vividprojects.protoplanner.Utils.Bundle2;

public class SpinnerMeasureAdapter extends ArrayAdapter<Bundle2<Integer,String>> {
    private LayoutInflater inflater;
    private int resourceId;
    private int resourceDropId;
    private int textViewId;
    private int imageViewId;

    public SpinnerMeasureAdapter(Context context, int resourceId, int resourceDropId, int textViewId, int imageViewId, Bundle2<Integer,String>[] list) {
        super(context,resourceId,textViewId,list);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resourceId = resourceId;
        this.resourceDropId = resourceDropId;
        this.textViewId = textViewId;
        this.imageViewId = imageViewId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Bundle2<Integer,String> item = getItem(position);

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

        Bundle2<Integer,String> item = getItem(position);

        TextView text = convertView.findViewById(textViewId);
        text.setText(item.second);

        ImageView image = convertView.findViewById(imageViewId);
        image.setImageResource(item.first);

        return convertView;
    }
}
