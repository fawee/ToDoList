package com.android.project.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.project.todolist.R;
import com.android.project.todolist.domain.ListItem;

import java.util.ArrayList;

/**
 * Created by fabian on 27.08.15.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> {

    private Context context;
    private ArrayList<ListItem> listItemArrayList;
    private int sub_menu_single_listitem;


    public ListItemAdapter(Context context, ArrayList<ListItem> listItemArrayList) {

        super(context, R.layout.sub_menu_single_listitem, listItemArrayList);
        this.context = context;
        this.listItemArrayList = listItemArrayList;
        this.sub_menu_single_listitem = R.layout.sub_menu_single_listitem;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(sub_menu_single_listitem, null);
        } else {
            view = convertView;
        }

        CheckBox title = (CheckBox) view.findViewById(R.id.listItem_checkBox);
        title.setText(listItemArrayList.get(position).getTitle());

        TextView date = (TextView) view.findViewById(R.id.listItem_date);
        date.setText(listItemArrayList.get(position).getStringFromDueDate());

        TextView note = (TextView) view.findViewById(R.id.listItem_note);
        String userNote = listItemArrayList.get(position).getNote();
        if(userNote.length() > 10) {
            note.setText(userNote.substring(0, 15) + "...");
        } else {
            note.setText(userNote);
        }





        return view;
    }
}
