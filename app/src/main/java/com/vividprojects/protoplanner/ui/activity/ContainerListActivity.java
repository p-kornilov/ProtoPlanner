package com.vividprojects.protoplanner.ui.activity;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.ui.fragments.CurrencyListFragment;
import com.vividprojects.protoplanner.ui.fragments.MeasureListFragment;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.Bundle1;
import com.vividprojects.protoplanner.utils.ItemNew;
import com.vividprojects.protoplanner.viewmodel.ViewModelHolder;
import com.vividprojects.protoplanner.viewmodels.CurrencyListViewModel;
import com.vividprojects.protoplanner.viewmodels.MeasureListViewModel;
import com.vividprojects.protoplanner.widgets.FabManager;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ContainerListActivity extends AppCompatActivity implements HasSupportFragmentInjector, FabManager {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    DataRepository dataRepository;

    @Inject
    NavigationController navigationController;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private int activityType;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        activityType = getIntent().getIntExtra(NavigationController.ACTIVITY_TYPE, NavigationController.CURRENCY_LIST);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;

        final Bundle1<ItemNew> modelBundle = new Bundle1<>();

        switch (activityType) {
            case NavigationController.CURRENCY_LIST:
                getSupportActionBar().setTitle("Currency list");
                fragment = CurrencyListFragment.create();
                modelBundle.item = obtainViewModel(CurrencyListViewModel.class);
                break;
            case NavigationController.MEASURE_LIST:
                getSupportActionBar().setTitle("Measure list");
                fragment = MeasureListFragment.create();
                modelBundle.item = obtainViewModel(MeasureListViewModel.class);
                break;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modelBundle.item != null)
                    modelBundle.item.itemNew();
            }
        });

        if (fragment != null)
            fragmentTransaction.replace(R.id.container_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void hideFab() {
        fab.hide();
    }

    @Override
    public void showFab() {
        fab.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
*//*        if (id == R.id.action_settings) {
            return true;
        }*//*

        return super.onOptionsItemSelected(item);
    }*/

    private <T extends ViewModel> T obtainViewModel(Class<T> viewModelClass) {
        ViewModelHolder<T> viewModelHolder = (ViewModelHolder<T>)getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        if (viewModelHolder != null && viewModelHolder.getViewModel() != null) {
            return viewModelHolder.getViewModel();
        } else {
            T model = ViewModelProviders.of(this,viewModelFactory).get(viewModelClass);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fff = ViewModelHolder.createContainer(model);
            ft.add(fff,ViewModelHolder.TAG).commit();
            return model;
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

/*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
*/
}
