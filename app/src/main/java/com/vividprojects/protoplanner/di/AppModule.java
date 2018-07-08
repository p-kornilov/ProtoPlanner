package com.vividprojects.protoplanner.di;

import android.content.Context;

import com.vividprojects.protoplanner.datamanager.DataSubscriber;
import com.vividprojects.protoplanner.db.LocalDataDB;
import com.vividprojects.protoplanner.db.NetworkDataDB;
import com.vividprojects.protoplanner.db.SDFileManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

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
    DataSubscriber provideDataSubscriber(){
        return new DataSubscriber();
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

    @Provides
    @Singleton
    OkHttpClient provideOKHttpClient() {
        return new OkHttpClient();
    }
}
