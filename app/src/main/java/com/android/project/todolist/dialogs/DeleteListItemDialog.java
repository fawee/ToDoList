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

public class DeleteListItemDialog extends DialogFragment {

    private DeleteNotifier deleteNotifier;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        deleteNotifier = (DeleteNotifier) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        builder.setMessage(R.string.message)
                .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteNotifier.onListItemsDeleted();
                    }
                })
                .setNegativeButton(R.string.action_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();

    }

}
