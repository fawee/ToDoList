package com.android.project.todolist.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.android.project.todolist.domain.ListItem;
import com.android.project.todolist.tools.Tools;
import java.util.Calendar;

/**
 * This class represents the Activity where you can customize your Item.
 */

public class ListItemDetailActivity extends Activity implements Communicator, View.OnClickListener {

    private ListItem listItemToEdit;
    private EditText title, dueDate, note, reminderDay, reminderTime;
    private TextView reminderDateTV, reminderTimeTV, dateTextView;
    private Button addListItemButton;
    private String listColor;
    private Spinner prioritySpinner;
    private ToggleButton reminder;
    private Calendar currentTime, alarm;
    private Intent i;
    private int year, month, day, hour, minute;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readIntent();
        initUI();
    }

    private void readIntent() {
        Bundle extras = getIntent().getExtras();


        listItemToEdit = new ListItem(extras.getInt("listItemID"),
                extras.getString("listItemTitle"),
                extras.getString("listItemNote"),
                extras.getInt("listItemPriority"),
                extras.getLong("listItemDueDate"),
                extras.getBoolean("listItemIsDone"),
                extras.getBoolean("listItemReminder"),
                extras.getLong("listItemReminderDate"),
                extras.getInt("listID"));

        listColor = extras.getString("listColor");
    }

    private void initUI() {
        setContentView(R.layout.activity_add_listitem_menu);
        initViews();
        initPrioritySpinner();
        initReminder();
        setupOnClickListener();
    }

    private void initViews() {
        title = (EditText) findViewById(R.id.addListItemMenuTitle);
        title.setText(listItemToEdit.getTitle());

        dueDate = (EditText) findViewById(R.id.addListItemMenuDate);
        dueDate.setText(listItemToEdit.getFormatedDueDate());

        note = (EditText) findViewById(R.id.addListItemMenuNote);
        note.setText(listItemToEdit.getNote());

        prioritySpinner = (Spinner) findViewById(R.id.addListItemMenuPriority);

        addListItemButton = (Button) findViewById(R.id.addListItemButton);

        initReminderViews();



        if(listItemToEdit.getReminder()) {
            reminderDay.setText(listItemToEdit.getFormatedReminderDate());
            reminderTime.setText(listItemToEdit.getFormatedReminderTime());
        }

        reminder = (ToggleButton) findViewById(R.id.reminderButton);
        setViewStyle();

        if (listItemToEdit.getReminder()) {
            reminder.setChecked(true);
            showReminderOptions();
        }
    }

    private void setViewStyle() {

        TextView titleTV = (TextView) findViewById(R.id.addListItemMenuTitleTV);
        TextView dateTV = (TextView) findViewById(R.id.addListItemMenuDateTV);
        TextView noteTV = (TextView) findViewById(R.id.addListItemMenuNoteTV);
        TextView priorityTV = (TextView) findViewById(R.id.addListItemMenuPriorityTV);
        TextView remindMe = (TextView) findViewById(R.id.addListItemActivityReminderTV);

        titleTV.setTextColor(getResources().getColor(Tools.getColor(listColor)));
        dateTV.setTextColor(getResources().getColor(Tools.getColor(listColor)));
        noteTV.setTextColor(getResources().getColor(Tools.getColor(listColor)));
        priorityTV.setTextColor(getResources().getColor(Tools.getColor(listColor)));
        remindMe.setTextColor(getResources().getColor(Tools.getColor(listColor)));
        reminderDateTV.setTextColor(getResources().getColor(Tools.getColor(listColor)));
        reminderTimeTV.setTextColor(getResources().getColor(Tools.getColor(listColor)));

        //Farbe für die Edittext-Linie
        title.getBackground().setColorFilter(getResources().getColor(Tools.getColor(listColor)), PorterDuff.Mode.SRC_ATOP);
        dueDate.getBackground().setColorFilter(getResources().getColor(Tools.getColor(listColor)), PorterDuff.Mode.SRC_ATOP);
        note.getBackground().setColorFilter(getResources().getColor(Tools.getColor(listColor)), PorterDuff.Mode.SRC_ATOP);
        reminderDay.getBackground().setColorFilter(getResources().getColor(Tools.getColor(listColor)), PorterDuff.Mode.SRC_ATOP);
        reminderTime.getBackground().setColorFilter(getResources().getColor(Tools.getColor(listColor)), PorterDuff.Mode.SRC_ATOP);
        prioritySpinner.getBackground().setColorFilter(getResources().getColor(Tools.getColor(listColor)), PorterDuff.Mode.SRC_ATOP);

        setViewFont(titleTV, dateTV, noteTV, priorityTV, remindMe);

    }

    private void setViewFont(TextView titleTV, TextView dateTV, TextView noteTV, TextView priorityTV, TextView remindMe) {
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface editTextFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Italic.ttf");

        titleTV.setTypeface(font);
        dateTV.setTypeface(font);
        noteTV.setTypeface(font);
        priorityTV.setTypeface(font);
        remindMe.setTypeface(font);
        reminderDateTV.setTypeface(font);
        reminderTimeTV.setTypeface(font);

        title.setTypeface(editTextFont);
        dueDate.setTypeface(editTextFont);
        note.setTypeface(editTextFont);
        reminderDay.setTypeface(editTextFont);
        reminderTime.setTypeface(editTextFont);
    }


    private void initReminderViews() {
        reminderDateTV = (TextView) findViewById(R.id.addListItemActivityReminderDateTV);
        reminderTimeTV = (TextView) findViewById(R.id.addListItemActivityReminderTimeTV);
        reminderDay = (EditText) findViewById(R.id.addListItemActivityReminderDate);
        reminderTime = (EditText) findViewById(R.id.addListItemActivityReminderTime);
        hideReminderOptions();
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
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.addListItemMenu_Priority_Spinner, R.layout.spinner_add_listitem_activity);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setSelection(listItemToEdit.getPriority() - 1);

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

    private void setupOnClickListener() {
        dueDate.setOnClickListener(this);
        reminderDay.setOnClickListener(this);
        reminderTime.setOnClickListener(this);
        addListItemButton.setOnClickListener(this);
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

    @Override
    public void getInputData(String listTitle, String listColor, int listID) {
    }

    @Override
    public void getDate(DatePicker view, int year, int month, int day) {
        String dateString = "";
        if (day > 9) {
            dateString = String.valueOf(day);
        } else {
            dateString = "0" + day;
        }
        dateString += ".";
        int tmpMonth = month + 1;

        if (tmpMonth > 9) {
            dateString += String.valueOf(tmpMonth);
        } else {
            dateString += "0" + tmpMonth;
        }
        dateString = dateString + "." + year;
        dateTextView = (TextView) findViewById(R.id.addListItemMenuDate);
        TextView reminderDateTextView = (TextView) findViewById(R.id.addListItemActivityReminderDate);
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

    private void createListItem() {
        i = getIntent();
        String listItemReminderDay = reminderDay.getText().toString();
        String listItemReminderTime = reminderTime.getText().toString();
        boolean completeInputs = false;
        boolean reminderIsSet = isReminded(listItemReminderDay, listItemReminderTime);

        listItemToEdit.setTitle(title.getText().toString());
        listItemToEdit.setNote(note.getText().toString());
        listItemToEdit.setDueDate(dueDate.getText().toString());
        listItemToEdit.setPriority(Integer.parseInt(prioritySpinner.getSelectedItem().toString()));
        listItemToEdit.setReminder(isReminded(listItemReminderDay, listItemReminderTime));
        
        //LOGIK! Überprüft, ob die Eingaben vollständig sind und ob evtl. Alarm gesetzt bzw. falsch gesetzt wurde

        if((!listItemToEdit.getTitle().equals(""))) {

            if(!reminder.isChecked()) {
                completeInputs = true;
            }
            else if(listItemReminderDay.equals("") || listItemReminderTime.equals("")) {
                completeInputs = false;
                Toast.makeText(getApplicationContext(), "Can't set Reminder: Date or Time missing", Toast.LENGTH_SHORT).show();

            }
            else {
                setReminderDate();
                if(!isValidReminderDate()) {
                    completeInputs = false;
                    Toast.makeText(getApplicationContext(), "Can't set Reminder in the past", Toast.LENGTH_SHORT).show();
                } else {
                    listItemToEdit.setReminderDate(listItemReminderDay + " " + listItemReminderTime);
                    completeInputs = true;
                }
            }

        } else {
            title.requestFocus();
            Toast.makeText(getApplicationContext(), "Title is missing", Toast.LENGTH_SHORT).show();
        }

        if(completeInputs) {

            if(reminderIsSet) {
                setReminderAlarm();
            }
            sendDataToSubMenu();
        }
    }

    private boolean isValidReminderDate() {
        if(currentTime.getTimeInMillis() >= alarm.getTimeInMillis()) {
            return false;
        } else {
            return true;
        }
    }

    private void setReminderDate() {
        day = Integer.parseInt(reminderDay.getText().toString().substring(0, 2));
        month = Integer.parseInt(reminderDay.getText().toString().substring(3, 5));
        year = Integer.parseInt(reminderDay.getText().toString().substring(6, 10));
        hour = Integer.parseInt(reminderTime.getText().toString().substring(0, 2));
        minute = Integer.parseInt(reminderTime.getText().toString().substring(3, 5));

        alarm = Calendar.getInstance();
        currentTime = Calendar.getInstance();
        alarm.set(Calendar.YEAR, year);
        alarm.set(Calendar.MONTH, month-1);
        alarm.set(Calendar.DAY_OF_MONTH, day);
        alarm.set(Calendar.HOUR_OF_DAY, hour);
        alarm.set(Calendar.MINUTE, minute);
        alarm.set(Calendar.SECOND, 0);
    }

    private void setReminderAlarm() {

        i.putExtra("year", year);
        i.putExtra("month", month);
        i.putExtra("day", day);
        i.putExtra("hour", hour);
        i.putExtra("minute", minute);
    }

    private boolean isReminded(String listItemReminderDay, String listItemReminderTime) {

        if (reminder.isChecked()) {
            if ((!listItemReminderDay.equals("")) && (!listItemReminderTime.equals(""))) {
                return true;
            }
        }
        return false;
    }

    private void sendDataToSubMenu() {
        i.putExtra("ListItemID", listItemToEdit.getListItemID());
        i.putExtra("Title", listItemToEdit.getTitle());
        i.putExtra("DueDate", listItemToEdit.getDueDate());
        i.putExtra("Note", listItemToEdit.getNote());
        i.putExtra("Priority", listItemToEdit.getPriority());
        i.putExtra("IsDone", listItemToEdit.getIsDone());
        i.putExtra("Reminder", listItemToEdit.getReminder());
        i.putExtra("ReminderDate", listItemToEdit.getReminderDate());
        i.putExtra("ListID", listItemToEdit.getListID());
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
