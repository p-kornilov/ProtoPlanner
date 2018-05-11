package com.vividprojects.protoplanner.DI;

import com.vividprojects.protoplanner.Interface.BlockListFragment;
import com.vividprojects.protoplanner.Interface.Dialogs.CreateLabelDialog;
import com.vividprojects.protoplanner.Interface.Dialogs.DeleteLabelDialog;
import com.vividprojects.protoplanner.Interface.Dialogs.EditVariantDialog;
import com.vividprojects.protoplanner.Interface.Fragments.CurrencyItemFragment;
import com.vividprojects.protoplanner.Interface.Fragments.CurrencyListFragment;
import com.vividprojects.protoplanner.Interface.Fragments.MeasureItemFragment;
import com.vividprojects.protoplanner.Interface.Fragments.MeasureListFragment;
import com.vividprojects.protoplanner.Interface.ImageViewFragment;
import com.vividprojects.protoplanner.Interface.Fragments.RecordItemFragment;
import com.vividprojects.protoplanner.Interface.RecordListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by p.kornilov on 22.12.2017.
 */

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract RecordListFragment contributeRecordListFragment();

    @ContributesAndroidInjector
    abstract RecordItemFragment contributeRecordItemFragment();

    @ContributesAndroidInjector
    abstract BlockListFragment contributeBlockFragment();

    @ContributesAndroidInjector
    abstract ImageViewFragment contributeImageViewFragment();

    @ContributesAndroidInjector
    abstract CreateLabelDialog contributeCreateLabelDialog();

    @ContributesAndroidInjector
    abstract DeleteLabelDialog contributeDeleteLabelDialog();

    @ContributesAndroidInjector
    abstract CurrencyListFragment contributeCurrencyListFragment();

    @ContributesAndroidInjector
    abstract CurrencyItemFragment contributeCurrencyFragment();

    @ContributesAndroidInjector
    abstract MeasureListFragment contributeMeasureListFragment();

    @ContributesAndroidInjector
    abstract MeasureItemFragment contributeMeasureItemFragment();

    @ContributesAndroidInjector
    abstract EditVariantDialog contributeEditVariantDialog();

 /*   @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();*/
}
