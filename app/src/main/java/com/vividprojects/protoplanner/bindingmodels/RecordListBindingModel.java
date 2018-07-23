package com.vividprojects.protoplanner.bindingmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.adapters.RecordListAdapter;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.widgets.FabManager;

import java.util.List;

public class RecordListBindingModel extends BaseObservable {
    private RecordListAdapter recordListAdapter;
    private String defaultImage;
    private FabManager fabManager;

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public void setContext(Fragment fragment) {
//        this.context = new WeakReference<>(fragment.getContext());
        this.recordListAdapter = new RecordListAdapter(fragment.getContext(), defaultImage);
        this.recordListAdapter.setMaster((ItemActionsRecord) fragment);
        this.fabManager = (FabManager) fragment.getActivity();
    }

    @Bindable
    public RecyclerView.Adapter getRecordListAdapter() {
        return recordListAdapter;
    }

    @Bindable
    public FabManager getRecordListFabManager() {
        return fabManager;
    }

    public void setRecordList(List<Record.Plain> list) {
        recordListAdapter.setData(list);
        notifyPropertyChanged(BR.recordListAdapter);
    }

    public void refreshRecord(Record.Plain record) {
        recordListAdapter.refresh(record);
        notifyPropertyChanged(BR.recordListAdapter);
    }

    public void setFilter(String filter) {
        recordListAdapter.setFilter(filter);
    }

    public void clearSelected() {
        recordListAdapter.clearSelected();
    }
}
