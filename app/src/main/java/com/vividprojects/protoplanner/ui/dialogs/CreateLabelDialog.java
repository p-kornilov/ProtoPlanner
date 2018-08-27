package com.vividprojects.protoplanner.ui.dialogs;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private View recycler_tint;

    private TextView labelName;
    private ColorPickerAdapter colorPickerAdapter;
    private CheckBox groupColorCheck;

    private LabelsViewModel model;

    private String editId = "";
    private boolean forGroup = false;
    private int groupColor = -1;
    private int labelColor = -1;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_label_dialog, null);

        recycler = v.findViewById(R.id.ald_color_picker);
        recycler_tint = v.findViewById(R.id.ald_recycler_tint);
        labelName = v.findViewById(R.id.ald_name);
        TextInputLayout textInputLayout = v.findViewById(R.id.ald_text_layout);
        groupColorCheck = v.findViewById(R.id.ald_checkBox);
        groupColorCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recycler_tint.setBackgroundColor(0xaaffffff);
                    colorPickerAdapter.setSelectedColor(groupColor);
                    colorPickerAdapter.setClickable(false);
                } else {
                    recycler_tint.setBackgroundColor(0x00ffffff);
                    colorPickerAdapter.setClickable(true);
                }
            }
        });

        labelName.setSelected(false);

        setRetainInstance(true);

        Bundle b = getArguments();
        if (b != null) {
            forGroup = b.getBoolean("FORGROUP", false);
            groupColor = b.getInt("GROUPCOLOR", -1);
            labelName.setText(b.getString("NAME", ""));
            labelColor = b.getInt("COLOR", -1);
            editId = b.getString("ID", "");
        }

/*        forGroup = false;
        editId = "dfg";
        labelColor = -10011977;
        groupColor = labelColor;// -16728876;*/

        String dialogName;
        String saveName;
        if (forGroup) {
            textInputLayout.setHint("Group name");
            if (editId.equals(""))
                dialogName = "New Group";
            else
                dialogName = "Edit Group";
        } else {
            textInputLayout.setHint("Label name");
            if (editId.equals(""))
                dialogName = "New Label";
            else
                dialogName = "Edit Label";
        }
        if (editId.equals(""))
            saveName = "Create";
        else
            saveName = "Save";



//        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                .setTitle(dialogName)
                // Add action buttons
                .setPositiveButton(saveName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (labelName.getText().length() == 0) {
                    Toast.makeText(CreateLabelDialog.this.getContext(), "Please set Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (colorPickerAdapter.getColor() == -1) {
                    Toast.makeText(CreateLabelDialog.this.getContext(), "Please select color", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!editId.equals("")) {
                    if (forGroup)
                        model.editGroup(labelName.getText().toString(), colorPickerAdapter.getColor(), editId);
                    else
                        model.editLabel(labelName.getText().toString(), "", colorPickerAdapter.getColor(), editId);
                } else {
                    if (forGroup)
                        model.newGroup(labelName.getText().toString(), colorPickerAdapter.getColor());
                    else
                        model.newLabel(labelName.getText().toString(), "", colorPickerAdapter.getColor());
                }
                CreateLabelDialog.this.getDialog().cancel();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
       // layoutManager.setAutoMeasureEnabled(true);
        recycler.setLayoutManager(layoutManager);
        //recycler.setNestedScrollingEnabled(false);
        recycler.setFocusable(false);

        colorPickerAdapter = new ColorPickerAdapter();
        recycler.setAdapter(colorPickerAdapter);

        model = ViewModelProviders.of(getActivity(), viewModelFactory).get(LabelsViewModel.class);

        if (forGroup) {
            groupColorCheck.setVisibility(View.GONE);
            colorPickerAdapter.setSelectedColor(groupColor);
            recycler_tint.setBackgroundColor(0x00ffffff);
        } else {
            groupColorCheck.setVisibility(View.VISIBLE);
            if (editId.equals("") || groupColor == labelColor) {
                groupColorCheck.setChecked(true);
                recycler_tint.setBackgroundColor(0xaaffffff);
                colorPickerAdapter.setSelectedColor(groupColor);
                colorPickerAdapter.setClickable(false);
            } else {
                groupColorCheck.setChecked(false);
                recycler_tint.setBackgroundColor(0x00ffffff);
                colorPickerAdapter.setSelectedColor(labelColor);
            }
        }
    }

}