package com.example.fabian.todolist;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;


public class SubMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        TextView nameOfList = (TextView) findViewById(R.id.tvSubMenuNameListObject);
        nameOfList.setText(name);
    }
}
