package com.vividprojects.protoplanner.Interface.Activity;

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

import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Interface.Fragments.CurrencyItemFragment;
import com.vividprojects.protoplanner.Interface.Fragments.CurrencyListFragment;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModel.ViewModelHolder;
import com.vividprojects.protoplanner.ViewModels.CurrencyItemViewModel;
import com.vividprojects.protoplanner.ViewModels.CurrencyListViewModel;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class CurrencyListActivity extends AppCompatActivity implements HasSupportFragmentInjector {
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        getSupportActionBar().setTitle("Currency list");
        final CurrencyListFragment fragment = CurrencyListFragment.create();
        obtainViewModel(CurrencyListViewModel.class);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.onFabClick(); //TODO Заменить на model
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

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
