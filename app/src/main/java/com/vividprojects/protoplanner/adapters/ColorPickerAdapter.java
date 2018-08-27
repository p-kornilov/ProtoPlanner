package com.vividprojects.protoplanner.adapters;

import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.widgets.Pallet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smile on 27.10.2017.
 */


public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {

    private ViewGroup parent;
    private int lastChecked = -1;
    private int currentColor = -1;
    private boolean clickable = true;

    private List<ColorPicker> data;

    public ColorPickerAdapter() {
        data = new ArrayList<>();
        for (int color: Pallet.getColors()) {
            data.add(new ColorPicker(color));
        }
    }

    public void setSelectedColor(int color) {
        for (int i=0; i<data.size(); i++)
            if (data.get(i).color == color) {
                selected(i);
                break;
            }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_picker, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ColorPicker obj = data.get(position);
        holder.data = obj;
        //noinspection ConstantConditions
//            holder.record_title.setText(obj.getId());
        if (obj!=null) {
            holder.root.getBackground().setColorFilter(obj.color, PorterDuff.Mode.SRC_ATOP);
           // holder.image.setVisibility(View.VISIBLE);
            if (obj.checked)
                holder.image.setVisibility(View.VISIBLE);
            else
                holder.image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getColor() {
        return currentColor;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        View root;
        LinearLayout layout;
        public ColorPicker data;

        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.cp_selector);
            root = view.findViewById(R.id.cp_color);
            layout = view.findViewById(R.id.cp_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickable)
                        selected(ViewHolder.this.getAdapterPosition());
                }
            });
        }
    }

    class ColorPicker {
        public int color;
        public boolean checked;

        ColorPicker(int color,boolean checked) {
            this.color = color;
            this.checked = checked;
        }

        ColorPicker(int color) {
            this(color,false);
        }

    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    private void selected(int position){
        if (lastChecked != position) {
            data.get(position).checked = true;
            currentColor = data.get(position).color;
            if (lastChecked != -1)
                data.get(lastChecked).checked = false;
            notifyItemChanged(lastChecked);
            lastChecked = position;
            notifyItemChanged(position);
        }
    }
}

