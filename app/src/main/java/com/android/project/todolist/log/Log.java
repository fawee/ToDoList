package com.android.project.todolist.log;

/**
 * Created by Patrick on 24.08.2015.
 */
public class Log {
    public static final boolean DEBUG = true;
    public static final String TAG = "AdvToDoList:";

    public static void d(String tag, String msg) {
        if (DEBUG) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            android.util.Log.d(TAG, msg);
        }
    }
}

