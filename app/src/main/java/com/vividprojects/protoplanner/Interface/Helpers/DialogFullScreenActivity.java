package com.vividprojects.protoplanner.Interface.Helpers;

import android.arch.lifecycle.ViewModelProvider;
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
            String id = getIntent().getStringExtra("ID");
            if (id != null)
                bundle.putString("ID", id);

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

}

interface DialogActions {
    public void actionClose();
    public void actionCancel();
}
