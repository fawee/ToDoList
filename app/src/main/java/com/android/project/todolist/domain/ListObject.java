package com.android.project.todolist.domain;


/**
 * This class represents a List on the Home-Activity.
 */

public class ListObject {

    private int listID;
    private String title;
    private int numOfListItems;
    private String colour;

    public ListObject(int listID, String title, int numOfListItems, String colour) {

        this.listID = listID;
        this.title = title;
        this.numOfListItems = numOfListItems;
        this.colour = colour;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumOfListItems() {
        return numOfListItems;
    }

    public void setNumOfListItems(int numOfListItems) {
        this.numOfListItems = numOfListItems;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour= colour;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID= listID;
    }


}
