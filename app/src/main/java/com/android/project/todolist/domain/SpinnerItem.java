package com.android.project.todolist.domain;

/**
 * Created by fabian on 02.09.15.
 */
public class SpinnerItem {

    private String colorName;
    private String colorValue;
    private int color;

    public SpinnerItem(String colorName, String colorValue, int color) {
        this.colorName = colorName;
        this.colorValue = colorValue;
        this.color = color;
    }

    public String getColorName() {
        return colorName;
    }

    public String getColorValue() {
        return colorValue;
    }

    public int getColor() {
        return color;
    }
}
