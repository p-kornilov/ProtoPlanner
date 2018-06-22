package com.vividprojects.protoplanner.ui.helpers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.R;

import static android.app.Activity.RESULT_OK;

public abstract class DialogFullScreenDialogAbstract extends DialogFragment {
    private static final String EMPTY_FRAGMENT = "EMPTY FRAGMENT";

    private AlertDialog dialog;
    private MenuItem saveMenu;
    private boolean isFullScreen = false;
    private DialogActions hostingActivity;

    public void enableButtons() {
        if (isFullScreen) {
            if (saveMenu != null) {
                saveMenu.setIcon(R.drawable.ic_check_white_24dp);
                saveMenu.setEnabled(true);
            }
        } else
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
    }

    public void disableButtons() {
        if (isFullScreen) {
            if (saveMenu != null) {
                saveMenu.setIcon(R.drawable.ic_check_disabled_24dp);
                saveMenu.setEnabled(false);
            }
        } else
            dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (isFullScreen) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
                actionBar.setTitle("Dialog title");
            }
            setHasOptionsMenu(true);
            return getRootView();
        } else
            return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null)
            isFullScreen = b.getBoolean("FULLSCREEN", false);
        if (isFullScreen) {
            try {
                hostingActivity = (DialogActions) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(hostingActivity.toString()
                        + " must implement DialogActions");
            }
        }
    }

    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
        if (isFullScreen) {
            return super.onCreateDialog(savedInstanceState);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setView(getRootView())
                    .setTitle("Basic variant")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            onSave();
                            returnResult();
                        }
                    })
                    .setNegativeButton("Cancel", null);
            dialog = builder.create();
            return dialog;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_dialog_fullscreen, menu);
        saveMenu = menu.findItem(R.id.mdf_action_save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mdf_action_save) {
            onSave();
            hostingActivity.actionResult(getResult());
            return true;
        } else if (id == android.R.id.home) {
            hostingActivity.actionCancel();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void returnResult() {
        Fragment tFragment = getTargetFragment();
        if (tFragment != null) {
            Intent intent = new Intent();
            intent.putExtra("ID", getResult());
            tFragment.onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeModels();
    }

    public abstract View getRootView();

    public abstract void observeModels();

    public abstract void onSave();

    public abstract String getResult();

}
