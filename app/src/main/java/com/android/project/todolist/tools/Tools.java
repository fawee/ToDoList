package com.android.project.todolist.tools;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.todolist.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Patrick on 09.09.2015.
 */
public class Tools {



    public static int currentListColor = 0;

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

    public static int getColor(String color) {
        switch (color){
            case "blue":
                return R.color.blue;
            case "green":
                return R.color.green;
            case "red":
                return R.color.red;
            case "yellow":
                return R.color.yellow;
            case "lime":
                return R.color.lime;
            case "white":
                return R.color.white;
            case "deep_purple":
                return R.color.deep_purple;
            case "teal":
                return R.color.teal;
            case "indigo":
                return R.color.indigo;
            default:
                return R.color.actionbar_color;
        }
    }






}
