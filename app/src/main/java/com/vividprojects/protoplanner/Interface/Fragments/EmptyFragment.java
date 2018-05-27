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

import com.vividprojects.protoplanner.Adapters.RecordListAdapter;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModels.RecordListViewModel;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class EmptyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.empty_fragment, container, false);
    }
}