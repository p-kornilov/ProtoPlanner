package com.vividprojects.protoplanner.di;


import com.vividprojects.protoplanner.ui.dialogs.CreateLabelDialog;
import com.vividprojects.protoplanner.ui.dialogs.DeleteLabelDialog;
import com.vividprojects.protoplanner.ui.dialogs.EditShopDialog;
import com.vividprojects.protoplanner.ui.dialogs.EditVariantDialog;
import com.vividprojects.protoplanner.ui.fragments.BlockListFragment;
import com.vividprojects.protoplanner.ui.fragments.CurrencyItemFragment;
import com.vividprojects.protoplanner.ui.fragments.CurrencyListFragment;
import com.vividprojects.protoplanner.ui.fragments.MeasureItemFragment;
import com.vividprojects.protoplanner.ui.fragments.MeasureListFragment;
import com.vividprojects.protoplanner.ui.fragments.VariantItemFragment;
import com.vividprojects.protoplanner.ui.fragments.ImageViewFragment;
import com.vividprojects.protoplanner.ui.fragments.RecordItemFragment;
import com.vividprojects.protoplanner.ui.fragments.RecordListFragment;

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
    abstract BlockListFragment contributeBlockListFragment();

    @ContributesAndroidInjector
    abstract RecordItemFragment contributeRecordItemFragment();

/*    @ContributesAndroidInjector
    abstract BlockListFragment contributeBlockFragment();*/

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

    @ContributesAndroidInjector
    abstract EditShopDialog contributeEditShopDialog();

    @ContributesAndroidInjector
    abstract VariantItemFragment contributeVariantItemFragment();

 /*   @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();*/
}
