package com.android.project.todolist.domain;


public class ListObject {

    private String title;
    private int numOfListItems;

    public ListObject(String title) {

        this.title = title;
        numOfListItems = 0;

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
