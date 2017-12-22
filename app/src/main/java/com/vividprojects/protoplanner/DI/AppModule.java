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
/*    Context context;

    public AppModule(Context context) {
        this.context = context;
    };*/

//    public AppModule() {};

    @Provides
    @Singleton
    DataManager provideDataManager(Context context){
        return new DataManager(context);
    }
}
