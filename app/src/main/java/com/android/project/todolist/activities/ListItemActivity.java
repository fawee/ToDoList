package com.android.project.todolist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.project.todolist.R;
import com.android.project.todolist.adapter.ListItemAdapter;
import com.android.project.todolist.domain.ListItem;
import com.android.project.todolist.log.Log;
import com.android.project.todolist.persistence.ListRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.android.project.todolist.tools.Tools.getDateFromString;


public class ListItemActivity extends ActionBarActivity {

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
        TextView tvListTitle = (TextView) findViewById(R.id.tvSubMenuNameListObject);
        tvListTitle.setText(listTitle);
        listView = (ListView) findViewById(R.id.listViewSubMenu);
        listItemAdapter = new ListItemAdapter(this, listItems);
        listView.setAdapter(listItemAdapter);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.listitem_floating_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.listItem_FloatingMenu_delete:
                db.removeListItem(listItems.get(info.position));
                listItems.remove(info.position);
                listItemAdapter.notifyDataSetChanged();
                return true;
            case R.id.listItem_FloatingMenu_Edit:
                editListItem(info.position);
                return true;
            /*case R.id.listItem_FloatingMenu_IsDone:
                listItems.get(info.position).setIsDone(true);
                //ToDo DB speichern
                return true;*/
            default:
                return super.onContextItemSelected(item);
        }
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
                onBackPressed();
                return true;

            case R.id.action_addListItem:
                addListItem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addListItem() {
        Intent i = new Intent(this, AddListItemActivity.class);
        i.putExtra("listItemID", 0);
        i.putExtra("listItemTitle", "");
        i.putExtra("listItemDueDate", "");
        i.putExtra("listItemNote", "");
        i.putExtra("listItemPriority", 3);
        i.putExtra("listItemReminder", false);
        i.putExtra("listItemReminderDate", "");
        i.putExtra("listID", listID);
        startActivityForResult(i, 1);
    }

    private void editListItem(int itemPossition) {
        Intent i = new Intent(this, AddListItemActivity.class);
        i.putExtra("listItemID", listItems.get(itemPossition).getListItemID());
        i.putExtra("listItemTitle", listItems.get(itemPossition).getTitle());
        i.putExtra("listItemDueDate", listItems.get(itemPossition).getStringFromDueDate());
        i.putExtra("listItemNote", listItems.get(itemPossition).getNote());
        i.putExtra("listItemPriority", listItems.get(itemPossition).getPriority());
        i.putExtra("listItemReminder", listItems.get(itemPossition).getReminder());
        i.putExtra("listItemReminderDate", listItems.get(itemPossition).getStringFromReminderDate());
        i.putExtra("listID", listItems.get(itemPossition).getListID());
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_LISTITEM) {
            if (resultCode == RESULT_OK) {
                int listItemID =data.getExtras().getInt("ListItemID");
                String title = data.getExtras().getString("Title");
                String dDate = data.getExtras().getString("DueDate");
                String note = data.getExtras().getString("Note");
                int priority = data.getExtras().getInt("Priority");
                boolean reminder = data.getExtras().getBoolean("Reminder");
                String rDate = data.getExtras().getString("ReminderDate");
                int listID = data.getExtras().getInt("ListID");

                //if(!dDate.equals("")) {
                Date dueDate = getDateFromString(dDate);
                GregorianCalendar calDueDate = new GregorianCalendar();
                calDueDate.setTime(dueDate);
                //}
                //if(!rDate.equals("")) {
                Date reminderDate = getDateFromString(rDate);
                GregorianCalendar calReminderDate = new GregorianCalendar();
                calReminderDate.setTime(reminderDate);
                //}

                ListItem listItem = new ListItem(listItemID,
                                                title,
                                                note,
                                                priority,
                                                calDueDate.get(Calendar.YEAR), calDueDate.get(Calendar.MONTH), calDueDate.get(Calendar.DAY_OF_MONTH),
                                                false,
                                                reminder,
                                                calReminderDate,
                                                listID);
                //listItem listItem = new listItem(1, title, note, Integer.parseInt(priority), calDueDate.get(Calendar.YEAR), calDueDate.get(Calendar.MONTH), calDueDate.get(Calendar.DAY_OF_MONTH), false, reminder, rDate, listID);
                if (data.getExtras().getInt("ListItemID") == 0){
                    listItem.setListItemID(db.insertListItem(listItem));
                    listItems.add(listItem);

                }
                else {
                    db.updateListItem(listItem);
                    for (int i = 0; i < listItems.size(); i++) {
                        if (listItems.get(i).getListItemID() == listItem.getListItemID()) {
                            listItems.set(i, listItem);
                        }
                    }
                }
                listItemAdapter.notifyDataSetChanged();
            }
        }
    }
}


