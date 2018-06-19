package com.vividprojects.protoplanner.Interface.Activity;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.vividprojects.protoplanner.Interface.Fragments.ImageViewFragment;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Utils.Display;
import com.vividprojects.protoplanner.ViewModels.ImageViewViewModel;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Widgets.TouchableViewPager;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by Smile on 30.10.2017.
 */

public class ImageViewActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private Fragment mViewModelHolder;
    private ImageViewViewModel model;
    private TouchableViewPager viewPager;
    private ImageButton defaultImage;
    private View scrim;
    private int defaultImageP = 0;
    private boolean requestListener = false;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        scrim = findViewById(R.id.iv_scrim);

        defaultImage = findViewById(R.id.iv_default);
        defaultImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultImageP = currentPosition;
                defaultImage.setImageResource(R.drawable.ic_star_default_gold);
            }
        });

        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(defaultImage.getLayoutParams());
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        CoordinatorLayout.LayoutParams lps = new CoordinatorLayout.LayoutParams(scrim.getLayoutParams());
        lps.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        int orientation = getResources().getConfiguration().orientation;

        if (navigationController.isTablet() || orientation == Configuration.ORIENTATION_PORTRAIT) {
            lp.bottomMargin = Display.calc_pixels(64);
            lps.bottomMargin = Display.calc_pixels(48);
        } else {
            lp.bottomMargin = 16;
            lps.bottomMargin = 0;
        }
        defaultImage.setLayoutParams(lp);
        scrim.setLayoutParams(lps);


        viewPager = findViewById(R.id.iv_pager);

        int position = getIntent().getIntExtra("POSITION",0);
        String variant = getIntent().getStringExtra("VARIANT_ID");

        model = ViewModelProviders.of(this,viewModelFactory).get(ImageViewViewModel.class);

        model.getV().observe(this,(item)->{});
        model.setVariantId(variant);

        model.getImages().observe(this,(item)->{
            if (item != null) {

                defaultImageP = item.second;

                ImageViewActivity.SectionsPagerAdapter mSectionsPagerAdapter = new ImageViewActivity.SectionsPagerAdapter(getSupportFragmentManager(), item.first.size());
                viewPager.setAdapter(mSectionsPagerAdapter);
                viewPager.setCurrentItem(position);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (currentPosition != position) {
                            currentPosition = position;
                            if (currentPosition == defaultImageP)
                                defaultImage.setImageResource(R.drawable.ic_star_default_gold);
                            else
                                defaultImage.setImageResource(R.drawable.ic_star_default_black);
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {}

                    @Override
                    public void onPageScrollStateChanged(int state) {}
                });
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("DEFAULT_IMAGE", defaultImageP);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
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
