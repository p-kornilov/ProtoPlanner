package com.vividprojects.protoplanner;

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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.ui.BlockListFragment;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.ui.fragments.RecordItemFragment;
import com.vividprojects.protoplanner.ui.fragments.RecordListFragment;
import com.vividprojects.protoplanner.utils.FabActions;
import com.vividprojects.protoplanner.viewmodels.BlockListViewModel;
import com.vividprojects.protoplanner.viewmodels.RecordListViewModel;
import com.vividprojects.protoplanner.widgets.FabManager;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener, FabManager {
    private final static String FRAGMENT_RECORD = "FRAGMENT_RECORD";
    private final static String FRAGMENT_BLOCK = "FRAGMENT_BLOCK";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    DataRepository dataRepository;

    @Inject
    NavigationController navigationController;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Toolbar secondToolBar;
    private FloatingActionButton fab;
    private FabActions model;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.nav_drawer_main);
//        setContentView(R.layout.activity_main);
        Toolbar toolbar;

        drawer = findViewById(R.id.drawer_layout);

        navigationController.setCurrentActivity(this);

        if (navigationController.isTablet()) {
            toolbar = (Toolbar) findViewById(R.id.toolbar1);
            setSupportActionBar(toolbar);
            secondToolBar = (Toolbar) findViewById(R.id.toolbar2);
        } else {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        replaceListFragment(FRAGMENT_RECORD);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model != null)
                    model.actionAdd();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Log.i("Test", "------------------------------ Dimension width " + getResources().getConfiguration().screenWidthDp);
        Log.i("Test", "------------------------------ Dimension height " + dataRepository.getHeight());
        Log.i("Test", "------------------------------ Type of the device " + dataRepository.getType());

    }

    private void replaceListFragment(String type) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = null;
        switch (type) {
            case FRAGMENT_RECORD:
                fragment = new RecordListFragment();
                fragmentTransaction.replace(R.id.lists_container, fragment, FRAGMENT_RECORD);
                fragmentTransaction.commit();
                model = ViewModelProviders.of(this,viewModelFactory).get(RecordListViewModel.class);
                break;
            case FRAGMENT_BLOCK:
                fragment = new BlockListFragment();
                fragmentTransaction.replace(R.id.lists_container, fragment, FRAGMENT_BLOCK);
                fragmentTransaction.commit();
                model = ViewModelProviders.of(this,viewModelFactory).get(BlockListViewModel.class);
        }
    }

    public Toolbar getSecondToolBar() {
        return secondToolBar;
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public void setRecordItem(String id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final RecordItemFragment fragment = RecordItemFragment.create(id);
        fragmentTransaction.replace(R.id.item_container, fragment);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_records:
                replaceListFragment(FRAGMENT_RECORD);
                break;
            case R.id.nav_blocks:
                replaceListFragment(FRAGMENT_BLOCK);
                break;
            case R.id.nav_labels:
                navigationController.openLabels();
                break;
            case R.id.nav_currency:
                navigationController.openCurrencies();
                break;
            case R.id.nav_measure:
                navigationController.openMeasures();
                break;
            case R.id.nav_settings:
                navigationController.openSettings();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
