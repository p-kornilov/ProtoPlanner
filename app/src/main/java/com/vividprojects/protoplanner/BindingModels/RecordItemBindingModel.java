package com.vividprojects.protoplanner.BindingModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.vividprojects.protoplanner.Adapters.VariantListAdapter;
import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.Utils.ItemActionsVariant;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecordItemBindingModel extends BaseObservable {
    private Record.Plain record;

    private VariantListAdapter alternativeVariantsListAdapter;

    private WeakReference<Runnable> onCommentEditClick;
    private WeakReference<Runnable> onLabelsEditClick;
    private WeakReference<Runnable> onAddVariantClick;

    public void setContext(Fragment fragment) {
//        this.context = new WeakReference<>(fragment.getContext());
        this.alternativeVariantsListAdapter = new VariantListAdapter(fragment.getContext());
        this.alternativeVariantsListAdapter.setMaster((ItemActionsVariant) fragment);
    }

    @Bindable
    public RecyclerView.Adapter getAlternativeVariantsAdapter() {
        return alternativeVariantsListAdapter;
    }

    @Bindable
    public boolean getIsAlternativeVariantsEmpty() {
        return alternativeVariantsListAdapter.getItemCount() == 0;
    }

    @Bindable
    public String getRecordName(){
        return record.name;
    }

    @Bindable
    public String getRecordComment(){
        return record.comment;
    }

    @Bindable
    public Label.Plain[] getRecordLabels() {
        return record.labels.toArray(new Label.Plain[record.labels.size()]);
    }

    public String getRecordId() {
        return record.id;
    }

    public void setRecord(Record.Plain record) {
        this.record = record;
        notifyPropertyChanged(BR.recordName);
        notifyPropertyChanged(BR.recordComment);
        notifyPropertyChanged(BR.recordLabels);
        //...
    }

    public void setAlternativeVariants(List<Variant.Plain> list) {
        alternativeVariantsListAdapter.setData(list);
        notifyPropertyChanged(BR.alternativeVariantsAdapter);
        notifyPropertyChanged(BR.isAlternativeVariantsEmpty);
    }

    public void refreshAlternativeVariant(Variant.Plain variant) {
        alternativeVariantsListAdapter.refresh(variant);
        notifyPropertyChanged(BR.isAlternativeVariantsEmpty);
    }

    public void setRecordComment(String comment) {
        this.record.comment = comment;
        notifyPropertyChanged(BR.recordComment);
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

    public void setOnAddVariantClick(Runnable func) {
        this.onAddVariantClick = new WeakReference<>(func);
    }

    public void onAddVariantClick() {
        if (onAddVariantClick != null && onAddVariantClick.get() != null)
            onAddVariantClick.get().run();
    }

    public void refreshVariant(Variant.Plain variant) {
        //alternativeVariantsListAdapter.refresh(variant);
    }
}
