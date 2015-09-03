package com.android.project.todolist.communicator;


import android.widget.DatePicker;

import com.android.project.todolist.domain.ListObject;

public interface Communicator {

    void getInputData(String listTitle, int listColor);
    void getDate(DatePicker view, int year, int month, int day);


}
