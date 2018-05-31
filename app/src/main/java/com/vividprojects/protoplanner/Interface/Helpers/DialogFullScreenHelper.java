package com.vividprojects.protoplanner.Interface.Helpers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.vividprojects.protoplanner.Interface.Dialogs.EditVariantDialog;
import com.vividprojects.protoplanner.Interface.Dialogs.EditVariantDialog_;

public class DialogFullScreenHelper {
    public static final int DIALOG_VARIANT = 0;

    public static void showDialog(int dialogType, Fragment sourceFragment, boolean isFullScreen, int requestCode){
        FragmentManager fm = sourceFragment.getFragmentManager();
        if (fm != null) {
            if (isFullScreen) {
                Intent intent;
                if (Build.VERSION.SDK_INT <= 23) {
                    intent = new Intent(sourceFragment.getActivity(), DialogFullScreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    intent = new Intent(sourceFragment.getActivity(), DialogFullScreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                intent.putExtra("DIALOG_TYPE",dialogType);
                sourceFragment.startActivityForResult(intent,requestCode);
            } else {
//            editVariantDialog.setTargetFragment(RecordItemFragment.this, REQUEST_EDIT_VARIANT);
                Bundle bundle = new Bundle();
                bundle.putBoolean("FULLSCREEN", false);

                DialogFragment dialog = DialogFullScreenHelper.createDialog(dialogType);
                dialog.setArguments(bundle);

                dialog.show(fm, "Edit main variant");
            }
        }
    }

    public static DialogFullScreenDialogAbstract createDialog(int dialogType) {
        switch (dialogType) {
            case DIALOG_VARIANT:
                return new EditVariantDialog_();
        }

        return null;
    }
}
