package com.android.project.todolist.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.android.project.todolist.R;
import com.android.project.todolist.communicator.Communicator;
import com.android.project.todolist.dialogs.DatePickerFragment;
import com.android.project.todolist.dialogs.TimePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;


public class AddListItemActivity extends Activity implements Communicator, View.OnClickListener {

    private EditText title, date, note, reminderDate, reminderTime;
    private TextView reminderDateTV, reminderTimeTV;
    private Button addListItemButton;
    private Spinner prioritySpinner;
    private ToggleButton reminder;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listitem_menu);
        setupGUI();
        setupOnClickListener();


    }

    private void setupOnClickListener() {
        date.setOnClickListener(this);
        reminderDate.setOnClickListener(this);
        reminderTime.setOnClickListener(this);
        addListItemButton.setOnClickListener(this);
    }



    private void setupGUI() {
        initViews();
        initPrioritySpinner();
        initReminder();

    }

    private void initReminder() {
        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showReminderOptions();
                } else {
                    hideReminderOptions();
                    clearInputs();
                }
            }
        });
    }

    private void clearInputs() {
        reminderTime.setText("");
        reminderDate.setText("");
    }

    private void hideReminderOptions() {
        reminderDateTV.setVisibility(View.INVISIBLE);
        reminderDate.setVisibility(View.INVISIBLE);
        reminderTimeTV.setVisibility(View.INVISIBLE);
        reminderTime.setVisibility(View.INVISIBLE);
    }

    private void showReminderOptions() {
        reminderDateTV.setVisibility(View.VISIBLE);
        reminderDate.setVisibility(View.VISIBLE);
        reminderTimeTV.setVisibility(View.VISIBLE);
        reminderTime.setVisibility(View.VISIBLE);
    }


    private void initPrioritySpinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.addListItemMenu_Priority_Spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
    }

    private void initViews() {
        title = (EditText) findViewById(R.id.addListItemMenuTitle);
        date = (EditText) findViewById(R.id.addListItemMenuDate);
        note = (EditText) findViewById(R.id.addListItemMenuNote);
        prioritySpinner = (Spinner) findViewById(R.id.addListItemMenuPriority);
        addListItemButton = (Button) findViewById(R.id.addListItemButton);
        reminder = (ToggleButton) findViewById(R.id.reminderButton);
        initReminderViews();
    }

    private void initReminderViews() {
        reminderDateTV = (TextView) findViewById(R.id.addListItemActivityReminderDateTV);
        reminderTimeTV = (TextView) findViewById(R.id.addListItemActivityReminderTimeTV);

        reminderDate = (EditText) findViewById(R.id.addListItemActivityReminderDate);
        reminderTime = (EditText) findViewById(R.id.addListItemActivityReminderTime);
        hideReminderOptions();
    }


    @Override
    public void getInputData(String listTitle, int listColor) {

    }

    @Override
    public void getDate(DatePicker view, int year, int month, int day) {

        TextView dateTextView = (TextView) findViewById(R.id.addListItemMenuDate);
        TextView reminderDateTextView = (TextView) findViewById(R.id.addListItemActivityReminderDate);

        GregorianCalendar dueDate = new GregorianCalendar(year, month, day);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        String dateString = df.format(dueDate.getTime());
        if(flag == 0) {
            dateTextView.setText(dateString);
        }
        if(flag == 1) {
            reminderDateTextView.setText(dateString);
        }



    }

    @Override
    public void getTime(TimePicker view, int hourOfDay, int minute) {
        TextView reminderTimeTextView = (TextView) findViewById(R.id.addListItemActivityReminderTime);
        String formattedTime = formatTime(hourOfDay, minute);
        reminderTimeTextView.setText(formattedTime);



        
    }

    private String formatTime(int hourOfDay, int minute) {
        String hour, min;
        if(hourOfDay < 10) {
            hour = "0" + String.valueOf(hourOfDay);
        } else {
            hour = String.valueOf(hourOfDay);
        }

        if(minute < 10) {
            min = "0" + String.valueOf(minute);
        } else {
            min = String.valueOf(minute);
        }
        String formattedTime = hour + ":" + min;
        return formattedTime;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.addListItemMenuDate:
                flag = 0;
                showDatePickerDialog();
                break;

            case R.id.addListItemActivityReminderDate:
                flag = 1;
                showDatePickerDialog();
                break;
            
            case R.id.addListItemActivityReminderTime:
                showTimePickerDialog();
                break;

            case R.id.addListItemButton:
                createListItem();
                break;
            
            
        }
    }

    private void createListItem() {
        String listItemTitle = title.getText().toString();
        String listItemDate = date.getText().toString();
        String listItemNote = note.getText().toString();
        String listItemPriority = prioritySpinner.getSelectedItem().toString();
        boolean listItemReminder = isReminded();

        //ÜBERPRÜFT OB USER DATUM GEWÄHLT HAT ODER NICHT
        if (!listItemDate.equals("")) {
            sendDataToSubMenu(listItemTitle, listItemDate, listItemNote, listItemPriority, listItemReminder);
        }

    }

    private boolean isReminded() {
        String rDate = reminderDate.getText().toString();
        String rTime = reminderTime.getText().toString();
        if(reminder.isChecked()) {
            if((!rDate.equals("")) && (!rTime.equals(""))) {
                return true;
            }
        }
        return false;
    }


    private void sendDataToSubMenu(String listItemTitle, String listItemDate, String listItemNote, String listItemPriority, boolean listItemReminder) {
        Intent i = getIntent();
        i.putExtra("Title", listItemTitle);
        i.putExtra("Date", listItemDate);
        i.putExtra("Note", listItemNote);
        i.putExtra("Priority", listItemPriority);
        i.putExtra("Reminder", listItemReminder);
        setResult(RESULT_OK, i);
        finish();
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");

    }

    private void showDatePickerDialog() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getFragmentManager(), "datePicker");
    }
}
