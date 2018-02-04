package com.vividprojects.protoplanner.Interface;

import android.animation.LayoutTransition;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Presenters.LabelsViewModel;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Display;
import com.vividprojects.protoplanner.ViewModel.ViewModelHolder;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static java.security.AccessController.getContext;

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

    private ChipsLayout chipsAvailable;

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

/*

*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //chipsAvailable.moveChild(25,23);
                chipsAvailable.setSelectedSort(true);

            }
        });

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

        model.getLabels().observe(this,(labels)->{
            if (labels != null) {
/*
                chipsAvailable.removeAllViews();
                chipsAvailable.noneChip(this);

*/
                chipsAvailable.setMode(ChipsLayout.MODE_FULL);
                chipsAvailable.setData(labels,null);

/*
                for (Label.Plain label : labels) {
                    Chip chip = new Chip(this);
                    chip.setTitle(label.name);
                    chip.setDeleteButtonVisible(true);
                    chip.setDeleteButtonStyle(R.drawable.ic_check_circle_grey_24dp);
                    chip.setColor(label.color);
*/
/*                    LayoutTransition t = chip.getLayoutTransition();
                    t.enableTransitionType(LayoutTransition.CHANGING);*//*

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        chip.setElevation(Display.calc_pixels(8));
                    }
                    chipsAvailable.addView(chip);
                }
*/
            }
        });

        model.refreshOriginal(id);

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

        if (id == R.id.sort_by_name) {
            //chipsAvailable.sortByName();
            chipsAvailable.setNameSort(true);
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
