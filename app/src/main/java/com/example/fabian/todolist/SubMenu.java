package com.example.fabian.todolist;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SubMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        TextView nameOfList = (TextView) findViewById(R.id.tvSubMenuNameListObject);
        nameOfList.setText(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sub_menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            
            case R.id.action_addListItem:
                addListItem();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void addListItem() {

    }
}
