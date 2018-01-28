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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.vividprojects.protoplanner.Adapters.LabelsDialogListAdapter;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.RunnableParam;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Smile on 23.01.2018.
 */

public class SelectTagsDialog extends DialogFragment implements Injectable {
    private List<Label.Plain> labels;
    private RecyclerView recycler;

    private RecordItemViewModel model;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());



        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.select_labels_dialog, null);

        recycler = v.findViewById(R.id.sld_recycler);

        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                .setTitle("Image URL")
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        String[] selectedLabels = new String[labels.size()];
                        for (Label.Plain label : labels) {
                            //selectedLabels[]
                        }
                        intent.putExtra("LABELS", selectedLabels);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        SelectTagsDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SelectTagsDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        recycler.setLayoutManager(layoutManager);
        //recycler.setNestedScrollingEnabled(false);
        recycler.setFocusable(false);

        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

        model.getLabels().observe(getActivity(),(labels)->{
            recycler.setAdapter(new LabelsDialogListAdapter(labels,getActivity()));
        });
    }
}
