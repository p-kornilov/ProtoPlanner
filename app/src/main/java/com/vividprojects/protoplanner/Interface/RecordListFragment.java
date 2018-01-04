package com.vividprojects.protoplanner.Interface;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.Adapters.RecordListAdapter;
import com.vividprojects.protoplanner.CoreData.Filter;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Presenters.RecordListViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.TMP.TestRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Smile on 19.10.2017.
 */

public class RecordListFragment extends Fragment implements Injectable {
    private RecyclerView recycler;

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


//        recycler.setAdapter(new TestRecyclerAdapter(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        final RecordListViewModel model = ViewModelProviders.of(getActivity(),viewModelFactory).get(RecordListViewModel.class);

        Bundle args = getArguments();

        if (args != null && args.containsKey("FILTER")){  // TODO Сделать восстановление состояния фильтра и ожет быть чего другого
        //    model.setFilter();
            model.setFilter(args.getStringArrayList("FILTER"));
        } else {
            model.setFilter(null);
        }

        model.getList().observe(this,resource -> {
            if (resource.data != null)
                recycler.setAdapter(new RecordListAdapter(resource.data,getActivity()));
        });
    }
}