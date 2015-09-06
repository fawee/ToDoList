package com.android.project.todolist.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.project.todolist.R;
import com.android.project.todolist.adapter.SpinnerAdapter;
import com.android.project.todolist.communicator.Communicator;
import com.android.project.todolist.domain.SpinnerItem;

import java.util.ArrayList;


public class DialogAddListObject extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private Communicator communicator;
    private ArrayList<SpinnerItem> list;
    private AlertDialog.Builder builder;
    private View view;
    private Spinner spinner;
    private int pickedColor;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setupDialog();
        setupSpinner();
        handleClicks();

        return builder.create();
    }

    private void setupSpinner() {
        spinner = (Spinner) view.findViewById(R.id.dialog_addList_spinner);
        list = new ArrayList<>();
        addColorsToSpinner();
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), list);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void addColorsToSpinner() {
        list.add(new SpinnerItem("Blue", R.color.blue));
        list.add(new SpinnerItem("Yellow", R.color.yellow));
        list.add(new SpinnerItem("Green", R.color.green));
        list.add(new SpinnerItem("Red", R.color.red));
        list.add(new SpinnerItem("Lime", R.color.lime));
    }

    private void setupDialog() {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_add_list, null);
        builder.setView(view);
        builder.setCancelable(false);

    }

    private void handleClicks() {
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edittextInput = (EditText) view.findViewById(R.id.dialog_addList_title);

                communicator.getInputData(edittextInput.getText().toString(), pickedColor);

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SpinnerItem spinnerItem = (SpinnerItem) parent.getItemAtPosition(position);
        pickedColor = spinnerItem.getColor();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
