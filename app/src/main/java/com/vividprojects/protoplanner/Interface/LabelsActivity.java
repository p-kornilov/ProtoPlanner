package com.vividprojects.protoplanner.Interface;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Presenters.LabelsViewModel;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModel.ViewModelHolder;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class LabelsActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    NavigationController navigationController;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Fragment mViewModelHolder;
    private LabelsViewModel model;

    private ChipsLayout chipsAvailable;
    private ChipsLayout chipsSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_labels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chipsAvailable = findViewById(R.id.al_chiplayout_available);
        chipsSelected = findViewById(R.id.al_chiplayout_selected);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String id = getIntent().getStringExtra("RECORD_ID");

        mViewModelHolder = getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        if (mViewModelHolder == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(ViewModelHolder.createContainer(obtainViewModel()),ViewModelHolder.TAG).commit();
            mViewModelHolder = getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        }

        if (model==null)
            model = ViewModelProviders.of(this,viewModelFactory).get(LabelsViewModel.class);

        model.getLabels().observe(this,(labels)->{
            if (labels != null) {
                chipsAvailable.removeAllViews();
                chipsAvailable.noneChip(this);

                for (Label.Plain label : labels) {
                    Chip chip = new Chip(this);
                    chip.setTitle(label.name);
                    chip.setColor(label.color);
                    chip.setDeleteButtonVisible(true);
                    chip.setDeleteButtonStyle(R.drawable.ic_check_circle_grey_24dp);
                    chipsAvailable.addView(chip);
                }
            }
        });

        model.getLabelsRecord(id).observe(this,(labels)->{
            if (labels != null) {
                chipsSelected.removeAllViews();
                chipsSelected.noneChip(this);

                for (Label.Plain label : labels) {
                    Chip chip = new Chip(this);
                    chip.setTitle(label.name);
                    chip.setColor(label.color);
                    chip.setDeleteButtonVisible(true);
                    //chip.setDeleteButtonStyle(R.drawable.ic_check_circle_grey_24dp);
                    chipsSelected.addView(chip);
                }
            }
        });

    //    chipsSelected.removeAllViews();
    //    chipsSelected.noneChip(this);
    }

    private LabelsViewModel obtainViewModel() {
        model = ViewModelProviders.of(this,viewModelFactory).get(LabelsViewModel.class);
        return model;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
