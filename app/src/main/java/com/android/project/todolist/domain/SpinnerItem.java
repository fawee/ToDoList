package com.android.project.todolist.domain;

/**
 * Created by fabian on 02.09.15.
 */
public class SpinnerItem {

    private String colorName;
    private int color;

    public SpinnerItem(String colorName, int color) {
        this.colorName = colorName;
        this.color = color;
    }

    public String getColorName() {
        return colorName;
    }

    public int getColor() {
        return color;
    }
}
