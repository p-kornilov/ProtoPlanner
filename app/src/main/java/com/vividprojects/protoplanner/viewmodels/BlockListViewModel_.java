package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.bindingmodels.BlockListBindingModel;
import com.vividprojects.protoplanner.bindingmodels.BlockListBindingModel;
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Filter;
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.datamanager.DataSubscriber;
import com.vividprojects.protoplanner.utils.FabActions;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class BlockListViewModel_ extends ViewModel implements FabActions {

    private Runnable addAction;

    final MutableLiveData<Filter> filter;

    private final LiveData<Resource<List<Block.Plain>>> list;
    private final LiveData<Resource<Block.Plain>> refreshedBlock;
    private final MutableLiveData<String> refreshedBlockId = new MutableLiveData<>();

    private DataRepository dataRepository;
    private DataSubscriber dataSubscriber;

    private BlockListBindingModel blockListBindingModel;
    private MediatorLiveData<Block.Plain> changedBlock = new MediatorLiveData<>();

    private LiveData<Block.Plain> currentSubscribedBlock;

    private MutableLiveData<String> newBlockName = new MutableLiveData<>();
    private final LiveData<Block.Plain> newBlock;

    @Inject
    public BlockListViewModel_(DataRepository dataRepository, DataSubscriber dataSubscriber) {
        //super();
        //list = new MutableLiveData<>();

        blockListBindingModel = new BlockListBindingModel();
        blockListBindingModel.setDefaultImage(dataRepository.getDefaultVariantImage());

        this.dataRepository = dataRepository;
        this.dataSubscriber = dataSubscriber;

        this.filter = new MutableLiveData<>();
        list = Transformations.switchMap(filter,input -> {
/*            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return dataRepository.loadBlocks(input.getFilter());
            }*/
            return BlockListViewModel_.this.dataRepository.loadBlocks(input.getFilter());
        });

        refreshedBlock = Transformations.switchMap(refreshedBlockId, id -> null);// BlockListViewModel_.this.dataRepository.loadBlock(id));
        newBlock = Transformations.switchMap(newBlockName, name -> null);// BlockListViewModel_.this.dataRepository.newBlock(name));

    }

    public void setFilter(List<String> ids) {
        Filter update = new Filter(ids);
        if (Objects.equals(filter.getValue(), update)) {
            return;
        }
        filter.setValue(update);
    }

    public LiveData<Resource<List<Block.Plain>>> getList(){

        return list;//dataRepository.loadBlocks();
    }

    public BlockListBindingModel getBlockListBindingModel() {
        return blockListBindingModel;
    }

    public LiveData<Resource<Block.Plain>> getRefreshedBlock() {
        return refreshedBlock;
    }

    public void refreshBlock(String id) {
        //dataRepository.attachVariantToBlock(id, blockItemId.getValue());
        refreshedBlockId.setValue(id);
    }

    public void subscribeToBlock(String id) {
        if (currentSubscribedBlock != null) {
            if (currentSubscribedBlock.getValue() != null)
                dataSubscriber.unsubscribePlain(currentSubscribedBlock.getValue().id);
            changedBlock.removeSource(currentSubscribedBlock);
        }
        currentSubscribedBlock = dataSubscriber.subscribePlain(Block.Plain.class,id);
        changedBlock.addSource(currentSubscribedBlock, rp->changedBlock.setValue(rp));
    }

    public LiveData<Block.Plain> getChangedBlock() {
        return changedBlock;
    }

    public void setActionAdd(Runnable addAction) {
        this.addAction = addAction;
    }

    @Override
    public void actionAdd() {
        if (addAction != null)
            addAction.run();
    }

    public void createNewBlock(String blockName) {
        newBlockName.setValue(blockName);
    }

    public LiveData<Block.Plain> getNewBlock() {
        return newBlock;
    }

    public void deleteBlock(String id) {
       // dataRepository.deleteBlock(id);
    }
}
