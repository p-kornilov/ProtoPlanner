package com.vividprojects.protoplanner.di;

import com.vividprojects.protoplanner.ui.activity.BlockActivity;
import com.vividprojects.protoplanner.ui.activity.ContainerItemActivity;
import com.vividprojects.protoplanner.ui.activity.ContainerListActivity;
import com.vividprojects.protoplanner.ui.activity.LabelsActivity_;
import com.vividprojects.protoplanner.ui.helpers.DialogFullScreenActivity;
import com.vividprojects.protoplanner.ui.activity.ImageViewActivity;
import com.vividprojects.protoplanner.ui.LabelsActivity;
import com.vividprojects.protoplanner.ui.activity.RecordActivity;
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
    abstract BlockActivity contributeBlockActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract ImageViewActivity contributeImageViewActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract LabelsActivity contributeLabelsActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract LabelsActivity_ contributeLabelsActivity_();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract ContainerItemActivity contributeContainerItemActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract ContainerListActivity contributeContainerListActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract DialogFullScreenActivity contributeDialogFullScreenActivity();
}
