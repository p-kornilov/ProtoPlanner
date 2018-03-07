package com.vividprojects.protoplanner.Interface.Activity;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Interface.Fragments.CurrencyFragment;
import com.vividprojects.protoplanner.Interface.Fragments.CurrencyListFragment;
import com.vividprojects.protoplanner.Interface.Fragments.RecordItemFragment;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Interface.RecordListFragment;
import com.vividprojects.protoplanner.Presenters.CurrencyListViewModel;
import com.vividprojects.protoplanner.Presenters.CurrencyViewModel;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModel.ViewModelHolder;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ContainerActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    DataRepository dataRepository;

    @Inject
    NavigationController navigationController;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        int activityType = getIntent().getIntExtra(NavigationController.ACTIVITY_TYPE,0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;

        switch (activityType) {
            case NavigationController.CURRENCY_LIST:
                fragment = new CurrencyListFragment();
                obtainViewModel(CurrencyListViewModel.class);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
                break;
            case NavigationController.CURRENCY_ITEM:
                fragment = new CurrencyFragment();
                obtainViewModel(CurrencyViewModel.class);

                fab.setVisibility(View.GONE);
                break;
        }

        if (fragment != null)
            fragmentTransaction.replace(R.id.container_container, fragment);
        fragmentTransaction.commit();


    }

    public void hideFab() {
        fab.hide();
    }

    public void showFab() {
        fab.show();
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
