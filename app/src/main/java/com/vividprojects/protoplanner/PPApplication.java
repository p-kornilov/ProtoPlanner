package com.vividprojects.protoplanner;

import android.app.Application;
import android.util.Log;


import com.vividprojects.protoplanner.DI.AppControllerModule;
import com.vividprojects.protoplanner.DI.DaggerMainComponent;
import com.vividprojects.protoplanner.DI.MainComponent;
import com.vividprojects.protoplanner.DI.DataManagerModule;
import com.vividprojects.protoplanner.DataManager.AppController;
import com.vividprojects.protoplanner.DataManager.DataManager;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Smile on 24.10.2017.
 */

public class PPApplication extends Application {

    @Inject
    DataManager dataManager;
    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure Realm for the application
        Realm.init(this);
        Log.d("Test", "------------------------------ Start");

        MainComponent mainComponent = DaggerMainComponent.builder()
                .dataManagerModule(new DataManagerModule())
                .appControllerModule(new AppControllerModule(getApplicationContext()))
                .build();
        mainComponent.inject(this);

        dataManager.initDB();
        dataManager.showDB();

   //     RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
   //     Realm.deleteRealm(realmConfiguration); // Clean slate
   //     Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }

}
