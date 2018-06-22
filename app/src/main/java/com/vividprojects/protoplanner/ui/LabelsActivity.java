package com.vividprojects.protoplanner.ui;

import android.animation.LayoutTransition;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.vividprojects.protoplanner.ui.dialogs.CreateLabelDialog;
import com.vividprojects.protoplanner.ui.dialogs.DeleteLabelDialog;
import com.vividprojects.protoplanner.viewmodels.LabelsViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.Display;
import com.vividprojects.protoplanner.utils.Settings;
import com.vividprojects.protoplanner.viewmodel.ViewModelHolder;
import com.vividprojects.protoplanner.widgets.Chip;
import com.vividprojects.protoplanner.widgets.ChipsLayout;

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
    private CardView card;
    private Chip currentLongPressedChip;

    private boolean startedForResult = false;

    private ChipsLayout chipsAvailable;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_labels_longpress, menu);
        currentLongPressedChip = (Chip)v;
        model.setCurrentLabel(currentLongPressedChip.getChipId());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.al_edit:
                CreateLabelDialog dialog = new CreateLabelDialog();
                Bundle b = new Bundle();
                b.putInt("COLOR",currentLongPressedChip.getColor());
                b.putString("NAME",currentLongPressedChip.getTitle());
                b.putString("ID",currentLongPressedChip.getChipId());
                dialog.setArguments(b);
             //   dialog.setData(currentLongPressedChip.getName());
                dialog.show(getSupportFragmentManager(),"Create Label");
                return true;
            case R.id.al_delete:
                DeleteLabelDialog ddialog = new DeleteLabelDialog();
                Bundle db = new Bundle();
                db.putInt("COLOR",currentLongPressedChip.getColor());
                db.putString("NAME",currentLongPressedChip.getTitle());
                ddialog.setArguments(db);
                //   dialog.setData(currentLongPressedChip.getName());
                ddialog.show(getSupportFragmentManager(),"Delete Label");
/*
                chipsAvailable.deleteChip(model.getCurrentLabel());
                model.deleteCurrentLabel();
*/
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_labels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chipsAvailable = findViewById(R.id.al_chiplayout_available);
        card = findViewById(R.id.al_card);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;

        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();

        toolBarHeight = (toolBarHeight>0)? toolBarHeight:0;
        int statusBarHeight = Display.calc_pixels(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25);
        int h = heightPixels - toolBarHeight - statusBarHeight;
        if (h>card.getHeight()) card.setMinimumHeight(h);

        if (getCallingActivity() != null)
            startedForResult = true;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startedForResult) {
                    Intent intent = new Intent();
                    intent.putExtra("SELECTED", chipsAvailable.getSelected());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    openNewLabelDialog();
                }

            }
        });

        if (startedForResult)
            fab.setImageResource(R.drawable.ic_check_white_24dp);
        else
            fab.setImageResource(R.drawable.ic_add_white_24dp);

        LayoutTransition transition = chipsAvailable.getLayoutTransition();
        transition.enableTransitionType(LayoutTransition.CHANGING);

        String id = getIntent().getStringExtra("RECORD_ID");

        mViewModelHolder = getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        if (mViewModelHolder == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(ViewModelHolder.createContainer(obtainViewModel()),ViewModelHolder.TAG).commit();
            mViewModelHolder = getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        }

        if (model==null)
            model = ViewModelProviders.of(this,viewModelFactory).get(LabelsViewModel.class);

        String[] selectedArray = getIntent().getStringArrayExtra("SELECTED");

        model.getLabels().observe(this,(labels)->{
            if (labels != null) {
                chipsAvailable.setMode(ChipsLayout.MODE_FULL);
                chipsAvailable.setData(labels,selectedArray,LabelsActivity.this);
            }
        });

        model.getDeleteLabelTrigger().observe(this, deletedId->{
            if (deletedId != null)
                chipsAvailable.deleteChip(deletedId);
        });

        model.getOnNewLabel().observe(this,newLabel->{
            chipsAvailable.insertChip(newLabel, this);
        });

        model.getOnEditLabel().observe(this,editLabel->{
            chipsAvailable.editChip(editLabel);
        });

        model.refreshOriginal(id);
    }

    private void openNewLabelDialog() {
        CreateLabelDialog dialog = new CreateLabelDialog();
        //dialog.setTarget(RecordItemFragment.this,REQUEST_LABELS_SET);
        dialog.show(getSupportFragmentManager(),"Create Label");
    }

    private LabelsViewModel obtainViewModel() {
        model = ViewModelProviders.of(this,viewModelFactory).get(LabelsViewModel.class);
        return model;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_labels, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Test","Entered - Submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                if (TextUtils.isEmpty(filter)) {
/*
                    adapter.filter("");
                    listView.clearTextFilter();
*/
                    chipsAvailable.setFilter(filter);
                    Log.d("Test","Entered - Empty");
                } else {
                    Log.d("Test","Entered - " + filter);
                    chipsAvailable.setFilter(filter);
/*
                    adapter.filter(newText);
*/
                }
                return true;
            }
        });

        MenuItem selsort = menu.findItem(R.id.sort_by_select);

        if (Settings.getGeneralSelectedSort(this)) {
            selsort.setChecked(true);
            chipsAvailable.setSelectedSort(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
/*            case R.id.action_settings:
                break;*/
            case R.id.sort_by_name:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    chipsAvailable.setNameSort(true);
                } else {
                    item.setChecked(false);
                    chipsAvailable.setNameSort(false);
                }
                break;
            case R.id.sort_by_select:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    chipsAvailable.setSelectedSort(true);
                } else {
                    item.setChecked(false);
                    chipsAvailable.setSelectedSort(false);
                }
                break;
            case R.id.labels_add:
                openNewLabelDialog();
                break;
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }


    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

