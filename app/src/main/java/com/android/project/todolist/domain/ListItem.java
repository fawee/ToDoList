package com.android.project.todolist.domain;


import android.text.format.DateFormat;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ListItem {

    private String title;
    private String note;
    private int priority;
    private GregorianCalendar calendar;
    private boolean reminder;
    private boolean isDone;
    private int listID;

    public ListItem(String title, String note, int year, int month, int day) {
        this.title = title;
        calendar = new GregorianCalendar(year, month, day);
        this.note = note;
    }


    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public int getPriority() {
        return priority;
    }

    public String getStringFromDate() {
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT,
                Locale.GERMANY);
        String dateString = df.format(calendar.getTime());

        return dateString;

    }

    public boolean getIsDone() {
        return isDone;
    }

    public boolean getReminder() {
        return reminder;
    }

    public int getListID() {
        return listID;
    }
}
