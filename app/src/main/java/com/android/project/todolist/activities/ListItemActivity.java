package com.android.project.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.project.todolist.R;
import com.android.project.todolist.adapter.ListItemAdapter;
import com.android.project.todolist.domain.ListItem;
import com.android.project.todolist.persistence.ListRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class ListItemActivity extends ActionBarActivity implements AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ListItemAdapter listItemAdapter;
    private ArrayList<ListItem> listItems;
    private ListRepository db;
    private int listID;
    private String listTitle;

    private static final int REQUEST_CODE_ADD_LISTITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readIntents();
        initDB();
        initArrayList();
        initUI();
    }

    private void readIntents() {
        Bundle extras = getIntent().getExtras();
        listID = extras.getInt("ListID");
        listTitle = extras.getString("ListTitle");
        TextView tvListTitle = (TextView) findViewById(R.id.tvSubMenuNameListObject);
//        tvListTitle.setText(listTitle);
    }

    private void initDB(){
        db = new ListRepository(this);
        db.open();
    }

    private void initArrayList(){
        listItems = new ArrayList<ListItem>();
        listItems = db.getItemsOfList(listID);
    }
    private void initUI() {
        setContentView(R.layout.activity_sub_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        listView = (ListView) findViewById(R.id.listViewSubMenu);
        listItemAdapter = new ListItemAdapter(this, listItems);
        listView.setAdapter(listItemAdapter);
        listView.setOnItemLongClickListener(this);
    }

    //Men
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
        int numOfListItems = listItems.size();
        Intent i = getIntent();
        i.putExtra("NumOfListItems", numOfListItems);
        setResult(RESULT_OK, i);

    }

    private void addListItem() {
        Intent i = new Intent(this, AddListItemActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_LISTITEM) {
            if (resultCode == RESULT_OK) {
                String title = data.getExtras().getString("Title");
                String date = data.getExtras().getString("Date");
                String note = data.getExtras().getString("Note");
                String priority = data.getExtras().getString("Priority");

                Date dueDate = getDateFromString(date);
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(dueDate);
                ListItem newListItem = new ListItem(1, title, note, Integer.parseInt(priority), cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), false, false, listID);
                newListItem.setListItemID(db.insertListItem(newListItem));
                listItems.add(newListItem);
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

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }
}


