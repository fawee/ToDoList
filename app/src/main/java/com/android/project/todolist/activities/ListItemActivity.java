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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.todolist.R;
import com.android.project.todolist.adapter.ListItemAdapter;
import com.android.project.todolist.communicator.DeleteNotifier;
import com.android.project.todolist.comparators.ListItemCompAlphabet;
import com.android.project.todolist.comparators.ListItemCompDone;
import com.android.project.todolist.comparators.ListItemCompPriority;
import com.android.project.todolist.dialogs.DeleteDialog;
import com.android.project.todolist.domain.ListItem;
import com.android.project.todolist.persistence.ListRepository;
import com.android.project.todolist.reminder.ReminderAlarm;
import com.android.project.todolist.tools.Tools;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the content of one List.
 * It displays the Items of a certain List.
 */

public class ListItemActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener, DeleteNotifier {

    private ListView listView;
    private ListItemAdapter listItemAdapter;
    private ArrayList<ListItem> listItems;
    private ListRepository db;

    //received Intent Values
    private Bundle extras;
    private int listID;
    private String listTitle, listColor;

    private ImageButton deleteListItemButton;
    private AdapterView.AdapterContextMenuInfo info;

    //Für den Reminder

    private ReminderAlarm reminderAlarm;


    private static final int REQUEST_CODE_ADD_LISTITEM = 1;
    private int deleteOption = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readIntents();
        initDB();
        initArrayList();
        initUI();
    }

    private void readIntents() {
        extras = getIntent().getExtras();
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
        tvListTitle.setOnClickListener(this);
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
        if (db.getNumOfListItems(listID, true) > 0) {
            deleteListItemButton.setVisibility(View.VISIBLE);
        } else {
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
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            case R.id.listItem_FloatingMenu_Edit:
                editListItem(info.position);
                return true;

            case R.id.listItem_FloatingMenu_delete:
                deleteOption = 1;
                deleteListItem();
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

            case R.id.action_sortAlphabeticallyLI:
                sortListItemsAlphabetically();
                break;

            case R.id.action_sortPriority:
                sortListItemsPriority();
                break;

            case R.id.action_sortDate:
                sortListItemsDate();
                break;

            case R.id.action_sortDone:
                sortListItemsDone();
                break;


            case R.id.action_addListItem:
                addListItem();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortListItemsDone() {
        Collections.sort(listItems, new ListItemCompDone());
        listItemAdapter.notifyDataSetChanged();
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
        Intent i = new Intent(this, ListItemDetailActivity.class);
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
        Intent i = new Intent(this, ListItemDetailActivity.class);
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

                if (reminder) {

                    reminderAlarm = new ReminderAlarm(this);
                    reminderAlarm.setReminderTime(data.getExtras().getInt("year"), data.getExtras().getInt("month"), data.getExtras().getInt("day"), data.getExtras().getInt("hour"), data.getExtras().getInt("minute"));
                    reminderAlarm.setNotificationText(listItem.getTitle(), listItem.getListItemID(), listItem.getNote());
                    reminderAlarm.setNotificationClickInfo(listID, listTitle, listColor);
                    reminderAlarm.setPendingIntent(listItem.getListItemID());
                    reminderAlarm.activateReminder();

                    //TODO: Noch im listItem reminder Datum darstellen? Oder Toast.
                } else {

                    if (reminderAlarm != null) {
                        reminderAlarm.cancelReminder(listItem.getListItemID());
                    }
                }

                listItemAdapter.notifyDataSetChanged();
            }
        }
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
                deleteOption = 0;
                deleteListItem();
                break;

            case R.id.tvSubMenuNameListObject:
                showListInfo();
        }
    }

    private void showListInfo() {

        Toast.makeText(getApplicationContext(), "You have " + listItems.size() + " Items in this List.", Toast.LENGTH_LONG).show();
    }

    //Öffnet Delete Dialog
    private void deleteListItem() {
        DeleteDialog dialog = new DeleteDialog();
        dialog.setMessage(getString(R.string.message));
        dialog.show(getFragmentManager(), "DeleteDialog");
    }


    //Delete all ListItems which marked with the flag "isDone"
    @Override
    public void onDeleted() {
        int numOfDeletedItems = 0;
        String message = " Item";
        if (deleteOption == 0) {

            int itemCount = listItems.size();
            for (int i = 0; i < itemCount; i++) {
                if (listItems.get(i).getIsDone()) {
                    numOfDeletedItems++;
                    if (reminderAlarm != null) {
                        reminderAlarm.cancelReminder(listItems.get(i).getListItemID());
                    }
                    db.removeListItem(listItems.get(i));
                    //listItemAdapter.remove(listItems.get(i));
                }
            }
            //checkedItemPositions.clear();
            initArrayList();
            initUI();
            //listItemAdapter.notifyDataSetChanged();
        } else {
            numOfDeletedItems = 1;

            if (reminderAlarm != null) {
                reminderAlarm.cancelReminder(listItems.get(info.position).getListItemID());
            }

            db.removeListItem(listItems.get(info.position));
            listItems.remove(info.position);
            listItemAdapter.notifyDataSetChanged();
            setDeleteButtonVisibility();
        }

        if (numOfDeletedItems > 1) {
            message += "s";
        }
        Toast.makeText(getApplicationContext(), "Deleted " + numOfDeletedItems + message, Toast.LENGTH_SHORT).show();

    }




}
