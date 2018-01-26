package com.vividprojects.protoplanner.Presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.os.Environment;
import android.transition.Transition;
import android.util.Log;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BaseTarget;
import com.vividprojects.protoplanner.CoreData.Filter;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Resource;
import com.vividprojects.protoplanner.DB.SDFileManager;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Images.FullTarget;
import com.vividprojects.protoplanner.Images.GlideApp;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class BlockListViewModel extends ViewModel {

    private final MutableLiveData<Filter> filter;

    private final LiveData<Resource<List<Record>>> list;

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

    public LiveData<Resource<List<Record>>> getList(){

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

}
