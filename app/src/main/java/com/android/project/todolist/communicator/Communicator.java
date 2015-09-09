package com.android.project.todolist.communicator;


import android.widget.DatePicker;
import android.widget.TimePicker;

import com.android.project.todolist.domain.ListObject;

public interface Communicator {

    void getInputData(String listTitle, String listColor, int listId);
    void getDate(DatePicker view, int year, int month, int day);
    void getTime(TimePicker view, int hourOfDay, int minute);


}
