package com.android.project.todolist.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.project.todolist.domain.ListObject;
import com.android.project.todolist.domain.ListItem;

import java.lang.String;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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

    private ToDoDBOpenHelper dbHelper;

    private SQLiteDatabase db;

    public ListRepository(Context context) {
        dbHelper = new ToDoDBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        newListItemValues.put(KEY_ListItem_DueDate, item.getStringFromDueDate());
        newListItemValues.put(KEY_ListItem_isDone, item.getIsDone());
        newListItemValues.put(KEY_ListItem_reminder, item.getReminder());
        newListItemValues.put(KEY_ListItem_ReminderDate, item.getStringFromReminderDate());
        newListItemValues.put(KEY_ListItem_List_ID, item.getListID());

        return (int)db.insert(TABLE_tblListItem, null, newListItemValues);
    }

    public void removeList(ListObject list) {
        //Nicht nur die Liste sondern auch die dazugehörigen ListItems müssen gelöscht werden, sollange in der DB noch keine Löschweitergabe implementiert ist, muss das manuel geschehen
        String whereClause = KEY_ListItem_List_ID + " = " + list.getListID();
        db.delete(TABLE_tblListItem, whereClause, null);


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
                int colour = cursor.getInt(2);

                lists.add(new ListObject(id, title, getNumOfListItems(id), colour));

            } while (cursor.moveToNext());
        }
        return lists;
    }

    private int updateList(ListObject list){
        ContentValues cv = new ContentValues();
        cv.put(KEY_List_Title, list.getTitle());
        cv.put(KEY_List_Colour, list.getColour());
        return (int) db.update(TABLE_tblList, cv, KEY_List_ID + " = " + list.getListID(), null);
    }

    private int updateListItem(ListItem item){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ListItem_Title, item.getTitle());
        cv.put(KEY_ListItem_Note, item.getNote());
        cv.put(KEY_ListItem_Priority, item.getNote());
        cv.put(KEY_ListItem_DueDate, item.getStringFromDueDate());
        cv.put(KEY_ListItem_isDone, item.getIsDone());
        cv.put(KEY_ListItem_reminder, item.getReminder());
        cv.put(KEY_ListItem_ReminderDate, item.getStringFromReminderDate());
        return (int)db.update(TABLE_tblListItem, cv, KEY_ListItem_ID + " = " + item.getListID(), null);
    }

    public ArrayList<ListItem> getItemsOfList(int listID) {
        ArrayList<ListItem> items = new ArrayList<ListItem>();
        Cursor cursor = db.query(TABLE_tblListItem, new String[] {  KEY_ListItem_ID,
                KEY_ListItem_Title,
                KEY_ListItem_Note,
                KEY_ListItem_Priority,
                KEY_ListItem_DueDate,
                KEY_ListItem_isDone,
                KEY_ListItem_reminder,
                KEY_ListItem_ReminderDate},
                KEY_ListItem_List_ID + " = " + listID,null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String note = cursor.getString(2);
                int priority = cursor.getInt(3);
                String dueDate = cursor.getString(4);
                boolean isDone = booleanOfInt(cursor.getInt(5));
                boolean reminder = booleanOfInt(cursor.getInt(6));
                String reminderDate = cursor.getString(7);

                // Formation of DueDate
                Date formatedDate = null;
                try {
                    formatedDate = new SimpleDateFormat("dd.MM.yy", Locale.GERMAN).parse(dueDate);
                    //formatedDate = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.GERMAN).parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calDueD = Calendar.getInstance(Locale.GERMAN);
                calDueD.setTime(formatedDate);

                // Formation of ReminderDate
                formatedDate = null;
                try {
                    formatedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN).parse(reminderDate);
                    //formatedDate = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.GERMAN).parse(reminderDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calRemD = Calendar.getInstance(Locale.GERMAN);
                calRemD.setTime(formatedDate);

                items.add(new ListItem(id, title, note, priority, calDueD.get(Calendar.DAY_OF_MONTH),
                        calDueD.get(Calendar.MONTH), calDueD.get(Calendar.YEAR), isDone, reminder, calRemD, listID));
            } while (cursor.moveToNext());
        }
        return items;
    }

    private int getNumOfListItems(int listID) {
        Cursor cursor = db.query(TABLE_tblListItem,new String[] {KEY_ListItem_List_ID}, KEY_ListItem_List_ID + " = " + listID, null,null,null,null);
        return cursor.getCount();
    }

    private boolean booleanOfInt(int i){
        if (i == 1){
            return true;
        }
        return false;
    }

    private class ToDoDBOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_CREATE_tblList = "create table " + TABLE_tblList
                + " (" + KEY_List_ID + " integer primary key autoincrement, " +
                KEY_List_Title + " text not null, " +
                KEY_List_Colour + " integer);";

        private static final String DATABASE_CREATE_tblListItem = "create table " + TABLE_tblListItem
                + " (" + KEY_ListItem_ID + " integer primary key autoincrement, " +
                KEY_ListItem_Title + " text not null, " +
                KEY_ListItem_Note + " text, " +
                KEY_ListItem_Priority + " integer, " +
                KEY_ListItem_DueDate + " text, " +
                KEY_ListItem_isDone + " integer, " +
                KEY_ListItem_reminder + " integer, " +
                KEY_ListItem_ReminderDate + " text, " +
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
