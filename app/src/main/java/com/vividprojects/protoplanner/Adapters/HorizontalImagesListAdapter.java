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

import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.RunnableParam;
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
    private int progress;

    private List<String> data;
  //  private LayoutInflater inflater;
    private RecordItemViewModel model;

    private boolean inLoadingState = false;
    //private Context context;

    private RunnableParam<String> onImageClick;

    public HorizontalImagesListAdapter(List<String> data, RecordItemViewModel model, RunnableParam<String> onImageClick) {
        this.data = data;
        this.model = model;
        this.onImageClick = onImageClick;
/*        data.add("");
        data.remove("");*/
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public int addMode() {
        int index;
        if (inLoadingState) {
            inLoadingState = false;
            index = data.indexOf("");
            data.remove("");
            notifyItemRemoved(index);

        } else {
            inLoadingState = true;
            data.add("");
            index = data.indexOf("");
            notifyItemInserted(data.indexOf(""));
        }
        return index;
    }

/*    public int loadingState(boolean state, int progress) {
        int index=0;
        if (state) {
            if (inLoadingState) {
                this.progress = progress;
                notifyItemChanged(index = data.indexOf(""));
            } else {
                inLoadingState = true;
                this.progress = progress;
                data.add("");
                index = data.indexOf("");
                notifyItemInserted(data.indexOf(""));
            }
        } else {
            inLoadingState = false;
            index = data.indexOf("");
            data.remove("");
            notifyItemRemoved(index);
        }
        return index;
    }*/

    public int loadingInProgress(int progress) {
        int index;
        if (inLoadingState) {
            this.progress = progress;
            index = data.indexOf("");
            notifyItemChanged(index);
        } else {
            inLoadingState = true;
            this.progress = progress;
            index = data.indexOf("");
            if (data.indexOf("") == -1) {
                data.add("");
                index = data.indexOf("");
                notifyItemInserted(index);
            }
        }
        return index;
    }

    public void imageReady() {
        inLoadingState = false;
        notifyItemChanged(data.indexOf(""));
    }

    public void loadingDone(boolean result_ok, String image) {
        inLoadingState = false;
        int index = data.indexOf("");
        if (result_ok) {
            data.set(index,image);
            notifyItemChanged(index);
        } else {
            data.remove("");
            notifyItemRemoved(index);
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
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(holder.image);
            holder.circlePBar.setVisibility(ProgressBar.INVISIBLE);
            holder.horisontalPBar.setVisibility(ProgressBar.INVISIBLE);
        } else {
//            holder.circlePBar.setVisibility(ProgressBar.VISIBLE);
            if (inLoadingState) {
                holder.image.setImageResource(0);
                holder.horisontalPBar.setVisibility(ProgressBar.VISIBLE);
                holder.circlePBar.setVisibility(ProgressBar.INVISIBLE);
                holder.horisontalPBar.setProgress(progress);
            } else {
                holder.image.setImageResource(0);
                holder.horisontalPBar.setVisibility(ProgressBar.INVISIBLE);
                holder.circlePBar.setVisibility(ProgressBar.VISIBLE);
            }
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
                    onImageClick.run(data);
                }
            });
        }
    }
}

