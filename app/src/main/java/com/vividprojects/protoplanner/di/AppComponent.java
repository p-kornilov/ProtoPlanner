package com.vividprojects.protoplanner.di;

import android.content.Context;

import com.vividprojects.protoplanner.adapters.RecordListAdapter;
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
        AppModule.class,
        ActivityModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Context context);

/*        @BindsInstance
        Builder appModule(AppModule appModule);*/

        AppComponent build();
    }

   // DataRepository returnDataManager();

    void inject(PPApplication application);
    void inject(RecordListAdapter recordListAdapter);

/*
    void inject(MainActivity activity);
    void inject(RecordListViewModel rvm);
    void inject(RecordListAdapter recordListAdapter);
*/
}
