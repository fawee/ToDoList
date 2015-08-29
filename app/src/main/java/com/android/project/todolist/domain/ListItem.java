package com.android.project.todolist.domain;


import android.text.format.DateFormat;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ListItem {

    private int listItemID;
    private String title;
    private String note;
    private int priority;
    private GregorianCalendar dueDate;
    private boolean isDone;
    private boolean reminder;
    private int listID;

    public ListItem(int listItemID, String title, String note, int priority, int year, int month, int day, boolean isDone, boolean reminder, int listID) {

        this.listItemID = listItemID;
        this.title = title;
        this.note = note;
        this.priority = priority;
        dueDate = new GregorianCalendar(year, month, day);
        this.isDone = isDone;
        this.reminder = reminder;
        this.listID = listID;
    }

    public int getListItemID() {
        return listItemID;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public int getPriority() { return priority; }

    public String getStringFromDueDate() {
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT,
                Locale.GERMANY);
        String dateString = df.format(dueDate.getTime());

        return dateString;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public boolean getReminder() {
        return reminder;
    }

    public int getListID() { return listID; }

}
