package com.vividprojects.protoplanner.DI;

import android.app.Application;

import com.vividprojects.protoplanner.DataManager.DataManager;
import com.vividprojects.protoplanner.PPApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Smile on 05.12.2017.
 */
@Singleton
@Component (modules = {
        AndroidInjectionModule.class,
        AppModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

    /*    @BindsInstance
        Builder appModule(AppModule appModule);*/

        AppComponent build();
    }

    DataManager returnDataManager();

    void inject(PPApplication application);
/*
    void inject(MainActivity activity);
    void inject(RecordListViewModel rvm);
    void inject(RecordListAdapter recordListAdapter);
*/
}
