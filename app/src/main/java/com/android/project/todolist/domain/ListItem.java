package com.android.project.todolist.domain;


import android.text.format.DateFormat;

import java.util.Calendar;
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
    //TODO: ist GregorianCalendar richtig gewählt?
    private Calendar reminderDate;
    //private String reminderDate;
    private int listID;

    public ListItem(int listItemID,
                    String title,
                    String note,
                    int priority,
                    int year, int month, int day,
                    boolean isDone,
                    boolean reminder,
                    Calendar reminderDate,
                    int listID) {
    //public ListItem(int listItemID, String title, String note, int priority, int year, int month, int day, boolean isDone, boolean reminder, String reminderDate, int listID) {

        this.listItemID = listItemID;
        this.title = title;
        this.note = note;
        this.priority = priority;
        dueDate = new GregorianCalendar(year, month, day);
        this.isDone = isDone;
        this.reminder = reminder;
        this.reminderDate = reminderDate;
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
        return df.format(dueDate.getTime());
    }

    public boolean getIsDone() { return isDone;}

    public boolean getReminder() {return reminder;}

    public Calendar getReminderDate() {
    //public String getReminderDate() {
        return reminderDate;
    }

    public String getStringFromReminderDate() {
        //TODO:"long" sorgt für "September" stat "09"
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT,
                Locale.GERMANY);
        return df.format(reminderDate.getTime());
    }

    public int getListID() { return listID; }

    public void setListItemID(int listItemID) {
        this.listItemID = listItemID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDueDate(GregorianCalendar dueDate) {
        this.dueDate = dueDate;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public void setReminderDate(GregorianCalendar reminderDate) {
        this.reminderDate = reminderDate;
    }

}
