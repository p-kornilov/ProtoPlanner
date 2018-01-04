package com.vividprojects.protoplanner.DI;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.DB.LocalDB;
import com.vividprojects.protoplanner.DB.NetworkDB;
import com.vividprojects.protoplanner.DB.NetworkResponse;
import com.vividprojects.protoplanner.DataManager.DataRepository;

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
    NetworkDB provideNetworkDB(){
        return new NetworkDB();
    }

    @Provides
    @Singleton
    LocalDB provideLocalDB(Context context){
        return new LocalDB(context);
    }
}
