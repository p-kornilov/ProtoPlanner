package com.vividprojects.protoplanner.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.PPApplication;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Smile on 27.10.2017.
 */


public class LabelsDialogListAdapter extends RecyclerView.Adapter<LabelsDialogListAdapter.ViewHolder> {

    private boolean inDeletionMode = false;
    private ViewGroup parent;

    private List<Label.Plain> data;
    private LayoutInflater inflater;
    private Context context;

    public LabelsDialogListAdapter(List<Label.Plain> data, Context context) {
        this.data = data;

        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
 /*       View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        return new MyViewHolder(itemView);*/

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.label_item_dialog, parent, false); // Сначала вернуть view инфлаттером
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        // После найти каждый view findView
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Label.Plain obj = data.get(position);
        holder.data = obj;

        holder.label_check.setChecked(holder.checked);
        holder.label_chip.setColor(obj.color);
        holder.label_chip.setTitle(obj.name);
       // holder.label_chip.setDeleteButtonVisible(false);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox label_check;
        Chip label_chip;
        boolean checked = false;
        public Label.Plain data;

        ViewHolder(View view) {
            super(view);
            label_check = view.findViewById(R.id.sld_check);
            label_chip = view.findViewById(R.id.sld_chip);

            label_check.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged (CompoundButton buttonView, boolean isChecked){
                    checked = isChecked;
                }
            });
        }
    }
}

