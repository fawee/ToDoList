package com.android.project.todolist.domain;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents an Item in a List.
 */

public class ListItem implements Comparable<ListItem> {

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private int listItemID;
    private String title;
    private String note;
    private int priority;
    private long dueDate;
    private boolean isDone;
    private boolean reminder;
    private long reminderDate;
    private int listID;

    public ListItem(int listItemID,
                    String title,
                    String note,
                    int priority,
                    long dueDate,
                    boolean isDone,
                    boolean reminder,
                    long reminderDate,
                    int listID) {
        this.listItemID = listItemID;
        this.title = title;
        this.note = note;
        this.priority = priority;
        this.dueDate = dueDate;
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

    public long getDueDate() { return dueDate; }

    public String getFormatedDueDate() {
        if (this.dueDate == 0) {
            return "";
        }
        else{
            try {
                DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                Date netDate = (new Date(dueDate));
                return sdf.format(netDate);
            } catch (Exception ex) {
                return "";
            }
        }
    }

    public boolean getIsDone() { return isDone;}

    public boolean getReminder() {return reminder;}

    public long getReminderDate() { return reminderDate; }

    public String getFormatedReminderDate(){
        if (this.reminderDate == 0){
            return "";
        }
        else {
            try {
                DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                Date netDate = (new Date(reminderDate));
                return sdf.format(netDate);
            } catch (Exception ex) {
                return "";
            }
        }
    }

    public String getFormatedReminderTime(){
        if (this.reminderDate == 0){
            return "";
        }
        else {
            try {
                DateFormat sdf = new SimpleDateFormat("HH:mm");
                Date netDate = (new Date(reminderDate));
                return sdf.format(netDate);
            } catch (Exception ex) {
                return "";
            }
        }
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

    public void setDueDate(long dueDate) { this.dueDate = dueDate; }

    public void setDueDate(String Date){
        if (Date.equals("")){
            this.dueDate=0;
        }
        else {
            //DateFormat formatter  = new SimpleDateFormat(DATE_FORMAT);
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date date = null;
            try {
                date = (Date) formatter.parse(Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.dueDate = date.getTime();
        }
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public void setReminderDate(long reminderDate) {
        this.reminderDate = reminderDate;
    }

    public void setReminderDate(String Date){
        if (Date.equals("")){
            this.reminderDate=0;
        }
        else{
            //DateFormat formatter  = new SimpleDateFormat(DATE_FORMAT);
            DateFormat formatter  = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date date = null;
            try {
                date = (Date)formatter.parse(Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.reminderDate = date.getTime();
        }
    }

    @Override
    public int compareTo(ListItem listItem) {
        //return (int) (this.dueDate.getTimeInMillis() - listItem.dueDate.getTimeInMillis());
        return (int) (this.dueDate - listItem.dueDate);
    }
}
