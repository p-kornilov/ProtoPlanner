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
    private Drawable itemBackgroundSingle;
    private Drawable itemBackgroundTop;
    private Drawable itemBackgroundBottom;
    private Drawable itemBackground;
    private float elevation;
    private ListOutline listOutline;


    public void setBackgrounds(Context context, int single, int top, int bottom, int background) {
        elevation = context.getResources().getDimension(R.dimen.cardElevation);
        itemBackgroundSingle = ContextCompat.getDrawable(context,single);
        itemBackgroundTop = ContextCompat.getDrawable(context,top);
        itemBackgroundBottom = ContextCompat.getDrawable(context,bottom);
        itemBackground = ContextCompat.getDrawable(context,background);
        listOutline = new ListOutline(context);
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

        int listSize = getItemCount();

        Drawable drawableResource;
        if (listSize == 1)
            drawableResource = itemBackgroundSingle;
        else if (position == 0)
            drawableResource = itemBackgroundTop;
        else if (position == listSize - 1)
            drawableResource = itemBackgroundBottom;
        else
            drawableResource = itemBackground;

        v.setBackground(drawableResource);

        if (listSize == 1 || position==0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                v.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                v.setOutlineProvider(listOutline);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract Object getObjForPosition(int position);

    protected abstract int getLayoutIdForPosition(int position);
}