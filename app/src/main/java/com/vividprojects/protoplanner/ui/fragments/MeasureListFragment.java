package com.vividprojects.protoplanner.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.adapters.MeasureListAdapter;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.ui.activity.ContainerListActivity;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.ItemActions;
import com.vividprojects.protoplanner.viewmodels.MeasureListViewModel;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 19.10.2017.
 */

public class MeasureListFragment extends Fragment implements Injectable, ItemActions {
    private RecyclerView recycler;
    private boolean fabVisible = true;
    private MeasureListAdapter measureListAdapter;
    private MeasureListViewModel model;
    private RecyclerView.LayoutManager layoutManager;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("Test", "onCreate - Records Fragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - RootListFragment");
        View v = (View) inflater.inflate(R.layout.fragment_container_list, container, false);
        recycler = (RecyclerView) v.findViewById(R.id.recycler_list);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fabVisible) {
                    ((ContainerListActivity) getActivity()).hideFab();
                    fabVisible = false;
                } else if (dy < 0 && !fabVisible) {
                    ((ContainerListActivity) getActivity()).showFab();
                    fabVisible = true;
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

 //       recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        model = ViewModelProviders.of(getActivity(),viewModelFactory).get(MeasureListViewModel.class);

        Bundle args = getArguments();

/*        if (args != null && args.containsKey("FILTER")){  // TODO Сделать восстановление состояния фильтра и ожет быть чего другого
        //    model.setFilter();
            model.setFilter(args.getStringArrayList("FILTER"));
        } else {
            model.setFilter(null);
        }*/

        model.setFilter("");

        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        //MeasureListAdapter = new MeasureListAdapter(this,(LinearLayoutManager)layoutManager);
        measureListAdapter = new MeasureListAdapter(R.layout.measure_item,R.layout.measure_item_header,this.getContext(),this);
        recycler.setAdapter(measureListAdapter);


        model.getList().observe(this, list -> {
            if (list != null)
//                recycler.setAdapter(new CurrencyListAdapter(list,getActivity()));
                measureListAdapter.setData(list);
        });

        model.getRefreshMeasure().observe(this, measure -> {
            if (measure != null) {};
//                recycler.setAdapter(new CurrencyListAdapter(list,getActivity()));
                measureListAdapter.refresh(measure);
        });

        model.getOnNewTrigger().observe(this, o -> {
            NavigationController.openMeasureForResult(0,MeasureListFragment.this);
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

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
                    measureListAdapter.setFilter(filter);
                    Log.d("Test","Entered - Empty");
                } else {
                    Log.d("Test","Entered - " + filter);
                    measureListAdapter.setFilter(filter);
/*
                    adapter.filter(newText);
*/
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.search:
/*
                EditTextDialog editNameDialog = new EditTextDialog();
                editNameDialog.setTargetFragment(this, REQUEST_EDIT_NAME);
                Bundle b = new Bundle();
                b.putString("TITLE","Edit note");
                b.putString("HINT","Name");
                b.putString("POSITIVE","Save");
                b.putString("NEGATIVE","Cancel");
                b.putString("EDITTEXT",recordName);
                editNameDialog.setArguments(b);
                editNameDialog.show(getFragmentManager(), "Edit name");
*/
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NavigationController.REQUEST_CODE_MEASURE:
                if (resultCode == RESULT_OK && data != null) {
                    Bundle b = data.getBundleExtra("BUNDLE");
                    if (b != null) {
                        int id = b.getInt("HASH",-1);
                        model.refresh(id);
                    }
                }
                return;
        }
    }

    public void onFabClick() {
        NavigationController.openCurrencyForResult(-1, this);
    }

    public static MeasureListFragment create() {
        MeasureListFragment measureListFragment = new MeasureListFragment();
        return measureListFragment;
    }

    @Override
    public void itemDelete(int item) {
        model.deleteMeasure(item);
    }

    @Override
    public void itemEdit(int item) {
        NavigationController.openMeasureForResult(item,MeasureListFragment.this);
    }

    @Override
    public void itemDefault(int item) {
        model.setDefaultMeasure(item);
    }
}