package com.vividprojects.protoplanner.Interface;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vividprojects.protoplanner.Adapters.ColorPickerAdapter;
import com.vividprojects.protoplanner.Adapters.LabelsDialogListAdapter;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class CreateLabelDialog extends DialogFragment implements Injectable {
    private List<Label.Plain> labels;
    private RecyclerView recycler;

    private RecordItemViewModel model;
    private TextView labelName;
    private ColorPickerAdapter colorPickerAdapter;

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
/*
                        Intent intent = new Intent();
                        String[] selectedLabels = new String[labels.size()];
                        for (Label.Plain label : labels) {
                            //selectedLabels[]
                        }
                        intent.putExtra("LABELS", selectedLabels);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

*/
                        ((GetLabelInterface) getActivity()).returnLabel(labelName.getText().toString(),colorPickerAdapter.getColor());

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

        colorPickerAdapter = new ColorPickerAdapter(null);

        recycler.setAdapter(colorPickerAdapter);

/*        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

        model.getLabels().observe(getActivity(),(labels)->{
            recycler.setAdapter(new LabelsDialogListAdapter(labels,getActivity()));
        });*/
    }
}
