package com.android.project.todolist.menues;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.android.project.todolist.R;
import com.android.project.todolist.adapter.ListItemAdapter;
import com.android.project.todolist.domain.ListItem;
import com.android.project.todolist.domain.ListObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class SubMenu extends ActionBarActivity {

    private ListView listView;
    private ListItemAdapter listItemAdapter;
    private ArrayList<ListItem> listItemArrayList;

    private static final int REQUEST_CODE_ADD_LISTITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGUI();
        connectData();
        readIntents();

    }

    private void connectData() {
        listItemArrayList = new ArrayList<ListItem>();
        listItemAdapter = new ListItemAdapter(this, listItemArrayList);
        listView.setAdapter(listItemAdapter);
    }


    private void readIntents() {
        readIntentsFromMainMenu();
    }


    private void readIntentsFromMainMenu() {
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("name");
        TextView nameOfList = (TextView) findViewById(R.id.tvSubMenuNameListObject);
        nameOfList.setText(name);
    }

    private void setupGUI() {
        setContentView(R.layout.activity_sub_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        listView = (ListView) findViewById(R.id.listViewSubMenu);
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
                updateChanges();
                onBackPressed();
                return true;


            case R.id.action_addListItem:
                addListItem();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //Aktualisiert Anzahl der ListItems in der Liste
    private void updateChanges() {
        int numOfListItems = listItemArrayList.size();
        Intent i = getIntent();
        i.putExtra("NumOfListItems", numOfListItems);
        setResult(RESULT_OK, i);

    }

    private void addListItem() {
        Intent i = new Intent(this, AddListItemMenu.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_LISTITEM) {
            if (resultCode == RESULT_OK) {
                String title = data.getExtras().getString("Title");
                String date = data.getExtras().getString("Date");
                String note = data.getExtras().getString("Note");

                Date dueDate = getDateFromString(date);
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(dueDate);
                ListItem listItem = new ListItem(title, note, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                listItemArrayList.add(listItem);
                listItemAdapter.notifyDataSetChanged();
            }
        }
    }

    private Date getDateFromString(String dateString) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,
                Locale.GERMANY);
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            // return current date as fallback
            return new Date();
        }
    }
}


