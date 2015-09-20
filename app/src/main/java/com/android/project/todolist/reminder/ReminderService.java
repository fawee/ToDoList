package com.android.project.todolist.reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.android.project.todolist.R;
import com.android.project.todolist.activities.ListItemActivity;
import com.android.project.todolist.communicator.ReminderNotifier;
import com.android.project.todolist.log.Log;

/**
 * Created by fabian on 18.09.15.
 */
public class ReminderService extends Service {

    private ReminderNotifier reminderNotifier;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int alarmID = intent.getExtras().getInt("alarmID");
        String title = intent.getExtras().getString("title");
        String note = intent.getExtras().getString("note");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_list_item_alarm_white)
                        .setContentTitle(title)
                        .setTicker("Reminder")
                        .setContentText(note);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);


        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(alarmID, mBuilder.build());
        return super.onStartCommand(intent, flags, startId);
    }
}
