package com.vividprojects.protoplanner.BindingModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Variant;

import java.util.List;

public class RecordItemBindingModel extends BaseObservable {
    private Record.Plain record;
    private Variant.Plain mainVariant;
    private List<Variant.Plain> listVariant;

    @Bindable
    public String getRecordName(){
        return record.name;
    }

    @Bindable
    public String getCommentName(){
        return record.comment;
    }

    public void setRecord(Record.Plain record) {
        this.record = record;
        notifyPropertyChanged(BR.recordName);
        notifyPropertyChanged(BR.commentName);
        //...
    }


}
