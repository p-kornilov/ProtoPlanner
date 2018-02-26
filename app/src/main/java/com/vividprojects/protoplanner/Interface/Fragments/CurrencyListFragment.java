package com.vividprojects.protoplanner.Interface.Fragments;

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

import com.vividprojects.protoplanner.Adapters.CurrencyListAdapter;
import com.vividprojects.protoplanner.Adapters.RecordListAdapter;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Presenters.CurrencyListViewModel;
import com.vividprojects.protoplanner.Presenters.RecordListViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.TMP.TestRecyclerAdapter;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class CurrencyListFragment extends Fragment implements Injectable {
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
        View v = (View) inflater.inflate(R.layout.fragment_container_list, container, false);
        recycler = (RecyclerView) v.findViewById(R.id.recycler_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
     //   recycler.addItemDecoration(mDividerItemDecoration);

        recycler.setAdapter(new TestRecyclerAdapter(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

 //       recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        final CurrencyListViewModel model = ViewModelProviders.of(getActivity(),viewModelFactory).get(CurrencyListViewModel.class);

        Bundle args = getArguments();

/*        if (args != null && args.containsKey("FILTER")){  // TODO Сделать восстановление состояния фильтра и ожет быть чего другого
        //    model.setFilter();
            model.setFilter(args.getStringArrayList("FILTER"));
        } else {
            model.setFilter(null);
        }*/

        model.setFilter("");

        model.getList().observe(this,list -> {
            if (list != null)
                recycler.setAdapter(new CurrencyListAdapter(list,,getActivity()));
        });
    }
}