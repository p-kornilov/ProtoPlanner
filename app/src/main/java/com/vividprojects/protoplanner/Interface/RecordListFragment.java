package com.vividprojects.protoplanner.Interface;

import android.app.Activity;
import android.arch.lifecycle.Observer;
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
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.MainCommunication;
import com.vividprojects.protoplanner.Presenters.RecordListFragmentViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.TMP.TestRecyclerAdapter;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Smile on 19.10.2017.
 */

public class RecordListFragment extends Fragment {
    RecyclerView recycler;
    //rwRecordsAdapter adapter;
    Realm realm;
    String[] testarray;
    MainCommunication mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test", "onCreate - Records Fragment");
        realm = Realm.getDefaultInstance();

        Log.d("Test", "------------------------------ Labels in records:");
        RealmResults<Label> ls = realm.where(Label.class).findAll();

        testarray = new String[ls.size()];
        for (int i = 0; i<ls.size();i++) {
            Log.d("Test", ls.get(i).toString());
            testarray[i] = ls.get(i).getName();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = getActivity();
        try {
            mCallback = (MainCommunication) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MainCommunication");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - RootListFragment");
        View v = (View) inflater.inflate(R.layout.records_fragment, container, false);
        recycler = (RecyclerView) v.findViewById(R.id.recycler_records);

        String[] ar = {
                "Test1",
                "Test2",
                "Test3",
                "Test4",
                "Test5",
                "Test6",
                "Test7",
                "Test8",
                "Test9",
                "Test10",
                "Test11",
                "Test12",
        };

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
 //       recycler.setAdapter(new TestRecyclerAdapter(ar,getActivity()));
//        recycler.setAdapter(new MyAdapter(testarray,getContext()));
 //       RealmResults<Record> ls = realm.where(Record.class).findAll();

        final RecordListFragmentViewModel model = ViewModelProviders.of(getActivity()).get(RecordListFragmentViewModel.class);

        recycler.setAdapter(new RecordListAdapter(model.getTest(),getActivity().getApplicationContext()));
/*        model.getList().observe(this,new Observer<RealmResults<Record>>() {
            @Override
            public void onChanged(@Nullable final RealmResults<Record> list) {
                // Update the UI, in this case, a TextView.
            //    recycler.setAdapter(new RecordListAdapter(model.getTest()));
            }
        });*/

        for (Record r:model.getTest()) {
            Log.d("Test", "Record - " + r.getId() + " " + r.getMainVariant().getTitle());
        }

    //    recycler.setAdapter(new RecordListAdapter(ls,mCallback));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("Test", "onCreate - Records Fragment - Activity");
    }
}
