package com.android.project.todolist.comparators;

import com.android.project.todolist.domain.ListObject;

import java.util.Comparator;

/**
 * Created by fabian on 15.09.15.
 */
public class ListCompAlphabet implements Comparator<ListObject> {
    @Override
    public int compare(ListObject list1, ListObject list2) {
        return list1.getTitle().compareToIgnoreCase(list2.getTitle());
    }
}
