package com.vividprojects.protoplanner.DI;

import android.app.Activity;

import com.vividprojects.protoplanner.Interface.RecordActivity;
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
}
