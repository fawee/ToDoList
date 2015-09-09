package com.android.project.todolist.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;


import com.android.project.todolist.R;
import com.android.project.todolist.communicator.Communicator;
import com.android.project.todolist.dialogs.DatePickerFragment;
import com.android.project.todolist.dialogs.TimePickerFragment;

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;


public class AddListItemActivity extends Activity implements Communicator, View.OnClickListener {

    private EditText title, dueDate, note, reminderDay, reminderTime;
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
        dueDate.setOnClickListener(this);
        reminderDay.setOnClickListener(this);
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
        reminderDay.setText("");
    }

    private void hideReminderOptions() {
        reminderDateTV.setVisibility(View.INVISIBLE);
        reminderDay.setVisibility(View.INVISIBLE);
        reminderTimeTV.setVisibility(View.INVISIBLE);
        reminderTime.setVisibility(View.INVISIBLE);
    }

    private void showReminderOptions() {
        reminderDateTV.setVisibility(View.VISIBLE);
        reminderDay.setVisibility(View.VISIBLE);
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
        dueDate = (EditText) findViewById(R.id.addListItemMenuDate);
        note = (EditText) findViewById(R.id.addListItemMenuNote);
        prioritySpinner = (Spinner) findViewById(R.id.addListItemMenuPriority);
        addListItemButton = (Button) findViewById(R.id.addListItemButton);
        reminder = (ToggleButton) findViewById(R.id.reminderButton);
        initReminderViews();
    }

    private void initReminderViews() {
        reminderDateTV = (TextView) findViewById(R.id.addListItemActivityReminderDateTV);
        reminderTimeTV = (TextView) findViewById(R.id.addListItemActivityReminderTimeTV);
        reminderDay = (EditText) findViewById(R.id.addListItemActivityReminderDate);
        reminderTime = (EditText) findViewById(R.id.addListItemActivityReminderTime);
        hideReminderOptions();
    }


    @Override
    public void getInputData(String listTitle, String listColor) {

    }

    @Override
    public void getDate(DatePicker view, int year, int month, int day) {

        TextView dateTextView = (TextView) findViewById(R.id.addListItemMenuDate);
        TextView reminderDateTextView = (TextView) findViewById(R.id.addListItemActivityReminderDate);

        GregorianCalendar dueDate = new GregorianCalendar(year, month, day);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        String dateString = df.format(dueDate.getTime());
        if (flag == 0) {
            dateTextView.setText(dateString);
        }
        if (flag == 1) {
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
        if (hourOfDay < 10) {
            hour = "0" + String.valueOf(hourOfDay);
        } else {
            hour = String.valueOf(hourOfDay);
        }

        if (minute < 10) {
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
        String listItemDueDate = dueDate.getText().toString();
        String listItemNote = note.getText().toString();
        String listItemPriority = prioritySpinner.getSelectedItem().toString();
        boolean listItemReminder = isReminded();
        String listItemReminderDate = "";
        //plausibilitytest of the Reminder input
        if (listItemReminder){
            if (reminderDay.equals("") ^ reminderTime.equals("")) {
                //TODO: Benutzerbenachrichtigung, dass er beide Werte eingeben muss
            }
            else if (!reminderDay.equals("")){
                String listItemReminderDay = reminderDay.getText().toString();
                String listItemReminderTime = reminderTime.getText().toString();
                listItemReminderDate = listItemReminderDay + " " + listItemReminderTime;
            }
        }
        //ÜBERPRÜFT OB USER DATUM GEWÄHLT HAT ODER NICHT
        if (!listItemDueDate.equals("")) {
            if(listItemReminder) {
                setAlarm();
            }
        }
        sendDataToSubMenu(listItemTitle, listItemDueDate, listItemNote, listItemPriority, listItemReminder, listItemReminderDate);
    }

    private void setAlarm() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_list_item_alarm)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        int mNotificationId = 001;

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private boolean isReminded() {
        String rDate = reminderDay.getText().toString();
        String rTime = reminderTime.getText().toString();
        if (reminder.isChecked()) {
            if ((!rDate.equals("")) && (!rTime.equals(""))) {
                return true;
            }
        }
        return false;
    }


    private void sendDataToSubMenu(String listItemTitle, String listItemDueDate, String listItemNote, String listItemPriority, boolean listItemReminder,  String listItemReminderDate) {
        Intent i = getIntent();
        i.putExtra("Title", listItemTitle);
        i.putExtra("DueDate", listItemDueDate);
        i.putExtra("Note", listItemNote);
        i.putExtra("Priority", listItemPriority);
        i.putExtra("Reminder", listItemReminder);
        i.putExtra("ReminderDate", listItemReminderDate);
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
