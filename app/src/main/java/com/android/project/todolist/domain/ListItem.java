package com.android.project.todolist.domain;


import java.util.Date;

public class ListItem {

    private String title;
    private String note;
    private int priority;
    private Date dueDate;
    private boolean reminder;
    private boolean isDone;
    private int listID;

    public ListItem(String title, String note, Date dueDate) {

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

    public Date getDueDate() {
        return dueDate;
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