package com.vividprojects.protoplanner.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vividprojects.protoplanner.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 23.01.2018.
 */

public class EditTextDialog extends DialogFragment {

    private TextView editText;
    private TextInputLayout editTextLayout;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());



        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_text_dialog, null);

        editText = v.findViewById(R.id.edit_text);
        editTextLayout = v.findViewById(R.id.edit_text_layout);

        editText.setSelected(true);

        setRetainInstance(true);

        String title = "Edit";
        String positiveButton = "OK";
        String negativeButton = "Cancel";

        Bundle b = getArguments();
        if ( b!= null) {
            title = b.getString("TITLE", "Edit");
            editTextLayout.setHint(b.getString("HINT", "Edit text"));
            positiveButton = b.getString("POSITIVE", "OK");
            negativeButton = b.getString("NEGATIVE", "Cancel");
        }

        builder.setView(v)
                .setTitle(title)
                // Add action buttons
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("EDITTEXT",editText.getText().toString());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, resultIntent);
                        EditTextDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditTextDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle b = getArguments();
        if ( b!= null) {
            editText.setText(b.getString("EDITTEXT", ""));
        }
    }
}
