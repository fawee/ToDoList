package com.android.project.todolist.persistence;

import android.content.Context;

/**
 * Created by Patrick on 01.09.2015.
 */
public class ParseBackUp {

    private Context context;
    private ListRepository db;

    public ParseBackUp(Context context) {
        this.context = context;
        db = new ListRepository(context);
    }

    public boolean uploadBackUp(){

        return false;
    }

    public boolean downloadBackUp(){

        return false;
    }
}
