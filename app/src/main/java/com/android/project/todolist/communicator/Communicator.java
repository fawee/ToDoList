package com.android.project.todolist.communicator;


import android.widget.DatePicker;

public interface Communicator {

    void getInputTextFromDialog(String inputText);
    void getDate(DatePicker view, int year, int month, int day);

}
