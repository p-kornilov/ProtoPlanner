package com.vividprojects.protoplanner.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
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
    private float elevation;


    public void init(Context context) {
        elevation = context.getResources().getDimension(R.dimen.cardElevation);
    }

    public DataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new DataBindingViewHolder(binding);
    }

    public void onBindViewHolder(DataBindingViewHolder holder, int position) {
        Object obj = getObjForPosition(position);
        holder.bind(obj);

        View v = holder.itemView;
        ViewCompat.setElevation(v,elevation);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract Object getObjForPosition(int position);

    protected abstract int getLayoutIdForPosition(int position);
}