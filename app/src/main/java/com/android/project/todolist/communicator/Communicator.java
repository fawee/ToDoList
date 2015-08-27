package com.android.project.todolist.communicator;


import android.widget.DatePicker;

import com.android.project.todolist.domain.ListObject;

public interface Communicator {

    void getInputTextFromDialog(String inputText);
    void getDate(DatePicker view, int year, int month, int day);


}
