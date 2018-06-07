package com.vividprojects.protoplanner.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DeleteDialogHelper {
    public static void show(Context context, String message, Runnable deleteFunction) {
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle("Delete");
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFunction.run();
                        dialog.dismiss();
                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
