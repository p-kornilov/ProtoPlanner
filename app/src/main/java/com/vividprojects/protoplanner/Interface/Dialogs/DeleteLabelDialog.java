package com.vividprojects.protoplanner.Interface.Dialogs;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Presenters.LabelsViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class DeleteLabelDialog extends DialogFragment implements Injectable {

    private ChipsLayout label;

    private LabelsViewModel model;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.delete_label_dialog, null);

        label = v.findViewById(R.id.chipLayout);

        setRetainInstance(true);

        builder.setView(v)
                .setTitle("Delete Label?")
                // Add action buttons
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        model.deleteCurrentLabel();
                        DeleteLabelDialog.this.getDialog().cancel();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteLabelDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle b = getArguments();
        if ( b!= null) {
            List<Label.Plain> list = new ArrayList<>();
            list.add(Label.getPlain(b.getInt("COLOR", -1),b.getString("NAME", ""),""));
            label.setMode(ChipsLayout.MODE_NON_TOUCH);
            label.setData(list,null);
        }

        model = ViewModelProviders.of(getActivity(),viewModelFactory).get(LabelsViewModel.class);
    }
}
