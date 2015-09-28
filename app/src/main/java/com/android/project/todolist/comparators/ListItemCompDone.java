package com.android.project.todolist.comparators;

import com.android.project.todolist.domain.ListItem;

import java.util.Comparator;

/**
 * This class is used to sort the Items according to their status (whether they are done or not).
 */
public class ListItemCompDone implements Comparator<ListItem> {
    @Override
    public int compare(ListItem listItem1, ListItem listItem2) {
        boolean b1 = listItem1.getIsDone();
        boolean b2 = listItem2.getIsDone();
        if( b1 && ! b2 ) {
            return 1;
        }
        if( ! b1 && b2 ) {
            return -1;
        }
        return 0;
    }
}
