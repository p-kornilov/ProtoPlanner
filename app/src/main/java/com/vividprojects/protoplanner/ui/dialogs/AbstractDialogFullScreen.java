package com.vividprojects.protoplanner.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.RunnableParam;

/**
 * Created by Smile on 23.01.2018.
 */

public abstract class AbstractDialogFullScreen extends DialogFragment {
    private static final String EMPTY_FRAGMENT = "EMPTY FRAGMENT";

    private AlertDialog dialog;
    private MenuItem saveMenu;
    private boolean isFullScreen = false;
    private boolean isClosing = false;

    private RunnableParam<Integer> enableCheck = (error) -> {
        if (error == 1) {
            if (isFullScreen) {
                if (saveMenu != null) {
                    saveMenu.setIcon(R.drawable.ic_check_disabled_24dp);
                    saveMenu.setEnabled(false);
                }
            } else
                dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
        } else {
            if (isFullScreen) {
                if (saveMenu != null) {
                    saveMenu.setIcon(R.drawable.ic_check_white_24dp);
                    saveMenu.setEnabled(true);
                }
            } else
                dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
        }
    };

    public RunnableParam<Integer> getEnableCheck() {
        return enableCheck;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (isFullScreen) {
            View rootView = getRootView();
            Toolbar toolbar = rootView.findViewById(R.id.toolbar);
            toolbar.setTitle("Dialog title");

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            }
            setHasOptionsMenu(true);
            return rootView;
        } else
            return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null)
            isFullScreen = b.getBoolean("FULLSCREEN", false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (isFullScreen) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setHasOptionsMenu(true);
            return dialog;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setView(getRootView())
                    .setTitle("Basic variant")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            onSave();
                        }
                    })
                    .setNegativeButton("Cancel", null);
            dialog = builder.create();
            return dialog;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isClosing)
            return;
        menu.clear();
        inflater.inflate(R.menu.menu_dialog_fullscreen, menu);
        saveMenu = menu.findItem(R.id.mdf_action_save);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mdf_action_save) {
            isClosing = true;
            onSave();
           // closeEmptyFragment();
            getActivity().invalidateOptionsMenu();
            FragmentManager fm = getFragmentManager();
            if (fm != null)
                fm.popBackStack();
           // dismiss();
            return true;
        } else if (id == android.R.id.home) {
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeModels();
    }

    public void showDialog(FragmentManager fragmentManager, boolean isFullScreen){
        if (isFullScreen) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, this).addToBackStack(EMPTY_FRAGMENT).commit();
        } else {
//            editVariantDialog.setTargetFragment(RecordItemFragment.this, REQUEST_EDIT_VARIANT);
            this.show(fragmentManager, "Edit main variant");
        }
    }

    private void closeEmptyFragment() {
        getActivity().invalidateOptionsMenu();
        FragmentManager fm = getFragmentManager();
        if (fm != null)
            fm.beginTransaction().remove(fm.findFragmentByTag(EMPTY_FRAGMENT)).commitNowAllowingStateLoss();
/*        FragmentManager fm = getFragmentManager();
        if (fm != null)
            fm.beginTransaction().remove(fm.findFragmentByTag(EMPTY_FRAGMENT)).commit();*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
    //    closeEmptyFragment();
    }

    public abstract View getRootView();

    public abstract void observeModels();

    public abstract void onSave();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().invalidateOptionsMenu();
    }



    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }
}
