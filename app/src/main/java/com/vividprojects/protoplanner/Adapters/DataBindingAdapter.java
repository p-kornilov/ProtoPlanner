package com.vividprojects.protoplanner.Adapters;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Outline;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;

import com.vividprojects.protoplanner.R;

/**
 * Created by p.kornilov on 26.03.2018.
 */


public abstract class DataBindingAdapter extends RecyclerView.Adapter<DataBindingViewHolder> {

    public DataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new DataBindingViewHolder(binding);
    }

    public void onBindViewHolder(DataBindingViewHolder holder, int position) {
        Object obj = getObjForPosition(position);
        holder.bind(obj);

/*        int listSize = getItemCount();

        int drawableResource;
        if (listSize == 1)
            drawableResource = R.drawable.list_item_background_single;
        else if (position == 0)
            drawableResource = R.drawable.list_item_background_top;
        else if (position == listSize - 1)
            drawableResource = R.drawable.list_item_background_bottom;
        else
            drawableResource = R.drawable.list_item_background;

        View v = holder.itemView;

        v.setBackground(ContextCompat.getDrawable(v.getContext(),drawableResource));

        if (listSize == 1 || position==0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                v.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                v.setOutlineProvider(new ListOutline());
        }*/
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract Object getObjForPosition(int position);

    protected abstract int getLayoutIdForPosition(int position);
}