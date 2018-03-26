package com.vividprojects.protoplanner.Adapters;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.vividprojects.protoplanner.BR;

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
