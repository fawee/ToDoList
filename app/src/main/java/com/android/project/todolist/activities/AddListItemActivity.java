package com.android.project.todolist.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.android.project.todolist.R;
import com.android.project.todolist.communicator.Communicator;
import com.android.project.todolist.dialogs.DatePickerFragment;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;


public class AddListItemActivity extends Activity implements Communicator {

    private EditText title, date, note, reminderDate, reminderTime;
    private TextView reminderDateTV, reminderTimeTV;
    private Spinner prioritySpinner;
    private ToggleButton reminder;

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
        String listItemPriority = prioritySpinner.getSelectedItem().toString();

        //ÜBERPRÜFT OB USER DATUM GEWÄHLT HAT ODER NICHT
        if(!listItemDate.equals("")) {
            sendDataToSubMenu(listItemTitle, listItemDate, listItemNote, listItemPriority);
        }

    }

    private void sendDataToSubMenu(String listItemTitle, String listItemDate, String listItemNote, String listItemPriority) {
        Intent i = getIntent();
        i.putExtra("Title", listItemTitle);
        i.putExtra("Date", listItemDate);
        i.putExtra("Note", listItemNote);
        i.putExtra("Priority", listItemPriority);
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
        initViews();
        initPrioritySpinner();
        initReminder();

    }

    private void initReminder() {
        reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    showReminderOptions();
                } else {
                    hideReminderOptions();
                }
            }
        });
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
        TextView textView = (TextView) findViewById(R.id.addListItemMenuDate);

        GregorianCalendar date = new GregorianCalendar(year, month, day);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        String dateString = df.format(date.getTime());

        textView.setText(dateString);
    }



}
