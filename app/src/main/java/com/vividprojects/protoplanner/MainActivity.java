package com.vividprojects.protoplanner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Interface.BlockFragment;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Interface.RecordItemFragment;
import com.vividprojects.protoplanner.Interface.RecordListFragment;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    DataRepository dataRepository;

    @Inject
    NavigationController navigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (navigationController.isTablet()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            final RecordListFragment fragment = new RecordListFragment();
            fragmentTransaction.replace(R.id.lists_container, fragment);
            fragmentTransaction.commit();

            setRecordItem("Empty");

        } else {
            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tb = (TabLayout) findViewById(R.id.my_tab);
            tb.setupWithViewPager(mViewPager);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

/*        int color = tv.getCurrentTextColor();
        int A = (color >> 24) & 0xff; // or color >>> 24
        int R = (color >> 16) & 0xff;
        int G = (color >>  8) & 0xff;
        int B = (color      ) & 0xff;

        tv.setText("Color is: A=" + A + " R=" + R + "G=" + G + "B=" + B);*/

       // AppComponent dmc = ((PPApplication) getApplication()).getMainComponent();
        //dmc.inject(this);

        //dataRepository.setContext(this);

        Log.i("Test", "------------------------------ Dimension width " + getResources().getConfiguration().screenWidthDp);
        Log.i("Test", "------------------------------ Dimension height " + dataRepository.getHeight());
        Log.i("Test", "------------------------------ Type of the device " + dataRepository.getType());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }


    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public void setRecordItem(String id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final RecordItemFragment fragment = RecordItemFragment.create(id);
        fragmentTransaction.replace(R.id.item_container, fragment);
        fragmentTransaction.commit();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new BlockFragment();
//            return new FirstFragment();
            else {
                return new RecordListFragment();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            CharSequence returnTitle;
            switch (position) {
                case 0:
                    returnTitle = "Blocks";
                    break;
                case 1:
                    returnTitle = "Apart Records";
                    break;
                default:
                    returnTitle = "null";
            }
            return returnTitle;
        }
    }
}
