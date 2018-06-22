package com.vividprojects.protoplanner.ui.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.ui.fragments.CurrencyItemFragment;
import com.vividprojects.protoplanner.ui.fragments.MeasureItemFragment;
import com.vividprojects.protoplanner.ui.fragments.VariantItemFragment;
import com.vividprojects.protoplanner.ui.helpers.ContainerFragment;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.utils.ViewModelHelper;
import com.vividprojects.protoplanner.viewmodels.CurrencyEditViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.viewmodels.MeasureEditViewModel;
import com.vividprojects.protoplanner.viewmodels.VariantItemViewModel;

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

    ContainerFragment fragment = null;

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

        switch (activityType) {
            case NavigationController.CURRENCY_ITEM:
                getSupportActionBar().setTitle("Currency");
                int iso_code = getIntent().getIntExtra(NavigationController.CURRENCY_ID,-1);
                fragment = CurrencyItemFragment.create(iso_code);
                CurrencyEditViewModel model_c = ViewModelHelper.obtainViewModel(CurrencyEditViewModel.class, getSupportFragmentManager(), viewModelFactory, this);

                model_c.onSave().observe(this,s->{
                    if (s != null) {
                        closeActivity();
                        finish();
                    }
                });
                break;
            case NavigationController.MEASURE_ITEM:
                getSupportActionBar().setTitle("Measure");
                int hash = getIntent().getIntExtra(NavigationController.MEASURE_HASH,-1);
                fragment = MeasureItemFragment.create(hash);
                MeasureEditViewModel model_m = ViewModelHelper.obtainViewModel(MeasureEditViewModel.class, getSupportFragmentManager(), viewModelFactory, this);

                model_m.onSave().observe(this, s->{
                    if (s != null) {
                        closeActivity();
                        finish();
                    }
                });
                break;
            case NavigationController.VARIANT_ITEM:
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                }
                String id = getIntent().getStringExtra(NavigationController.VARIANT_ID);
                fragment = VariantItemFragment.create(id);
                VariantItemViewModel model_v = ViewModelHelper.obtainViewModel(VariantItemViewModel.class, getSupportFragmentManager(), viewModelFactory, this);

                model_v.getVariantItem().observe(this, variant -> {
                    if (variant != null)
                        actionBar.setTitle(variant.title);
                });
                break;
        }

        if (fragment != null)
            fragmentTransaction.replace(R.id.container_container, fragment);
        fragmentTransaction.commit();


    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mdf_action_save) {
            return true;
        } else if (id == android.R.id.home) {
            closeActivity();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
        super.onBackPressed();
    }

    private void closeActivity() {
        Bundle b = fragment.onCloseActivity();
        Intent intent = new Intent();
        intent.putExtra("BUNDLE", b);
        setResult(RESULT_OK, intent);
    }

}
