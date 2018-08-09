package com.vividprojects.protoplanner.ui.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.images.GlideApp;
import com.vividprojects.protoplanner.ui.fragments.BlockItemFragment;
import com.vividprojects.protoplanner.ui.fragments.RecordItemFragment;
import com.vividprojects.protoplanner.utils.ViewModelHelper;
import com.vividprojects.protoplanner.viewmodels.BlockItemViewModel;
import com.vividprojects.protoplanner.viewmodels.RecordItemViewModel;

import java.io.File;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by Smile on 30.10.2017.
 */

public class BlockActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private BlockItemViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        String id = getIntent().getStringExtra("BLOCK_ID");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }

        model = ViewModelHelper.obtainViewModel(BlockItemViewModel.class, getSupportFragmentManager(), viewModelFactory, this);
        model.getBlockName().observe(this, name -> {
            if (name != null) {
                toolbar.setTitle(name);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final BlockItemFragment fragment = BlockItemFragment.create(id);
        fragmentTransaction.replace(R.id.block_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
