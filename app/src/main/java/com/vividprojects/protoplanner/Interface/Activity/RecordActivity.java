package com.vividprojects.protoplanner.Interface.Activity;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.vividprojects.protoplanner.Interface.Fragments.CurrencyFragment;
import com.vividprojects.protoplanner.Interface.Fragments.RecordItemFragment;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModel.ViewModelHolder;

import java.util.List;

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

    private ViewModelHolder<RecordItemViewModel> mViewModelHolder;
    private RecordItemViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        String id = getIntent().getStringExtra("RECORD_ID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout1);

//        model = ViewModelProviders.of(this, viewModelFactory).get(RecordItemViewModel.class);
        model = obtainViewModel();
        model.getRecordName().observe(this, name -> {
            if (name != null) {
                mCollapsingToolbarLayout.setTitle(name);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final RecordItemFragment fragment = RecordItemFragment.create(id);
        fragmentTransaction.replace(R.id.record_container, fragment);
        fragmentTransaction.commit();


/*        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.activity_record_edit);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fragment.onRecordEdit())
                    ((FloatingActionButton)view).setImageResource(R.drawable.ic_check_white_24dp);
                else
                    ((FloatingActionButton)view).setImageResource(R.drawable.ic_edit_white_24dp);
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

/*
        switch (id) {
            case R.id.ra_edit_name:
                EditTextDialog editNameDialog = new EditTextDialog();
              //  editNameDialog.setTargetFragment(this, REQUEST_EDIT_NAME);
                //addImageURLDialog.show(getFragmentManager(), "Add_image_url");
                break;
        }
        return true;
*/
        return false;
        //return super.onOptionsItemSelected(item);
    }

    private RecordItemViewModel obtainViewModel() {
        ViewModelHolder<RecordItemViewModel> viewModelHolder = (ViewModelHolder<RecordItemViewModel>)getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        if (viewModelHolder != null && viewModelHolder.getViewModel() != null) {
            return viewModelHolder.getViewModel();
        } else {
            RecordItemViewModel model = ViewModelProviders.of(this,viewModelFactory).get(RecordItemViewModel.class);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fff = ViewModelHolder.createContainer(model);
            ft.add(fff,ViewModelHolder.TAG).commit();
            return model;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
