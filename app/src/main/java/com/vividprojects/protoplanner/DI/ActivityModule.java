package com.vividprojects.protoplanner.DI;

import com.vividprojects.protoplanner.Interface.Activity.CurrencyItemActivity;
import com.vividprojects.protoplanner.Interface.Activity.CurrencyListActivity;
import com.vividprojects.protoplanner.Interface.ImageViewActivity;
import com.vividprojects.protoplanner.Interface.LabelsActivity;
import com.vividprojects.protoplanner.Interface.Activity.RecordActivity;
import com.vividprojects.protoplanner.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by p.kornilov on 22.12.2017.
 */

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract RecordActivity contributeRecordActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract ImageViewActivity contributeImageViewActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract LabelsActivity contributeLabelsActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract CurrencyItemActivity contributeCurrencyItemActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract CurrencyListActivity contributeCurrencyListActivity();
}
