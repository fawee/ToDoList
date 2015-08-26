package com.android.project.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.project.todolist.domain.ListObject;
import com.android.project.todolist.R;

import java.util.ArrayList;

/**
 * Created by fabian on 24.08.15.
 */
public class ListObjectAdapter extends ArrayAdapter<ListObject> {

    private Context context;
    private ArrayList<ListObject> arrayList;
    private int main_menu_single_list_layout;


    public ListObjectAdapter(Context context, ArrayList<ListObject> arrayList) {
        super(context, R.layout.main_menu_single_list, arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.main_menu_single_list_layout = R.layout.main_menu_single_list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(main_menu_single_list_layout, null);
        } else {
            view = convertView;
        }

        TextView tv_titleListObject = (TextView) view.findViewById(R.id.tv_listName);
        tv_titleListObject.setText(arrayList.get(position).getTitle());

        TextView tv_numOfListItems = (TextView) view.findViewById(R.id.tv_numOfListItems);
        tv_numOfListItems.setText(""+ arrayList.get(position).getNumOfListItems());


        return view;
    }
}
