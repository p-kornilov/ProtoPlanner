package com.vividprojects.protoplanner.bindingmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.adapters.RecordCategorizedListAdapter;
import com.vividprojects.protoplanner.adapters.VariantListAdapter;
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.utils.ItemActionsVariant;

import java.lang.ref.WeakReference;
import java.util.List;

public class BlockItemBindingModel extends BaseObservable {
    private Block.Plain block;

    private RecordCategorizedListAdapter recordsListAdapter;
    private String defaultImage;

    private WeakReference<Runnable> onCommentEditClick;
    private WeakReference<Runnable> onLabelsEditClick;
    private WeakReference<Runnable> onAddRecordClick;

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public void setContext(Fragment fragment) {
//        this.context = new WeakReference<>(fragment.getContext());
        this.recordsListAdapter = new RecordCategorizedListAdapter(fragment.getContext(), defaultImage);
        this.recordsListAdapter.setMaster((ItemActionsRecord) fragment);
    }

    @Bindable
    public RecyclerView.Adapter getRecordsAdapter() {
        return recordsListAdapter;
    }

    @Bindable
    public String getBlockName(){
        return block.name;
    }

    @Bindable
    public String getBlockComment(){
        return block.comment;
    }

    @Bindable
    public String getBlockTotalValue(){
        return String.valueOf(block.value);
    }

    @Bindable
    public String getBlockTotalRecords(){
        return String.valueOf(block.elementsCount);
    }

    public String getBlockId() {
        return block.id;
    }

    public void setBlock(Block.Plain block) {
        this.block = block;
        notifyPropertyChanged(BR.blockName);
        notifyPropertyChanged(BR.blockComment);
    }

    public void setRecords(List<Record.Plain> list) {
        recordsListAdapter.setData(list);
        notifyPropertyChanged(BR.recordsAdapter);
    }

    public void refreshRecord(Record.Plain record) {
        recordsListAdapter.refresh(record);
        notifyPropertyChanged(BR.recordsAdapter);
    }

    public void setBlockComment(String comment) {
        this.block.comment = comment;
        notifyPropertyChanged(BR.blockComment);
    }

    public void setOnCommentEditClick(Runnable func) {
        this.onCommentEditClick = new WeakReference<>(func);
    }

    public void setOnLabelsEditClick(Runnable func) {
        this.onLabelsEditClick = new WeakReference<>(func);
    }

    public void onCommentEditClick() {
        if (onCommentEditClick != null && onCommentEditClick.get() != null)
            onCommentEditClick.get().run();
    }

    public void onLabelsEditClick() {
        if (onLabelsEditClick != null && onLabelsEditClick.get() != null)
            onLabelsEditClick.get().run();
    }

    public void setOnAddRecordClick(Runnable func) {
        this.onAddRecordClick = new WeakReference<>(func);
    }

    public void onAddRecordClick() {
        if (onAddRecordClick != null && onAddRecordClick.get() != null)
            onAddRecordClick.get().run();
    }
}
