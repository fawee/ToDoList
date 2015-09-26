package com.android.project.todolist.comparators;

import com.android.project.todolist.domain.ListItem;

import java.util.Comparator;

/**
 * This class is used to sort the Items according to their priority (1 = high, 2 = medium, 3 = low).
 */

public class ListItemCompPriority implements Comparator<ListItem> {
    @Override
    public int compare(ListItem listItem1, ListItem listItem2) {
        return listItem1.getPriority() - listItem2.getPriority();
    }
}
