package com.android.project.todolist.domain;


/**
 * This class is used in the "DialogAddListObject" and represents a custom Spinner
 * where you can choose a color for your list.
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
