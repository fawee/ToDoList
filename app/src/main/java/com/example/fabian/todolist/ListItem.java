package com.example.fabian.todolist;


import java.util.Date;

public class ListItem {

    private String title;
    private String note;
    private int priority;
    private Date dueDate;
    private boolean reminder;
    private boolean isDone;

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

    public byte[] getReminder() {
        return reminder;
    }

    public byte[] getListID() {
        return listID;
    }
}
