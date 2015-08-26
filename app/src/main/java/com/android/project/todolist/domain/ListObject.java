package com.android.project.todolist.domain;

/**
 * Created by fabian on 24.08.15.
 */
public class ListObject {

    private String title;
    private int numOfListItems;

    public ListObject(String title, int numOfListItems) {

        this.title = title;
        this.numOfListItems = numOfListItems;

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
}
