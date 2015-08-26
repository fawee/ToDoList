package com.android.project.todolist.menues;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.project.todolist.dialogs.DialogAddListObject;
import com.android.project.todolist.domain.ListObject;
import com.android.project.todolist.adapter.ListObjectAdapter;
import com.android.project.todolist.R;

import java.util.ArrayList;


public class MainMenu extends ActionBarActivity implements DialogAddListObject.OnAddButtonHandler, AdapterView.OnItemClickListener {

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
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        main_menu_gridView = (GridView) findViewById(R.id.gridView);
        main_menu_gridView.setOnItemClickListener(this);
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



    @Override
    public void getInputTextFromDialog(String inputText) {
        ListObject listObject = new ListObject(inputText, 0);
        listObjects.add(listObject);
        listObjectAdapter.notifyDataSetChanged();
    }


    //ClickListener für die einzelnen ListObjects
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListObject object = (ListObject) parent.getItemAtPosition(position);
        String name = object.getTitle();
        Intent intent = new Intent(MainMenu.this, SubMenu.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }


}
