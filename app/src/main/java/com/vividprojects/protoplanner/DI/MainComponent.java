package com.vividprojects.protoplanner.DI;

import com.vividprojects.protoplanner.Adapters.RecordListAdapter;
import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.PPApplication;
import com.vividprojects.protoplanner.Presenters.RecordListFragmentViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Smile on 05.12.2017.
 */
@Singleton
@Component (modules = {DataManagerModule.class, AppControllerModule.class})
public interface MainComponent {
    void inject(PPApplication application);
    void inject(RecordListFragmentViewModel rvm);
    void inject(RecordListAdapter recordListAdapter);
}
