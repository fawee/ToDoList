package com.android.project.todolist.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TimePicker;


import com.android.project.todolist.communicator.Communicator;
import com.android.project.todolist.dialogs.DialogAddListObject;
import com.android.project.todolist.domain.ListObject;
import com.android.project.todolist.adapter.ListObjectAdapter;
import com.android.project.todolist.R;
import com.android.project.todolist.persistence.ListRepository;


import java.util.ArrayList;


public class ListActivity extends ActionBarActivity implements Communicator, AdapterView.OnItemClickListener/*, AdapterView.OnItemLongClickListener*/ {

    private GridView main_menu_gridView;
    private ArrayList<ListObject> listObjects;
    private ListObjectAdapter listObjectAdapter;
    private ListObject listObject;
    private ListRepository db;


    private static final int REQUEST_CODE_OPEN_SUBMENU = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDB();
        initArrayList();
        initUI();
    }

    private void initDB(){
        db = new ListRepository(this);
        db.open();
    }

    private void initArrayList(){
        listObjects = new ArrayList<ListObject>();
        listObjects = db.getAllLists();
    }

    private void initUI() {
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        main_menu_gridView = (GridView) findViewById(R.id.gridView);
        listObjectAdapter = new ListObjectAdapter(this, listObjects);
        main_menu_gridView.setAdapter(listObjectAdapter);
        main_menu_gridView.setOnItemClickListener(this);

        registerForContextMenu(main_menu_gridView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_floating_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.list_FloatingMenu_delete:
                db.removeList(listObjects.get(info.position));
                listObjects.remove(info.position);
//                listObjects.clear();
//                listObjectAdapter.notifyDataSetChanged();
//                listObjects = db.getAllLists();
                listObjectAdapter.notifyDataSetChanged();
                return true;
            case R.id.list_FloatingMenu_Edit:
                //ToDo
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_settings:
                return true;

            case R.id.action_addListObject:
                addNewListObject();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewListObject() {
        DialogAddListObject dialog = new DialogAddListObject();
        dialog.show(getFragmentManager(), "Add List Object Dialog");

    }

    // Add new List to DB and UI
    @Override
    public void getInputData(String listTitle, int listColor) {
        ListObject newListObject = new ListObject(1, listTitle, 0, listColor);
        newListObject.setListID(db.insertList(newListObject));
        listObjects.add(newListObject);
        listObjectAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDate(DatePicker view, int year, int month, int day) {

    }

    @Override
    public void getTime(TimePicker view, int hourOfDay, int minute) {

    }

    //ClickListener für die einzelnen ListObjects
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listObjects.get(position) == null) {

        } else {
            Intent i = new Intent(getApplicationContext(), ListItemActivity.class);
            i.putExtra("ListID", listObjects.get(position).getListID());
            i.putExtra("ListTitle", listObjects.get(position).getTitle());
            startActivityForResult(i, REQUEST_CODE_OPEN_SUBMENU);
        }

        /*
        listObject = (ListObject) parent.getItemAtPosition(position);
        String listObjectTitle = listObject.getTitle();
        Intent intent = new Intent(ListActivity.this, ListItemActivity.class);
        intent.putExtra("name", listObjectTitle);
        startActivityForResult(intent, REQUEST_CODE_OPEN_SUBMENU);
*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Aktualisiert das Textview für die Anzahl der ListItems in der Liste
        if(requestCode == REQUEST_CODE_OPEN_SUBMENU) {
            // listObject.setNumOfListItems(data.getExtras().getInt("NumOfListItems"));
            listObjectAdapter.notifyDataSetChanged();
        }
    }

}
