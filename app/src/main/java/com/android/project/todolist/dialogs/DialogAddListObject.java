package com.android.project.todolist.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import com.android.project.todolist.R;
import com.android.project.todolist.communicator.Communicator;


public class DialogAddListObject extends DialogFragment {

    private Communicator communicator;
    private EditText edittextInput;
    private AlertDialog.Builder builder;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setupDialog();
        handleClicks();

        return builder.create();
    }

    private void setupDialog() {
        builder = new AlertDialog.Builder(getActivity());
        edittextInput = new EditText(getActivity());
        builder.setTitle(getString(R.string.dialog_title_add_listobject));
        builder.setCancelable(false);
        builder.setView(edittextInput);
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
                communicator.getInputTextFromDialog(edittextInput.getText().toString());
            }
        });
    }

}
