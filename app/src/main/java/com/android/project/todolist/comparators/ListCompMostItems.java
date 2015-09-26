package com.android.project.todolist.comparators;

import com.android.project.todolist.domain.ListObject;

import java.util.Comparator;

/**
 * This class is used to sort the Lists according to the number of undone Items.
 */


public class ListCompMostItems implements Comparator<ListObject> {
    @Override
    public int compare(ListObject list1, ListObject list2) {
        return list2.getNumOfListItems() - list1.getNumOfListItems();
    }
}
