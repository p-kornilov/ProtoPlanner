package com.vividprojects.protoplanner.Interface;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModel.ViewModelHolder;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.realm.Realm;

/**
 * Created by Smile on 30.10.2017.
 */

public class RecordActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Fragment mViewModelHolder;

    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mViewModelHolder = getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        if (mViewModelHolder == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(ViewModelHolder.createContainer(obtainViewModel()),ViewModelHolder.TAG).commit();
            mViewModelHolder = getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        }


        realm = Realm.getDefaultInstance();
        // initDB();
        String id = getIntent().getStringExtra("RECORD_ID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        String record_title = realm.where(Record.class).equalTo("id",id).findFirst().getMainVariant().getTitle();

        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout1);
        mCollapsingToolbarLayout.setTitle(record_title);

    /*    TextView t = (TextView) findViewById(R.id.textView2);

        t.setText(id);*/

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final RecordItemFragment fragment = RecordItemFragment.create(id);
        fragmentTransaction.replace(R.id.record_container, fragment);
        fragmentTransaction.commit();


        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.activity_record_edit);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fragment.onRecordEdit())
                    ((FloatingActionButton)view).setImageResource(R.drawable.ic_check_white_24dp);
                else
                    ((FloatingActionButton)view).setImageResource(R.drawable.ic_edit_white_24dp);
            }
        });
    }

    private RecordItemViewModel obtainViewModel() {
        return ViewModelProviders.of(this,viewModelFactory).get(RecordItemViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
        return true;
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
