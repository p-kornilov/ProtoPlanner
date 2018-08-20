package com.vividprojects.protoplanner.ui.activity;

import android.animation.LayoutTransition;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
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
import android.widget.Toast;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.bindingmodels.LabelsListBindingModel;
import com.vividprojects.protoplanner.databinding.ActivityLabelsBinding;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.ui.dialogs.CreateLabelDialog;
import com.vividprojects.protoplanner.ui.dialogs.DeleteLabelDialog;
import com.vividprojects.protoplanner.utils.Display;
import com.vividprojects.protoplanner.utils.ItemActionsLabel;
import com.vividprojects.protoplanner.utils.Settings;
import com.vividprojects.protoplanner.viewmodel.ViewModelHolder;
import com.vividprojects.protoplanner.viewmodels.LabelsViewModel;
import com.vividprojects.protoplanner.widgets.Chip;
import com.vividprojects.protoplanner.widgets.ChipsLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class LabelsActivity_ extends AppCompatActivity implements HasSupportFragmentInjector, ItemActionsLabel {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    NavigationController navigationController;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private LabelsViewModel model;
    private Chip currentLongPressedChip;

    private boolean startedForResult = false;
    private ActivityLabelsBinding binding;
    private LabelsListBindingModel bindingModel;

    private Runnable onFabClick = () -> {
        if (startedForResult) {
/*            Intent intent = new Intent();
            intent.putExtra("SELECTED", chipsAvailable.getSelected());
            setResult(RESULT_OK, intent);
            finish();*/
        } else {
            openNewLabelDialog();
        }
    };

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_labels_);//ActivityLabelsBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }



        if (getCallingActivity() != null)
            startedForResult = true;

        model = ViewModelProviders.of(this,viewModelFactory).get(LabelsViewModel.class);

        bindingModel = model.getLabelsListBindingModel();
        bindingModel.setContext(this, true, startedForResult);
        bindingModel.setOnFabClick(onFabClick);
        binding.setLabelsModel(bindingModel);

        String[] selectedArray = getIntent().getStringArrayExtra("SELECTED");

        model.getLabels().observe(this, labels -> {
            if (labels != null)
                bindingModel.setLabelsList(labels);
        });

        model.getLabelGroups().observe(this, groups -> {
            if (groups != null)
                bindingModel.setLabelGroupsList(groups);
        });
/*
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
*/
        String id = getIntent().getStringExtra("RECORD_ID");
        model.refreshOriginal(id);

    }

    private void openNewLabelDialog() {
        CreateLabelDialog dialog = new CreateLabelDialog();
        //dialog.setTarget(RecordItemFragment.this,REQUEST_LABELS_SET);
        dialog.show(getSupportFragmentManager(),"Create Label");
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
                    bindingModel.setFilter(filter);
                    Log.d("Test","Entered - Empty");
                } else {
                    Log.d("Test","Entered - " + filter);
                    bindingModel.setFilter(filter);
                }
                return true;
            }
        });

        MenuItem selsort = menu.findItem(R.id.sort_by_select);

        if (Settings.getGeneralSelectedSort(this)) {
            selsort.setChecked(true);
            bindingModel.setSelectedSort(true);
        }

        MenuItem commit = menu.findItem(R.id.commit);
        commit.setVisible(startedForResult);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.sort_by_name:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    bindingModel.setNameSort(true);
                } else {
                    item.setChecked(false);
                    bindingModel.setNameSort(false);
                }
                break;
            case R.id.sort_by_select:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    bindingModel.setSelectedSort(true);
                } else {
                    item.setChecked(false);
                    bindingModel.setSelectedSort(false);
                }
                break;
            case R.id.groups_add:
                openNewLabelDialog();
                break;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemGroupDelete(String groupId) {
        model.deleteGroup(groupId);
    }

    @Override
    public void itemGroupEdit(String groupId) {

    }

    @Override
    public void itemLabelAdd(String groupId) {

    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

