package com.android.project.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.project.todolist.R;
import com.android.project.todolist.domain.SpinnerItem;

import java.util.ArrayList;


public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {
    private Context context;
    private int spinnerLayout;
    private ArrayList<SpinnerItem> list;



    public SpinnerAdapter(Context context, ArrayList<SpinnerItem> list) {
        super(context,R.layout.dialog_add_list_spinner, list);
        this.list = list;
        this.context = context;
        this.spinnerLayout = R.layout.dialog_add_list_spinner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(spinnerLayout, parent, false);
        } else {
            view = convertView;
        }

        TextView colorName = (TextView) view.findViewById(R.id.colorName);
        TextView color = (TextView) view.findViewById(R.id.color);

        colorName.setText(list.get(position).getColorName());
        color.setBackgroundResource(list.get(position).getColor());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
