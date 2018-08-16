package com.vividprojects.protoplanner.ui.dialogs;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vividprojects.protoplanner.adapters.ColorPickerAdapter;
import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.viewmodels.LabelsViewModel;
import com.vividprojects.protoplanner.R;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class CreateLabelDialog extends DialogFragment implements Injectable {
    private RecyclerView recycler;

    private TextView labelName;
    private ColorPickerAdapter colorPickerAdapter;

    private LabelsViewModel model;

    private String editId = "";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());



        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_label_dialog, null);

        recycler = v.findViewById(R.id.ald_color_picker);
        labelName = v.findViewById(R.id.ald_name);

        labelName.setSelected(false);

        setRetainInstance(true);

//        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                .setTitle("New Label")
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (!editId.equals(""))
                            model.editLabel(labelName.getText().toString(), "",colorPickerAdapter.getColor(),editId);
                        else
                            model.newLabel(labelName.getText().toString(), "",colorPickerAdapter.getColor());

                        CreateLabelDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CreateLabelDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),5);
        layoutManager.setAutoMeasureEnabled(true);
        recycler.setLayoutManager(layoutManager);
        //recycler.setNestedScrollingEnabled(false);
        recycler.setFocusable(false);

        colorPickerAdapter = new ColorPickerAdapter();
        recycler.setAdapter(colorPickerAdapter);

        Bundle b = getArguments();
        if ( b!= null) {
            labelName.setText(b.getString("NAME", ""));
            colorPickerAdapter.setSelectedColor(b.getInt("COLOR", -1));
            editId = b.getString("ID","");
        }

        model = ViewModelProviders.of(getActivity(),viewModelFactory).get(LabelsViewModel.class);
    }
}
