package com.vividprojects.protoplanner.DI;

import com.vividprojects.protoplanner.Interface.BlockListFragment;
import com.vividprojects.protoplanner.Interface.RecordItemFragment;
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

 /*   @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();*/
}
