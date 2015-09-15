package com.android.project.todolist.comparators;

import com.android.project.todolist.domain.ListItem;

import java.util.Comparator;

/**
 * Created by fabian on 15.09.15.
 */
public class ListItemCompPriority implements Comparator<ListItem> {
    @Override
    public int compare(ListItem listItem1, ListItem listItem2) {
        return listItem1.getPriority() - listItem2.getPriority();
    }
}
