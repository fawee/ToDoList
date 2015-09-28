package com.android.project.todolist.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.project.todolist.R;
import com.android.project.todolist.communicator.CancelNotifier;


public class CancelDialog extends DialogFragment {

    private CancelNotifier cancelNotifier;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        cancelNotifier = (CancelNotifier) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
        builder.setMessage(R.string.dialog_cancelMessage)
                .setPositiveButton(R.string.dialog_cancelYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelNotifier.onCancelPressed();
                    }
                })
                .setNegativeButton(R.string.dialog_cancelNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();

    }

}
