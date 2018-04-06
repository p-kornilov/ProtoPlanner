package com.vividprojects.protoplanner.DI;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.vividprojects.protoplanner.ViewModels.BlockListViewModel;
import com.vividprojects.protoplanner.ViewModels.CurrencyItemViewModel;
import com.vividprojects.protoplanner.ViewModels.CurrencyListViewModel;
import com.vividprojects.protoplanner.ViewModels.ImageViewViewModel;
import com.vividprojects.protoplanner.ViewModels.LabelsViewModel;
import com.vividprojects.protoplanner.ViewModels.MeasureItemViewModel;
import com.vividprojects.protoplanner.ViewModels.MeasureListViewModel;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.ViewModels.RecordListViewModel;
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
    @ViewModelKey(CurrencyItemViewModel.class)
    abstract ViewModel bindCurrencyViewModel(CurrencyItemViewModel currencyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MeasureListViewModel.class)
    abstract ViewModel bindMeasureListViewModel(MeasureListViewModel measureListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MeasureItemViewModel.class)
    abstract ViewModel bindMeasureViewModel(MeasureItemViewModel measureViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PPViewModelFactory factory);
}
