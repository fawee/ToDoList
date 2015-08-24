package com.example.fabian.todolist;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;


public class MainMenu extends ActionBarActivity implements  DialogAddListObject.OnAddButtonHandler {

    private GridView main_menu_gridView;
    private ArrayList<ListObject> listObjects;
    private ListObjectAdapter listObjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupGUI();
        connectData();

    }

    private void connectData() {
        listObjects = new ArrayList<>();
        listObjectAdapter = new ListObjectAdapter(this, listObjects);
        main_menu_gridView.setAdapter(listObjectAdapter);
    }

    private void setupGUI() {
        setContentView(R.layout.activity_main_menu);
        main_menu_gridView = (GridView) findViewById(R.id.gridView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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



    @Override
    public void getInputTextFromDialog(String inputText) {
        ListObject listObject = new ListObject(inputText, 0);
        listObjects.add(listObject);
        listObjectAdapter.notifyDataSetChanged();
    }
}
