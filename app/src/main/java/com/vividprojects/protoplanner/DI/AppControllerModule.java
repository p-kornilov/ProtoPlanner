package com.vividprojects.protoplanner.DI;

import android.content.Context;

import com.vividprojects.protoplanner.DataManager.AppController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Smile on 07.12.2017.
 */
@Module
public class AppControllerModule {
    private Context context;

    public AppControllerModule(Context context){
        this.context = context;
    };

    @Provides
    @Singleton
    AppController provideAppController(){
        return new AppController(context);
    }
}
