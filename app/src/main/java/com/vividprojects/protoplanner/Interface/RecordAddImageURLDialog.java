package com.vividprojects.protoplanner.Interface;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.RunnableParam;

/**
 * Created by Smile on 23.01.2018.
 */

public class RecordAddImageURLDialog extends DialogFragment {
    private RunnableParam<String> onOK;
    private EditText url;

    public void setOnOK(RunnableParam<String> onOK) {
        this.onOK = onOK;
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.record_addimage_url, null);
        url = (EditText) v.findViewById(R.id.record_image_url);
        url.setText("http://dgwrlxstb34qz.cloudfront.net/media/article_image/cover/original/56886-6-binatang-ini-lucu-banget-dan-hanya-tinggal-pulau-hokkaido.jpg");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                .setTitle("Image URL")
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        onOK.run(url.getText().toString());
                        RecordAddImageURLDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RecordAddImageURLDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
