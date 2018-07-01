package com.vividprojects.protoplanner.bindingmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.adapters.RecordListAdapter;
import com.vividprojects.protoplanner.adapters.VariantListAdapter;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.utils.ItemActionsVariant;

import java.lang.ref.WeakReference;
import java.util.List;

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
