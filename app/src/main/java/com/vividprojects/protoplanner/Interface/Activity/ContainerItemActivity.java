package com.vividprojects.protoplanner.Interface.Activity;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Interface.Fragments.CurrencyItemFragment;
import com.vividprojects.protoplanner.Interface.Fragments.CurrencyListFragment;
import com.vividprojects.protoplanner.Interface.Fragments.MeasureItemFragment;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.ViewModels.CurrencyListViewModel;
import com.vividprojects.protoplanner.ViewModels.CurrencyItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModel.ViewModelHolder;
import com.vividprojects.protoplanner.ViewModels.MeasureItemViewModel;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ContainerItemActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    DataRepository dataRepository;

    @Inject
    NavigationController navigationController;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        int activityType = getIntent().getIntExtra(NavigationController.ACTIVITY_TYPE, NavigationController.CURRENCY_ITEM);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;

        switch (activityType) {
            case NavigationController.CURRENCY_ITEM:
                getSupportActionBar().setTitle("Currency");
                int iso_code = getIntent().getIntExtra(NavigationController.CURRENCY_ID,-1);
                fragment = CurrencyItemFragment.create(iso_code);
                CurrencyItemViewModel model_c = obtainViewModel(CurrencyItemViewModel.class);

                model_c.getOnSaveId().observe(this,id->{
                    if (id != null) {
                        Intent intent = new Intent();
                        intent.putExtra("ID", id.intValue());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                break;
            case NavigationController.MEASURE_ITEM:
                getSupportActionBar().setTitle("Measure");
                int hash = getIntent().getIntExtra(NavigationController.MEASURE_HASH,-1);
                fragment = MeasureItemFragment.create(hash);
                MeasureItemViewModel model_m = obtainViewModel(MeasureItemViewModel.class);

                model_m.getOnSaveHash().observe(this,hashm->{
                    if (hashm != null) {
                        Intent intent = new Intent();
                        intent.putExtra("HASH", hashm.intValue());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                break;
        }

        if (fragment != null)
            fragmentTransaction.replace(R.id.container_container, fragment);
        fragmentTransaction.commit();


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
