package com.android.project.todolist.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.project.todolist.R;
import com.android.project.todolist.adapter.SpinnerAdapter;
import com.android.project.todolist.communicator.Communicator;
import com.android.project.todolist.domain.SpinnerItem;
import com.android.project.todolist.log.Log;
import com.android.project.todolist.tools.Tools;

import java.util.ArrayList;


public class DialogAddListObject extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private Communicator communicator;
    private ArrayList<SpinnerItem> spinnerItems;
    private AlertDialog.Builder builder;
    private View view;
    private Spinner spinner;
    private String pickedColor;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (Communicator) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(String.valueOf(getArguments().getInt("listId")));
        int listID = getArguments().getInt("listId");
        String listTitle = getArguments().getString("listTitle");
        String listColour = getArguments().getString("listColour");
        setupDialog(listTitle, listColour);
        setupSpinner(listColour);
        if(listTitle.equals("")){   handleClicks("Add", listID);}
        else{                       handleClicks("Save", listID);}

        return builder.create();
    }

    private void setupDialog(String listTitle, String listColour) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_add_list, null);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");

        EditText editTListTitle = (EditText) view.findViewById(R.id.dialog_addList_title);
        editTListTitle.setText(listTitle);

        TextView dialogHead = (TextView) view.findViewById(R.id.dialog_head);
        dialogHead.setTypeface(font);
        Tools.setColor(listColour, dialogHead);

        builder.setView(view);
        builder.setCancelable(false);

    }

    private void setupSpinner(String preselectedColour) {
        spinner = (Spinner) view.findViewById(R.id.dialog_addList_spinner);
        spinnerItems = new ArrayList<>();
        addColorsToSpinner();
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getActivity(), spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(getPreselectedSpinnerPosition(preselectedColour));
        spinner.setOnItemSelectedListener(this);
    }

    private int getPreselectedSpinnerPosition(String preselectedColour){
        for(int position = 0; position < spinnerItems.size();position++ ){
            if (spinnerItems.get(position).getColorValue().equals(preselectedColour)){
                return position;
            }
        }
        return 0;
    }
    private void addColorsToSpinner() {
        //ToDo: nachstehende Construktoraufrufe werden komischerweisße nicht akzeptiert, dass in der mitte ist ja ein STring kein int
/*        spinnerItems.add(new SpinnerItem("Blue", R.string.spinnerBlue, R.color.blue));
        spinnerItems.add(new SpinnerItem("Yellow", R.string.spinnerYellow, R.color.yellow));
        spinnerItems.add(new SpinnerItem("Green", R.string.spinnerGreen, R.color.green));
        spinnerItems.add(new SpinnerItem("Red", R.string.spinnerRed, R.color.red));
        spinnerItems.add(new SpinnerItem("Lime", R.string.spinnerLime, R.color.lime));*/
        spinnerItems.add(new SpinnerItem("Blue", "blue", R.color.blue));
        spinnerItems.add(new SpinnerItem("Yellow", "yellow", R.color.yellow));
        spinnerItems.add(new SpinnerItem("Green", "green", R.color.green));
        spinnerItems.add(new SpinnerItem("Red", "red", R.color.red));
        spinnerItems.add(new SpinnerItem("Lime", "lime", R.color.lime));
        spinnerItems.add(new SpinnerItem("Teal", "teal", R.color.teal));
        spinnerItems.add(new SpinnerItem("Indigo", "indigo", R.color.indigo));
    }

    private void handleClicks(String positivTitle, final int listID) {
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(positivTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText edittextInput = (EditText) view.findViewById(R.id.dialog_addList_title);

                communicator.getInputData(edittextInput.getText().toString(), pickedColor, listID);

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SpinnerItem spinnerItem = (SpinnerItem) parent.getItemAtPosition(position);
        pickedColor = spinnerItem.getColorValue();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
