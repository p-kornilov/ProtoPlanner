package com.vividprojects.protoplanner.DI;

import com.vividprojects.protoplanner.Interface.Activity.ContainerItemActivity;
import com.vividprojects.protoplanner.Interface.Activity.ContainerListActivity;
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
    abstract ContainerItemActivity contributeContainerItemActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract ContainerListActivity contributeContainerListActivity();
}
