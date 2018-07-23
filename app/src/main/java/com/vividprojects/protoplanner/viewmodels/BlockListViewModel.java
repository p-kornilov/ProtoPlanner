package com.vividprojects.protoplanner.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.vividprojects.protoplanner.coredata.Filter;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.utils.FabActions;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class BlockListViewModel extends ViewModel implements FabActions {

    private final MutableLiveData<Filter> filter;

    private final LiveData<Resource<List<Record.Plain>>> list;

    private final LiveData<Integer> loadProgress;
    private final MutableLiveData<String> loadProgressSwitcher;

    private DataRepository dataRepository;

 //   @Inject
 //   SDFileManager fileManager;   // TODO Заинжектить dataRepository и из нее раскрутить/проверить всю цепочку, потом перенести логику в model


    @Inject
    public BlockListViewModel(DataRepository dataRepository) {
        //super();
        //list = new MutableLiveData<>();

        this.dataRepository = dataRepository;

        this.filter = new MutableLiveData<>();
        list = Transformations.switchMap(filter,input -> {
/*            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return dataRepository.loadRecords(input.getFilter());
            }*/
            return BlockListViewModel.this.dataRepository.loadRecords(input.getFilter());
        });

        loadProgressSwitcher = new MutableLiveData<>();
        loadProgress = Transformations.switchMap(loadProgressSwitcher,url->{
           return null;//dataRepository.saveImageFromURLtoVariant(url,null);
        });
    }

    public void setFilter(List<String> ids) {
        Filter update = new Filter(ids);
        if (Objects.equals(filter.getValue(), update)) {
            return;
        }
        filter.setValue(update);
    }

    public LiveData<Resource<List<Record.Plain>>> getList(){

        return list;//dataRepository.loadRecords();
    }

    public LiveData<Integer> getLoadProgress(){
        return loadProgress;
    }

    public void load(String url) {
        loadProgressSwitcher.setValue(url);
//        dataRepository.initImages();
     //   dataRepository.saveImageFromURL("http://anub.ru/uploads/07.2015/976_podborka_34.jpg",null).observe(this,progress->{

      //  });
    }

    @Override
    public void actionAdd() {

    }
}
