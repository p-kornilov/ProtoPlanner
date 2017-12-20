package com.vividprojects.protoplanner.DI;

import com.vividprojects.protoplanner.DataManager.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Smile on 05.12.2017.
 */
@Module
public class DataManagerModule {
    public DataManagerModule() {};

    @Provides
    @Singleton
    DataManager provideDataManager(){
        return new DataManager();
    }
}
