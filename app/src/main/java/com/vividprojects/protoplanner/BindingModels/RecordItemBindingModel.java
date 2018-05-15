package com.vividprojects.protoplanner.BindingModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Variant;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecordItemBindingModel extends BaseObservable {
    private Record.Plain record;
    private Variant.Plain mainVariant;
    private List<Variant.Plain> listVariant;

    private WeakReference<Runnable> onCommentEditClick;

    @Bindable
    public String getRecordName(){
        return record.name;
    }

    @Bindable
    public String getRecordComment(){
        return record.comment;
    }

    public String getRecordId() {
        return record.id;
    }

    public void setRecord(Record.Plain record) {
        this.record = record;
        notifyPropertyChanged(BR.recordName);
        notifyPropertyChanged(BR.recordComment);
        //...
    }

    public void setRecordComment(String comment) {
        this.record.comment = comment;
        notifyPropertyChanged(BR.recordComment);
    }

    public void setOnCommentEditClick(Runnable func) {
        this.onCommentEditClick = new WeakReference<>(func);
    }

    public void onCommentEditClick() {
        if (onCommentEditClick != null && onCommentEditClick.get() != null)
            onCommentEditClick.get().run();
    }
}
