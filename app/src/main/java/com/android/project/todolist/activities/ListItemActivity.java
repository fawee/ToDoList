package com.android.project.todolist.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.project.todolist.R;
import com.android.project.todolist.adapter.ListItemAdapter;
import com.android.project.todolist.comparators.ListItemCompAlphabet;
import com.android.project.todolist.comparators.ListItemCompPriority;
import com.android.project.todolist.domain.ListItem;
import com.android.project.todolist.persistence.ListRepository;
import com.android.project.todolist.reminder.ReminderService;
import com.android.project.todolist.tools.Tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ListItemActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listView;
    private ListItemAdapter listItemAdapter;
    private ArrayList<ListItem> listItems;
    private ListRepository db;
    private int listID;
    private String listTitle, listColor;
    private ImageButton deleteListItemButton;

    //Für den Reminder
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private Intent reminderIntent;


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
        listColor = extras.getString("ListColor");
        Tools.currentListColor = Tools.getColor(listColor);
    }

    private void initDB() {
        db = new ListRepository(this);
        db.open();
    }

    private void initArrayList() {
        listItems = new ArrayList<ListItem>();
        listItems = db.getItemsOfList(listID);
    }

    private void initUI() {
        setContentView(R.layout.activity_sub_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        TextView tvListTitle = (TextView) findViewById(R.id.tvSubMenuNameListObject);
        tvListTitle.setText(listTitle);
        Tools.setColor(listColor, tvListTitle);
        deleteListItemButton = (ImageButton) findViewById(R.id.delete_listItems);
        deleteListItemButton.setOnClickListener(this);
        setDeleteButtonVisibility();
        listView = (ListView) findViewById(R.id.listViewSubMenu);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);
        listItemAdapter = new ListItemAdapter(this, listItems);
        listView.setAdapter(listItemAdapter);
        registerForContextMenu(listView);
    }

    private void setDeleteButtonVisibility() {
        if (db.getNumOfListItems(listID, true) > 0){
            deleteListItemButton.setVisibility(View.VISIBLE);
        }
        else{
            deleteListItemButton.setVisibility(View.INVISIBLE);
        }
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
        switch (item.getItemId()) {

            case R.id.listItem_FloatingMenu_Edit:
                editListItem(info.position);
                return true;

            case R.id.listItem_FloatingMenu_delete:
                deleteItem(info);
            /*case R.id.listItem_FloatingMenu_IsDone:
                listItems.get(info.position).setIsDone(true);
                //ToDo DB speichern
                return true;*/
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteItem(AdapterView.AdapterContextMenuInfo info) {
        db.removeListItem(listItems.get(info.position));
        listItems.remove(info.position);
        listItemAdapter.notifyDataSetChanged();
        setDeleteButtonVisibility();
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

            case R.id.action_sortAlphabeticallyLI:
                sortListItemsAlphabetically();
                break;

            case R.id.action_sortPriority:
                sortListItemsPriority();
                break;

            case R.id.action_sortDate:
                sortListItemsDate();
                break;

            case R.id.action_addListItem:
                addListItem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortListItemsDate() {
        Collections.sort(listItems);
        listItemAdapter.notifyDataSetChanged();
    }

    private void sortListItemsPriority() {
        Collections.sort(listItems, new ListItemCompPriority());
        listItemAdapter.notifyDataSetChanged();
    }

    private void sortListItemsAlphabetically() {
        Collections.sort(listItems, new ListItemCompAlphabet());
        listItemAdapter.notifyDataSetChanged();
    }

    private void addListItem() {
        Intent i = new Intent(this, AddListItemActivity.class);
        i.putExtra("listItemID", 0);
        i.putExtra("listItemTitle", "");
        i.putExtra("listItemDueDate", "");
        i.putExtra("listItemNote", "");
        i.putExtra("listItemPriority", 0);
        i.putExtra("listItemReminder", false);
        i.putExtra("listItemReminderDate", "");
        i.putExtra("listID", listID);
        i.putExtra("listColor", listColor);
        startActivityForResult(i, 1);
    }

    private void editListItem(int itemPosition) {
        Intent i = new Intent(this, AddListItemActivity.class);
        i.putExtra("listItemID", listItems.get(itemPosition).getListItemID());
        i.putExtra("listItemTitle", listItems.get(itemPosition).getTitle());
        i.putExtra("listItemDueDate", listItems.get(itemPosition).getDueDate());
        i.putExtra("listItemNote", listItems.get(itemPosition).getNote());
        i.putExtra("listItemPriority", listItems.get(itemPosition).getPriority());
        i.putExtra("listItemIsDone", listItems.get(itemPosition).getIsDone());
        i.putExtra("listItemReminder", listItems.get(itemPosition).getReminder());
        i.putExtra("listItemReminderDate", listItems.get(itemPosition).getReminderDate());
        i.putExtra("listID", listItems.get(itemPosition).getListID());
        i.putExtra("listColor", listColor);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_LISTITEM) {
            if (resultCode == RESULT_OK) {
                int listItemID = data.getExtras().getInt("ListItemID");
                String title = data.getExtras().getString("Title");
                long dueDate = data.getExtras().getLong("DueDate");
                String note = data.getExtras().getString("Note");
                int priority = data.getExtras().getInt("Priority");
                boolean isDone = data.getExtras().getBoolean("IsDone");
                boolean reminder = data.getExtras().getBoolean("Reminder");
                long reminderDate = data.getExtras().getLong("ReminderDate");
                int listID = data.getExtras().getInt("ListID");

                ListItem listItem = new ListItem(listItemID,
                        title,
                        note,
                        priority,
                        dueDate,
                        isDone,
                        reminder,
                        reminderDate,
                        listID);

                if (data.getExtras().getInt("ListItemID") == 0) {
                    listItem.setListItemID(db.insertListItem(listItem));
                    listItems.add(listItem);

                } else {
                    db.updateListItem(listItem);
                    for (int i = 0; i < listItems.size(); i++) {
                        if (listItems.get(i).getListItemID() == listItem.getListItemID()) {
                            listItems.set(i, listItem);
                        }
                    }
                }

                //Einstellungen für den Reminder

                reminderIntent = new Intent(this, ReminderService.class);
                if(reminder) {

                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Calendar alarm = Calendar.getInstance();
                    setReminderDate(data, alarm);

                    reminderIntent.putExtra("title", listItem.getTitle());
                    reminderIntent.putExtra("alarmID", listItem.getListItemID());
                    reminderIntent.putExtra("note", listItem.getNote());

                    alarmIntent = PendingIntent.getService(this, listItem.getListItemID(), reminderIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), alarmIntent);
                    //TODO: Noch im listItem reminder Datum darstellen? Oder Toast.
                } else {

                    if(alarmManager != null) {
                        cancelReminder(listItem.getListItemID(), reminderIntent);
                    }
                }

                listItemAdapter.notifyDataSetChanged();
            }
        }
    }

    private void cancelReminder(int listItemID, Intent intent) {
        alarmIntent = PendingIntent.getService(this, listItemID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(alarmIntent);
    }

    private void setReminderDate(Intent data, Calendar alarm) {
        alarm.set(Calendar.YEAR, data.getExtras().getInt("year"));
        alarm.set(Calendar.MONTH, data.getExtras().getInt("month")-1);
        alarm.set(Calendar.DAY_OF_MONTH, data.getExtras().getInt("day"));
        alarm.set(Calendar.HOUR_OF_DAY, data.getExtras().getInt("hour"));
        alarm.set(Calendar.MINUTE, data.getExtras().getInt("minute"));
        alarm.set(Calendar.SECOND, 0);
    }

    //todo speichern
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!listItems.get(position).getIsDone()) {
            listItems.get(position).setIsDone(true);
            listView.setItemChecked(position, true);

        } else {
            listItems.get(position).setIsDone(false);
            listView.setItemChecked(position, false);

        }
        int tmp = db.updateListItem(listItems.get(position));
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i).getListItemID() == listItems.get(position).getListItemID()) {
                listItems.set(i, listItems.get(position));
            }
        }
        setDeleteButtonVisibility();
        listItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_listItems:
                deleteListItem();
                break;
        }
    }

    //Delete all ListItems which marked with the flag "isDone"
    private void deleteListItem() {
        //ToDo: Dialog um vom User nochmal die Bestätigung zu bekommen.
        int itemCount = listItems.size();
        for (int i = 0; i < itemCount; i++) {
            if (listItems.get(i).getIsDone()) {
                db.removeListItem(listItems.get(i));
                //listItemAdapter.remove(listItems.get(i));
            }
        }
        //checkedItemPositions.clear();
        initArrayList();
        initUI();
        //listItemAdapter.notifyDataSetChanged();
        //Todo: Hier noch ne Toast Message um User mitzuteilen wie viele gelöscht wurde.

    }
}
