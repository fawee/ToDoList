package com.android.project.todolist.tools;

import android.view.View;
import com.android.project.todolist.R;

/**
 * This class is just used for getting the correct color in different activities.
 */

public class Tools {


    public static int currentListColor = 0;

    public static void setColor(String color, View view) {
        switch (color) {
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
            case "pink":
                view.setBackgroundResource(R.color.pink);
                break;
            case "orange":
                view.setBackgroundResource(R.color.orange);
                break;
            default:
                view.setBackgroundResource(R.color.actionbar_color);
        }
    }

    public static int getColor(String color) {
        switch (color) {
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
            case "pink":
                return R.color.pink;
            case "orange":
                return R.color.orange;
            default:
                return R.color.actionbar_color;
        }
    }
}
