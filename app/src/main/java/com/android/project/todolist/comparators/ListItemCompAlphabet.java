package com.android.project.todolist.comparators;

import com.android.project.todolist.domain.ListItem;

import java.util.Comparator;

/**
 * This class is used to sort the Items alphabetically.
 */

public class ListItemCompAlphabet implements Comparator<ListItem> {
    @Override
    public int compare(ListItem listItem1  , ListItem listItem2) {
        return listItem1.getTitle().compareToIgnoreCase(listItem2.getTitle());
    }
}
