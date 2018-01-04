package com.vividprojects.protoplanner.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.PPApplication;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Smile on 27.10.2017.
 */


public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private boolean inDeletionMode = false;
    private ViewGroup parent;

    private List<Record> data;
    private LayoutInflater inflater;
    private Context context;

    @Inject
    NavigationController navigationController;

    public RecordListAdapter(List<Record> data,Context context) {
        this.data = data;

        this.context = context;

        ((PPApplication) context.getApplicationContext()).getMainComponent().inject(this);   // TODO Пересмотреть, как можно сделать по аналогии с ViewModel - автоматическое инжектирование

        Log.d("Test", "------------------------------ In RLA Type of the device " + navigationController.getType());

        //inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
 /*       View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        return new MyViewHolder(itemView);*/

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false); // Сначала вернуть view инфлаттером
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        // После найти каждый view findView
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Record obj = data.get(position);
        holder.data = obj;
        //noinspection ConstantConditions
//            holder.record_title.setText(obj.getId());
        holder.record_title.setText(obj.getMainVariant().getTitle());
        holder.record_count.setText(obj.getMainVariant().getFormattedCount());
       // holder.record_measure.setText(obj.getMainVariant().getMeasure().getTitle());
        holder.record_value.setText(obj.getMainVariant().getFormattedValue());

        holder.record_chiplayout.removeAllViews();
        holder.record_chiplayout.addView(new Chip(parent.getContext(),Color.BLUE,false));
        holder.record_chiplayout.addView(new Chip(parent.getContext(),Color.RED,false));
        holder.record_chiplayout.addView(new Chip(parent.getContext(),Color.GREEN,false));
        holder.record_chiplayout.noneChip(context);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView record_title;
        TextView record_count;
        TextView record_measure;
        TextView record_value;
        ChipsLayout record_chiplayout;
        public Record data;

        ViewHolder(View view) {
            super(view);
            record_title = (TextView) view.findViewById(R.id.record_title);
            record_count = (TextView) view.findViewById(R.id.record_count);
        //    record_measure = (TextView) view.findViewById(R.id.record_measure);
            record_value = (TextView) view.findViewById(R.id.record_value);
            record_chiplayout = (ChipsLayout) view.findViewById(R.id.record_chiplayout);

/*            Button edit_button = (Button) view.findViewById(R.id.record_list_edit);
            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Test", "------------------------------ Edit click" + data.getId());
                    mCallback.onRecordEditClick(data.getId());
                }
            });*/

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Test", "------------------------------ Edit click" + data.getId());
                    //mCallback.onRecordEditClick(data.getId());
                   // appController.openRecord(data.getId());
                    navigationController.openRecord(data.getId());
                }
            });
        }
    }
}

