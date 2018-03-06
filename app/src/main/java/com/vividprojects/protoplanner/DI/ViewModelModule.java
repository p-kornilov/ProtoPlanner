package com.vividprojects.protoplanner.DI;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.vividprojects.protoplanner.Presenters.BlockListViewModel;
import com.vividprojects.protoplanner.Presenters.CurrencyListViewModel;
import com.vividprojects.protoplanner.Presenters.CurrencyViewModel;
import com.vividprojects.protoplanner.Presenters.ImageViewViewModel;
import com.vividprojects.protoplanner.Presenters.LabelsViewModel;
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
    @ViewModelKey(BlockListViewModel.class)
    abstract ViewModel bindBlockListViewModel(BlockListViewModel blockListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecordItemViewModel.class)
    abstract ViewModel bindRecordItemViewModel(RecordItemViewModel recordItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewViewModel.class)
    abstract ViewModel bindImageViewViewModel(ImageViewViewModel imageViewViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LabelsViewModel.class)
    abstract ViewModel bindLabelsViewModel(LabelsViewModel labelsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyListViewModel.class)
    abstract ViewModel bindCurrencyListViewModel(CurrencyListViewModel currencyListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyViewModel.class)
    abstract ViewModel bindCurrencyViewModel(CurrencyViewModel currencyViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PPViewModelFactory factory);
}
