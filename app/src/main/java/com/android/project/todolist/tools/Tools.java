package com.android.project.todolist.tools;

import android.view.View;

import com.android.project.todolist.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Patrick on 09.09.2015.
 */
public class Tools {

    public static void setColor(String color, View view) {
        switch (color){
            case "blue":
                view.setBackgroundResource(R.color.blue);
                break;
            case "green":
                view.setBackgroundResource(R.color.green);
                break;
            case "red":
                view.setBackgroundResource(R.color.red);
                break;
            case "yellow":
                view.setBackgroundResource(R.color.yellow);
                break;
            case "lime":
                view.setBackgroundResource(R.color.lime);
                break;
            case "white":
                view.setBackgroundResource(R.color.white);
                break;
            case "deep_purple":
                view.setBackgroundResource(R.color.deep_purple);
                break;
            case "teal":
                view.setBackgroundResource(R.color.teal);
                break;
            case "indigo":
                view.setBackgroundResource(R.color.indigo);
                break;
            default:
                view.setBackgroundResource(R.color.actionbar_color);
        }
    }




}