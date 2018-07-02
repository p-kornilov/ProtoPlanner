package com.vividprojects.protoplanner.bindingmodels;

import android.databinding.BaseObservable;

import com.vividprojects.protoplanner.adapters.RecordListAdapter;

public class RecordListBindingModel extends BaseObservable {
    private RecordListAdapter recordListAdapter;
    private String defaultImage;

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

/*    public void setContext(Fragment fragment) {
//        this.context = new WeakReference<>(fragment.getContext());
        this.recordListAdapter = new RecordListAdapter(fragment.getContext(), defaultImage);
        this.recordListAdapter.setMaster((ItemActionsRecord) fragment);
    }

    @Bindable
    public RecyclerView.Adapter getRecordListAdapter() {
        return recordListAdapter;
    }

    public void setRecordList(List<Record.Plain> list) {
        recordListAdapter.setData(list);
        notifyPropertyChanged(BR.recordListAdapter);
    }

    public void refreshRecord(Record.Plain record) {
        recordListAdapter.refresh(record);
        notifyPropertyChanged(BR.recordListAdapter);
    }*/
}
