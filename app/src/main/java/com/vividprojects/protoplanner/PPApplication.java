package com.vividprojects.protoplanner;

import android.app.Application;
import android.util.Log;


import com.vividprojects.protoplanner.DI.AppComponent;
import com.vividprojects.protoplanner.DI.AppControllerModule;
import com.vividprojects.protoplanner.DI.AppModule;
import com.vividprojects.protoplanner.DataManager.DataManager;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by Smile on 24.10.2017.
 */

public class PPApplication extends Application {

    @Inject
    DataManager dataManager;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Configure Realm for the application
        Realm.init(this);
        Log.d("Test", "------------------------------ Start");

/*        appComponent = DaggerAppComponent.builder()
                .dataManagerModule(new AppModule(this))
                .appControllerModule(new AppControllerModule(getApplicationContext()))
                .build();*/
        appComponent.inject(this);

        dataManager.initDB();
 //       dataManager.showDB();

   //     RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
   //     Realm.deleteRealm(realmConfiguration); // Clean slate
   //     Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default
    }

    public AppComponent getMainComponent() {
        return appComponent;
    }

}
