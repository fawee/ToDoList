package com.android.project.todolist.communicator;


import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * This interface is like a Communicator between the Dialogs and the Activities.
 * It takes the data from the dialog and provides the data for the Activity.
 */


public interface Communicator {

    void getInputData(String listTitle, String listColor, int listId);
    void getDate(DatePicker view, int year, int month, int day);
    void getTime(TimePicker view, int hourOfDay, int minute);


}
