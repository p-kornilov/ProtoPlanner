package com.vividprojects.protoplanner.Interface.Helpers;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.vividprojects.protoplanner.Interface.Dialogs.EditShopDialog;
import com.vividprojects.protoplanner.Interface.Dialogs.EditVariantDialog;

public class DialogFullScreenHelper {
    public static final int DIALOG_VARIANT = 0;
    public static final int DIALOG_SHOP = 1;

    public static void showDialog(int dialogType, Fragment sourceFragment, boolean isFullScreen, int requestCode, Bundle b){
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
                //intent.putExtra("ID",id);
                intent.putExtra("BUNDLE",b);
                sourceFragment.startActivityForResult(intent,requestCode);
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("FULLSCREEN", false);
                //bundle.putString("ID", id);
                bundle.putBundle("BUNDLE",b);

                DialogFragment dialog = DialogFullScreenHelper.createDialog(dialogType);
                dialog.setTargetFragment(sourceFragment, requestCode);
                dialog.setArguments(bundle);

                dialog.show(fm, "Edit main variant");
            }
        }
    }

    public static DialogFullScreenDialogAbstract createDialog(int dialogType) {
        switch (dialogType) {
            case DIALOG_VARIANT:
                return new EditVariantDialog();
            case DIALOG_SHOP:
                return new EditShopDialog();        }

        return null;
    }
}
