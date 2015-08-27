package com.android.project.todolist.menues;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import com.android.project.todolist.R;
import com.android.project.todolist.communicator.Communicator;
import com.android.project.todolist.dialogs.DatePickerFragment;
import com.android.project.todolist.domain.ListItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class AddListItemMenu extends Activity implements Communicator {

    private EditText title, date, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listitem_menu);
        setupGUI();
        setupDatePicker();
        setupAddListItemClickListener();


    }

    private void setupAddListItemClickListener() {
        Button addButton = (Button) findViewById(R.id.button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListItem();

            }
        });
    }

    private void createListItem() {
        String listItemTitle = title.getText().toString();
        String listItemDate = date.getText().toString();
        String listItemNote = note.getText().toString();

        sendDataToSubMenu(listItemTitle, listItemDate, listItemNote);


    }

    private void sendDataToSubMenu(String listItemTitle, String listItemDate, String listItemNote) {
        Intent i = getIntent();
        i.putExtra("Title", listItemTitle);
        i.putExtra("Date", listItemDate);
        i.putExtra("Note", listItemNote);
        setResult(RESULT_OK, i);
        finish();
    }


    private void setupDatePicker() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    private void showDatePickerDialog(View v) {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getFragmentManager(), "datePicker");
    }

    private void setupGUI() {
        title = (EditText) findViewById(R.id.addListItemMenuTitle);
        date = (EditText) findViewById(R.id.addListItemMenuDate);
        note = (EditText) findViewById(R.id.addListItemMenuNote);
    }


    @Override
    public void getInputTextFromDialog(String inputText) {

    }

    @Override
    public void getDate(DatePicker view, int year, int month, int day) {
        TextView textView = (TextView) findViewById(R.id.addListItemMenuDate);

        GregorianCalendar date = new GregorianCalendar(year, month, day);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        String dateString = df.format(date.getTime());

        textView.setText(dateString);
    }


}
