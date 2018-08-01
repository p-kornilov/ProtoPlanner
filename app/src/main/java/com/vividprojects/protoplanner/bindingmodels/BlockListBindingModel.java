package com.vividprojects.protoplanner.bindingmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.adapters.BlockListAdapter;
import com.vividprojects.protoplanner.adapters.RecordListAdapter;
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.utils.ItemActionsBlock;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.widgets.FabManager;

import java.util.List;

public class BlockListBindingModel extends BaseObservable {
    private BlockListAdapter listAdapter;
    private String defaultImage;
    private FabManager fabManager;

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public void setContext(Fragment fragment) {
//        this.context = new WeakReference<>(fragment.getContext());
        this.listAdapter = new BlockListAdapter(fragment.getContext(), defaultImage);
        this.listAdapter.setMaster((ItemActionsBlock) fragment);
        this.fabManager = (FabManager) fragment.getActivity();
    }

    @Bindable
    public RecyclerView.Adapter getBlockListAdapter() {
        return listAdapter;
    }

    @Bindable
    public FabManager getBlockListFabManager() {
        return fabManager;
    }

    public void setBlockList(List<Block.Plain> list) {
        listAdapter.setData(list);
        notifyPropertyChanged(BR.blockListAdapter);
    }

    public void refreshBlock(Block.Plain block) {
        listAdapter.refresh(block);
        notifyPropertyChanged(BR.blockListAdapter);
    }

    public void setFilter(String filter) {
        listAdapter.setFilter(filter);
    }

    public void clearSelected() {
        listAdapter.clearSelected();
    }
}
