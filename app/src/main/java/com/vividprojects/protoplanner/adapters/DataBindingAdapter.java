package com.vividprojects.protoplanner.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.R;

/**
 * Created by p.kornilov on 26.03.2018.
 */


public abstract class DataBindingAdapter extends RecyclerView.Adapter<DataBindingViewHolder> {
    public static final int TYPE_FLAT = 0;
    public static final int TYPE_ELEVATED = 1;

    private float elevation;
    private boolean typeElevated = false;


    public void init(Context context, int type) {
        elevation = context.getResources().getDimension(R.dimen.cardElevation);
        if (type == TYPE_ELEVATED)
            typeElevated = true;
    }

    public DataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new DataBindingViewHolder(binding);
    }

    public void onBindViewHolder(DataBindingViewHolder holder, int position) {
        Object obj = getObjForPosition(position);
        holder.bind(obj);

        if (typeElevated) {
            View v = holder.itemView;
            ViewCompat.setElevation(v,elevation);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract Object getObjForPosition(int position);

    protected abstract int getLayoutIdForPosition(int position);
}