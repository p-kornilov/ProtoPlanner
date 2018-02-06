package com.vividprojects.protoplanner.Adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Display;
import com.vividprojects.protoplanner.Utils.RunnableParam;
import com.vividprojects.protoplanner.Widgets.Pallet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Smile on 27.10.2017.
 */


public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {

    private boolean inDeletionMode = false;
    private ViewGroup parent;
    private int lastChecked = -1;


    private List<ColorPicker> data;
  //  private LayoutInflater inflater;
    private RecordItemViewModel model;

    private boolean inLoadingState = false;
    //private Context context;

    private RunnableParam<Integer> onImageClick;

    public ColorPickerAdapter(RecordItemViewModel model) {
        data = new ArrayList<>();
        for (int color: Pallet.getColors()) {
            data.add(new ColorPicker(color));
        }
        this.model = model;
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

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        View root;
        public ColorPicker data;

        ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.cp_selector);
            root = view.findViewById(R.id.cp_color);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

    private void selected(int position){
        if (lastChecked != position) {
            data.get(position).checked = true;
            if (lastChecked != -1)
                data.get(lastChecked).checked = false;
            lastChecked = position;
            notifyDataSetChanged();
        }
    }
}

