package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.vividprojects.protoplanner.bindingmodels.BlockItemBindingModel;
import com.vividprojects.protoplanner.bindingmodels.RecordItemBindingModel;
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.datamanager.DataRepository;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class BlockItemViewModel extends ViewModel {
    final MutableLiveData<String> blockItemId;
    private final LiveData<Resource<Block.Plain>> blockItem;
    private final LiveData<Resource<List<Record.Plain>>> records;
    private final MutableLiveData<String> blockNameTrigger;
    //private final LiveData<String> recordNameChange;
    private final MediatorLiveData<String> blockName;

    private DataRepository dataRepository;
    private BlockItemBindingModel bindingModelBlock;

    private boolean inImageLoading = false;

    @Inject
    public BlockItemViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;
        blockItemId = new MutableLiveData<>();
        blockNameTrigger = new MutableLiveData<>();

        bindingModelBlock = new BlockItemBindingModel();
        bindingModelBlock.setDefaultImage(null);

        blockItem = Transformations.switchMap(blockItemId, input -> {
            return BlockItemViewModel.this.dataRepository.loadBlock(input);
        });

        records = Transformations.switchMap(blockItemId, id -> BlockItemViewModel.this.dataRepository.loadRecords(id));

        blockName = new MediatorLiveData<>();
        blockName.addSource(blockNameTrigger,name->{
            BlockItemViewModel.this.dataRepository.setBlockName(blockItemId.getValue(),name);
            blockName.setValue(name);
        });
        blockName.addSource(blockItem,block->{blockName.setValue(block.data.name);});

    }

    public void setId(String id) {
        if (Objects.equals(blockItemId.getValue(), id)) {
            return;
        }
        blockItemId.setValue(id);
    }

    public LiveData<Resource<Block.Plain>> getBlockItem() {
        return blockItem;
    }

    public LiveData<String> getBlockName() {
        return blockName;
    }

    public LiveData<String> getBlockItemID() {
        return blockItemId;
    }

    public void setBlockName(String name) {
        if (Objects.equals(blockNameTrigger.getValue(), name)) {
            return;
        }
        blockNameTrigger.setValue(name);
        //dataRepository.setRecordName(blockItemId.getValue(),name);
    }

    public LiveData<Resource<List<Record.Plain>>> getRecords() {
        return records;
    }

    public BlockItemBindingModel getBindingModelBlock() {
        return bindingModelBlock;
    }

    public LiveData<String> setComment(String comment) {
        return dataRepository.setBlockComment(bindingModelBlock.getBlockId(),comment);
    }

/*
    public LiveData<List<Variant.Plain>> getAlternativeVariants() {
        return alternativeVariants;
    }

    public void refreshVariant(String id) {
        //dataRepository.attachVariantToRecord(id, blockItemId.getValue());
        refreshedVariantId.setValue(id);
    }

    public void addVariant(String id) {
        dataRepository.attachVariantToRecord(id, blockItemId.getValue());
        refreshedVariantId.setValue(id);
    }

    public LiveData<Resource<Variant.Plain>> getRefreshedVariant() {
        return refreshedVariant;
    }
*/

/*
    public void deleteVariant(String id) {
        dataRepository.deleteVariant(id);
    }
*/

}
