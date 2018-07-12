package com.vividprojects.protoplanner.bindingmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.vividprojects.protoplanner.adapters.VariantListAdapter;
import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.utils.ItemActionsVariant;

import java.lang.ref.WeakReference;
import java.util.List;

public class RecordItemBindingModel extends BaseObservable {
    private Record.Plain record;

    private VariantListAdapter alternativeVariantsListAdapter;
    private String defaultImage;

    private WeakReference<Runnable> onCommentEditClick;
    private WeakReference<Runnable> onLabelsEditClick;
    private WeakReference<Runnable> onAddVariantClick;
    private WeakReference<Runnable> onCreateBasicVariantClick;

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public void setContext(Fragment fragment) {
//        this.context = new WeakReference<>(fragment.getContext());
        this.alternativeVariantsListAdapter = new VariantListAdapter(fragment.getContext(), defaultImage);
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
    public boolean getRecordBasicVariantIsEmpty(){
        return record.mainVariant == null;
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
        notifyPropertyChanged(BR.recordBasicVariantIsEmpty);
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

    public void setOnCreateBasicVariantClick(Runnable func) {
        this.onCreateBasicVariantClick = new WeakReference<>(func);
    }

    public void onAddVariantClick() {
        if (onAddVariantClick != null && onAddVariantClick.get() != null)
            onAddVariantClick.get().run();
    }

    public void onCreateBasicVariantClick() {
        if (onCreateBasicVariantClick != null && onCreateBasicVariantClick.get() != null)
            onCreateBasicVariantClick.get().run();
    }


    public void refreshVariant(Variant.Plain variant) {
        //alternativeVariantsListAdapter.refresh(variant);
    }
}
