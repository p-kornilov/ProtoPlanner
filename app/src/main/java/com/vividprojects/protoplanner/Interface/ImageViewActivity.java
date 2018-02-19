package com.vividprojects.protoplanner.Interface;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.Presenters.ImageViewViewModel;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.ViewModel.ViewModelHolder;
import com.vividprojects.protoplanner.Widgets.TouchableViewPager;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.realm.Realm;

/**
 * Created by Smile on 30.10.2017.
 */

public class ImageViewActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Fragment mViewModelHolder;
    private ImageViewViewModel model;
    private TouchableViewPager viewPager;
    private boolean requestListener = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mViewModelHolder = getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        if (mViewModelHolder == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(ViewModelHolder.createContainer(obtainViewModel()),ViewModelHolder.TAG).commit();
            mViewModelHolder = getSupportFragmentManager().findFragmentByTag(ViewModelHolder.TAG);
        }

        int position = getIntent().getIntExtra("POSITION",0);
        String variant = getIntent().getStringExtra("VARIANT_ID");

        model.getV().observe(this,(item)->{});
        model.setVariantId(variant);

        model.getImages().observe(this,(item)->{

            ImageViewActivity.SectionsPagerAdapter mSectionsPagerAdapter = new ImageViewActivity.SectionsPagerAdapter(getSupportFragmentManager(),item.size());
            viewPager = findViewById(R.id.iv_pager);
            viewPager.setAdapter(mSectionsPagerAdapter);
            viewPager.setCurrentItem(position);
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private ImageViewViewModel obtainViewModel() {
        model = ViewModelProviders.of(this,viewModelFactory).get(ImageViewViewModel.class);
        return model;
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

        Log.d("Listener", "Listener000 - " + requestListener);

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.change_listener) {
            requestListener = !requestListener;
            viewPager.requestDisallowInterceptTouchEvent(requestListener);
      //      viewPager.requestD
            Log.d("Listener", "Listener - " + requestListener);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int count;

        public SectionsPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.count = count;
        }

        @Override
        public Fragment getItem(int position) {

            ImageViewFragment fragment = new ImageViewFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION",position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
