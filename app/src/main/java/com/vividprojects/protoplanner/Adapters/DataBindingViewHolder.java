package com.vividprojects.protoplanner.Adapters;

import android.annotation.TargetApi;
import android.databinding.ViewDataBinding;
import android.graphics.Outline;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.R;

/**
 * Created by p.kornilov on 26.03.2018.
 */

public class DataBindingViewHolder extends RecyclerView.ViewHolder {
    private final ViewDataBinding binding;

    public DataBindingViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        binding.setVariable(BR.obj, obj);
        binding.executePendingBindings();
    }
}
