package com.vividprojects.protoplanner.Interface.Activity;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Interface.Fragments.RecordItemFragment;
import com.vividprojects.protoplanner.Utils.ViewModelHelper;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModel.ViewModelHolder;

import java.io.File;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by Smile on 30.10.2017.
 */

public class RecordActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecordItemViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        String id = getIntent().getStringExtra("RECORD_ID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.toolbar_layout1);
        ImageView defaultImage = findViewById(R.id.ar_default_image);

        model = ViewModelHelper.obtainViewModel(RecordItemViewModel.class, getSupportFragmentManager(), viewModelFactory, this);
        model.getRecordName().observe(this, name -> {
            if (name != null) {
                mCollapsingToolbarLayout.setTitle(name);
            }
        });

        model.getDefaultImage().observe(this, image -> {
            GlideApp.with(defaultImage)
                    .load(new File(image))
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(defaultImage);
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final RecordItemFragment fragment = RecordItemFragment.create(id);
        fragmentTransaction.replace(R.id.record_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
