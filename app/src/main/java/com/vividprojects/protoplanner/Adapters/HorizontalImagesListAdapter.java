package com.vividprojects.protoplanner.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;
import com.vividprojects.protoplanner.Widgets.HorizontalImages;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Smile on 27.10.2017.
 */


public class HorizontalImagesListAdapter extends RecyclerView.Adapter<HorizontalImagesListAdapter.ViewHolder> {

    private boolean inDeletionMode = false;
    private ViewGroup parent;

    private List<String> data;
  //  private LayoutInflater inflater;
    private RecordItemViewModel model;

    private boolean inAddMode = false;
    //private Context context;

    @Inject
    NavigationController navigationController;

    public HorizontalImagesListAdapter(List<String> data, RecordItemViewModel model) {
        this.data = data;
        this.model = model;
/*        data.add("");
        data.remove("");*/
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addMode() {
        if (inAddMode) {
            inAddMode = false;
            int index = data.indexOf("");
            data.remove("");
            notifyItemRemoved(index);

        } else {
            inAddMode = true;
            data.add("");
            notifyItemInserted(data.indexOf(""));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_item, parent, false); // Сначала вернуть view инфлаттером
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        // После найти каждый view findView
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String obj = data.get(position);
        holder.data = obj;
        //noinspection ConstantConditions
//            holder.record_title.setText(obj.getId());
        if (!obj.equals("")) {
            GlideApp.with(holder.image)
                    .load(new File(obj))
                    .into(holder.image);
            holder.circlePBar.setVisibility(ProgressBar.INVISIBLE);
        } else {
            holder.circlePBar.setVisibility(ProgressBar.VISIBLE);
            holder.image.setImageResource(0);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ProgressBar horisontalPBar;
        ProgressBar circlePBar;
        public String data;

        ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.ii_image);
            horisontalPBar = (ProgressBar) view.findViewById(R.id.ii_progress_horizontal);
            circlePBar = (ProgressBar) view.findViewById(R.id.ii_progress_circle);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
}

