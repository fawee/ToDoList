package com.android.project.todolist.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.project.todolist.domain.ListObject;
import com.android.project.todolist.domain.ListItem;
import com.android.project.todolist.reminder.ReminderAlarm;
import com.android.project.todolist.log.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;


public class ListRepository  {
    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_tblList = "tbl_List";
    private static final String TABLE_tblListItem = "tbl_ListItem";

    public static final String KEY_List_ID = "Lst_ID";
    public static final String KEY_List_Title = "Lst_Title";
    public static final String KEY_List_Colour = "Lst_Colour";

//    public static final int COLUMN_TASK_INDEX = 1;
//    public static final int COLUMN_DATE_INDEX = 2;

    public static final String KEY_ListItem_ID = "LstItem_ID";
    public static final String KEY_ListItem_Title = "LstItem_Title";
    public static final String KEY_ListItem_Note = "LstItem_Note";
    public static final String KEY_ListItem_Priority = "LstItem_Priority";
    public static final String KEY_ListItem_DueDate = "LstItem_DueDate";
    public static final String KEY_ListItem_isDone = "LstItem_isDone";
    public static final String KEY_ListItem_reminder = "LstItem_reminder";
    public static final String KEY_ListItem_ReminderDate = "LstItem_ReminderDate";
    public static final String KEY_ListItem_List_ID = "LstItem_List_ID";

//    //Indexe noch setzen
//    public static final int COLUMN_TASK_INDEX = 1;
//    public static final int COLUMN_DATE_INDEX = 2;
    private Context context;
    private ToDoDBOpenHelper dbHelper;
    public String dbPath;
    public String dbName;

    private SQLiteDatabase db;

    public ListRepository(Context context) {
        this.context = context;
        dbHelper = new ToDoDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        initDBPath();
    }

    private void initDBPath() {
        dbName = dbHelper.getDatabaseName();
        dbPath = String.valueOf(context.getDatabasePath(dbName));
    }

    public void open() throws SQLException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public int insertList(ListObject list) {
        ContentValues newListValues = new ContentValues();

        newListValues.put(KEY_List_Title, list.getTitle());
        newListValues.put(KEY_List_Colour, list.getColour());

        return (int)db.insert(TABLE_tblList, null, newListValues);
    }

    public int insertListItem(ListItem item) {
        ContentValues newListItemValues = new ContentValues();

        newListItemValues.put(KEY_ListItem_Title, item.getTitle());
        newListItemValues.put(KEY_ListItem_Note, item.getNote());
        newListItemValues.put(KEY_ListItem_Priority, item.getPriority());
        newListItemValues.put(KEY_ListItem_DueDate, item.getDueDate());
        newListItemValues.put(KEY_ListItem_isDone, item.getIsDone());
        newListItemValues.put(KEY_ListItem_reminder, item.getReminder());
        newListItemValues.put(KEY_ListItem_ReminderDate, item.getReminderDate());
        newListItemValues.put(KEY_ListItem_List_ID, item.getListID());

        return (int)db.insert(TABLE_tblListItem, null, newListItemValues);
    }

    public void removeList(ListObject list) {
        ReminderAlarm reminderAlarm = new ReminderAlarm(context);   //deklarierung des Klassenobjects
        //Loop to remove each Reminder in the List

        String whereClause = KEY_ListItem_List_ID + " = " + list.getListID() + " AND " + KEY_ListItem_reminder + " = " + 1;
        Cursor cursor = db.query(TABLE_tblListItem, new String[]{KEY_ListItem_ID},whereClause, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int listItemID = cursor.getInt(0);
                reminderAlarm.cancelReminder(listItemID);  //aufruf der Klassenmethode zum entfernen eines Reminder
            } while (cursor.moveToNext());
        }

        //Delete of ListItems of List
        whereClause = KEY_ListItem_List_ID + " = " + list.getListID();
        db.delete(TABLE_tblListItem, whereClause, null);

        //Final delete of List
        whereClause = KEY_List_ID + " = " + list.getListID();
        db.delete(TABLE_tblList, whereClause, null);
    }

    public void removeListItem(ListItem item) {
        String whereClause = KEY_ListItem_ID + " = " + item.getListItemID();

        db.delete(TABLE_tblListItem, whereClause, null);
    }

    public ArrayList<ListObject> getAllLists() {
        ArrayList<ListObject> lists = new ArrayList<ListObject>();
        Cursor cursor = db.query(TABLE_tblList, new String[] { KEY_List_ID,KEY_List_Title, KEY_List_Colour}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id= cursor.getInt(0);
                String title = cursor.getString(1);
                String colour = cursor.getString(2);

                lists.add(new ListObject(id, title, getNumOfListItems(id, false), colour));

            } while (cursor.moveToNext());
        }
        return lists;
    }

    public int updateList(ListObject list){
        ContentValues cv = new ContentValues();
        cv.put(KEY_List_Title, list.getTitle());
        cv.put(KEY_List_Colour, list.getColour());
        return (int) db.update(TABLE_tblList, cv, KEY_List_ID + " = " + list.getListID(), null);
    }

    public int updateListItem(ListItem item){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ListItem_Title, item.getTitle());
        cv.put(KEY_ListItem_Note, item.getNote());
        cv.put(KEY_ListItem_Priority, item.getPriority());
        cv.put(KEY_ListItem_DueDate, item.getDueDate());
        cv.put(KEY_ListItem_isDone, item.getIsDone());
        cv.put(KEY_ListItem_reminder, item.getReminder());
        cv.put(KEY_ListItem_ReminderDate, item.getReminderDate());
        int tmp = db.update(TABLE_tblListItem, cv, KEY_ListItem_ID + " = " + item.getListItemID(), null);
        return tmp;
    }

    public ArrayList<ListItem> getItemsOfList(int listID) {
        ArrayList<ListItem> items = new ArrayList<ListItem>();
        Cursor cursor = db.query(TABLE_tblListItem, new String[]{KEY_ListItem_ID,
                        KEY_ListItem_Title,
                        KEY_ListItem_Note,
                        KEY_ListItem_Priority,
                        KEY_ListItem_DueDate,
                        KEY_ListItem_isDone,
                        KEY_ListItem_reminder,
                        KEY_ListItem_ReminderDate},
                KEY_ListItem_List_ID + " = " + listID, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String note = cursor.getString(2);
                int priority = cursor.getInt(3);
                long dueDate = cursor.getLong(4);
                boolean isDone = booleanOfInt(cursor.getInt(5));
                boolean reminder = booleanOfInt(cursor.getInt(6));
                long reminderDate = cursor.getLong(7);
                items.add(new ListItem( id,
                        title,
                        note,
                        priority,
                        dueDate,
                        isDone,
                        reminder,
                        reminderDate,
                        listID));
            } while (cursor.moveToNext());
        }
        return items;
    }

    public int getNumOfListItems(int listID, boolean isDone) {
        String whereClause = KEY_ListItem_List_ID + " = " + listID;
        if (isDone){
            whereClause += " AND " + KEY_ListItem_isDone +" = "+ 1;
        }
        else {
            whereClause += " AND " + KEY_ListItem_isDone +" = "+ 0;
        }
        Cursor cursor = db.query(TABLE_tblListItem,new String[] {KEY_ListItem_List_ID}, whereClause, null,null,null,null);
        return cursor.getCount();
    }

    private boolean booleanOfInt(int i){
        if (i == 1){
            return true;
        }
        return false;
    }

    public byte[] getDBByteArray(){

        File file = new File(dbPath);
        int size = (int) file.length();
        byte[] data = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(data, 0, data.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }

    public void setDBByteArray(byte[] data){

        FileOutputStream backUp = null;
        try {
            backUp = new FileOutputStream(dbPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            backUp.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            backUp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    private class ToDoDBOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_CREATE_tblList = "create table " + TABLE_tblList
                + " (" + KEY_List_ID + " integer primary key autoincrement, " +
                KEY_List_Title + " text not null, " +
                KEY_List_Colour + " text);";

        private static final String DATABASE_CREATE_tblListItem = "create table " + TABLE_tblListItem
                + " (" + KEY_ListItem_ID + " integer primary key autoincrement, " +
                KEY_ListItem_Title + " text not null, " +
                KEY_ListItem_Note + " text, " +
                KEY_ListItem_Priority + " integer, " +
                KEY_ListItem_DueDate + " integer, " +
                KEY_ListItem_isDone + " integer, " +
                KEY_ListItem_reminder + " integer, " +
                KEY_ListItem_ReminderDate + " integer, " +
                KEY_ListItem_List_ID + " integer);";

        public ToDoDBOpenHelper(Context c, String dbname,
                                SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_tblList);
            db.execSQL(DATABASE_CREATE_tblListItem);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
