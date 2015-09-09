package com.android.project.todolist.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Patrick on 09.09.2015.
 */
public class Tools {
    public static Date getDateFromString(String dateString) {
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
    try {
        return df.parse(dateString);
    } catch (ParseException e) {
        // TODo, keine gute idee
        return new Date();
    }
}
}
