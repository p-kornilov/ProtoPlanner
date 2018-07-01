package com.vividprojects.protoplanner.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.adapters.RecordListAdapter;
import com.vividprojects.protoplanner.adapters.RecordListAdapter_;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.viewmodels.RecordListViewModel;
import com.vividprojects.protoplanner.R;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class RecordListFragment extends Fragment implements Injectable {
    private RecyclerView recycler;
    private boolean fabVisible = true;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View v = (View) inflater.inflate(R.layout.records_fragment, container, false);
        recycler = (RecyclerView) v.findViewById(R.id.recycler_records);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
       // layoutManager.setAutoMeasureEnabled(true);
        recycler.setLayoutManager(layoutManager);
       // recycler.setNestedScrollingEnabled(false);
       // recycler.setFocusable(false);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recycler.addItemDecoration(mDividerItemDecoration);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fabVisible) {
                    ((MainActivity) getActivity()).hideFab();
                    fabVisible = false;
                } else if (dy < 0 && !fabVisible) {
                    ((MainActivity) getActivity()).showFab();
                    fabVisible = true;
                }
            }
        });

//        recycler.setAdapter(new TestRecyclerAdapter(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       // recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        final RecordListViewModel model = ViewModelProviders.of(getActivity(),viewModelFactory).get(RecordListViewModel.class);

        Bundle args = getArguments();

        if (args != null && args.containsKey("FILTER")){  // TODO Сделать восстановление состояния фильтра и ожет быть чего другого
        //    model.setFilter();
            model.setFilter(args.getStringArrayList("FILTER"));
        } else {
            model.setFilter(null);
        }

        RecordListAdapter_ adapter = new RecordListAdapter_(getContext(),null);
        recycler.setAdapter(adapter);

        model.getList().observe(this,resource -> {
            if (resource.data != null)
                adapter.setData(resource.data);
                //recycler.setAdapter(new RecordListAdapter_(resource.data,getActivity()));
//            recycler.setAdapter(new RecordListAdapter(resource.data,getActivity()));
        });
    }
}