package com.vividprojects.protoplanner.ui.helpers;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vividprojects.protoplanner.R;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class DialogFullScreenActivity extends AppCompatActivity  implements DialogActions, HasSupportFragmentInjector{

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fullscreen);

        Toolbar toolbar = findViewById(R.id.dfs_toolbar);
        setSupportActionBar(toolbar);

        int dialogType = getIntent().getIntExtra("DIALOG_TYPE",-1);
        if (dialogType != -1) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            Bundle bundle = new Bundle();
            bundle.putBoolean("FULLSCREEN", true);
            //String id = getIntent().getStringExtra("ID");
            Bundle b = getIntent().getBundleExtra("BUNDLE");
            bundle.putBundle("BUNDLE",getIntent().getBundleExtra("BUNDLE"));
            if (b != null)
                bundle.putBundle("BUNDLE",b);

            DialogFragment dialog = DialogFullScreenHelper.createDialog(dialogType);
            dialog.setArguments(bundle);

            fragmentTransaction.replace(R.id.dfs_container, dialog).commit();
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public void actionClose() {
        this.finish();
    }

    public void actionCancel() {
        this.finish();
    }

    public void actionResult(String id) {
        Intent i = new Intent();
        i.putExtra("ID", id);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

}

interface DialogActions {
    public void actionClose();
    public void actionCancel();
    public void actionResult(String id);
}
