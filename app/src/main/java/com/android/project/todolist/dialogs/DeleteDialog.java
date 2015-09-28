package com.android.project.todolist.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.project.todolist.R;
import com.android.project.todolist.communicator.DeleteNotifier;

/**
 * This class represents a Dialog, which asks, if you really want to delete the Items.
 */

public class DeleteDialog extends DialogFragment {

    private DeleteNotifier deleteNotifier;
    private AlertDialog.Builder builder;
    private String message;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        deleteNotifier = (DeleteNotifier) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        builder.setMessage(message)
                .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteNotifier.onDeleted();
                    }
                })
                .setNegativeButton(R.string.action_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();

    }

    public void setMessage(String message) {
        this.message = message;
    }

}
