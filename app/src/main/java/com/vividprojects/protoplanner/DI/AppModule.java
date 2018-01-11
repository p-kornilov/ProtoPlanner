package com.vividprojects.protoplanner.DI;

import android.content.Context;

import com.vividprojects.protoplanner.DB.LocalDataDB;
import com.vividprojects.protoplanner.DB.NetworkDataDB;
import com.vividprojects.protoplanner.DB.SDFileManager;

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

    /*@Provides
    @Singleton
    DataRepository provideDataManager(Context context){
        return new DataRepository(context);
    }*/

    @Provides
    @Singleton
    NetworkDataDB provideNetworkDB(){
        return new NetworkDataDB();
    }

    @Provides
    @Singleton
    LocalDataDB provideLocalDB(Context context){
        return new LocalDataDB(context);
    }

    @Provides
    @Singleton
    SDFileManager provideSDFileManager(Context context){
        return new SDFileManager(context);
    }
}
