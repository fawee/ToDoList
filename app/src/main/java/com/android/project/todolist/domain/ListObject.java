package com.android.project.todolist.domain;

/**
 * Created by fabian on 24.08.15.
 */
public class ListObject {

    private long listID;
    private String title;
    private int numOfListItems;
    private int colour;

    public ListObject(int listID, String title, int numOfListItems, int colour) {

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

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour= colour;
    }

    public long getListID() {
        return listID;
    }

    public void setListID(long listID) {
        this.listID= listID;
    }

}
