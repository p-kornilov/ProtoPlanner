package com.vividprojects.protoplanner.DI;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.Presenters.RecordListViewModel;
import com.vividprojects.protoplanner.ViewModel.PPViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by p.kornilov on 21.12.2017.
 */

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecordListViewModel.class)
    abstract ViewModel bindRecordListViewModel(RecordListViewModel recordListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecordItemViewModel.class)
    abstract ViewModel bindRecordItemViewModel(RecordItemViewModel recordItemViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PPViewModelFactory factory);
}
