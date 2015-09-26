package com.android.project.todolist.comparators;

import com.android.project.todolist.domain.ListObject;

import java.util.Comparator;

/**
 * This class is used to sort the Lists alphabetically.
 */


public class ListCompAlphabet implements Comparator<ListObject> {
    @Override
    public int compare(ListObject list1, ListObject list2) {
        return list1.getTitle().compareToIgnoreCase(list2.getTitle());
    }
}
