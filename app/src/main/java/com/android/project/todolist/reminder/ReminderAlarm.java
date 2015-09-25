package com.android.project.todolist.reminder;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ReminderAlarm {

    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private Intent intent;
    private Calendar time;

    public ReminderAlarm(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.intent = new Intent(context, ReminderService.class);
        this.time = Calendar.getInstance();

    }

    public void setReminderTime(int year, int month, int day, int hour, int minute) {
        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month-1);
        time.set(Calendar.DAY_OF_MONTH, day);
        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, minute);
        time.set(Calendar.SECOND, 0);
    }

    public void setPendingIntent(int listItemID) {
        this.alarmIntent = PendingIntent.getService(context, listItemID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void activateReminder() {
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), alarmIntent);
    }

    public void setNotificationText(String title, int listItemID, String note) {
        intent.putExtra("title", title);
        intent.putExtra("alarmID", listItemID);
        intent.putExtra("note", note);
    }
    public void setNotificationClickInfo(int listID, String listTitle, String listColor) {
        intent.putExtra("ListID", listID);
        intent.putExtra("ListTitle", listTitle);
        intent.putExtra("ListColor", listColor);
    }

    public void cancelReminder(int listItemID) {
        alarmIntent = PendingIntent.getService(context, listItemID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(alarmIntent);
    }


}
