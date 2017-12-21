package com.vividprojects.protoplanner.DI;

import android.content.Context;

import com.vividprojects.protoplanner.DataManager.DataManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Smile on 05.12.2017.
 */
@Module (includes = ViewModelModule.class)
public class AppModule {
    private Context context;

 public AppModule(Context context) {
     this.context = context;
 };

//    public AppModule() {};

    @Provides
    @Singleton
    DataManager provideDataManager(){
        return new DataManager(context);
    }
}
