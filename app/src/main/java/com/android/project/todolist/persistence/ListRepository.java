package com.android.project.todolist.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.project.todolist.domain.ListItem;

/**
 * Created by Patrick on 24.08.2015.
 */
public class ListRepository  {
    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_tbl_List = "tbl_List";
    private static final String DATABASE_tbl_ListItem = "tbl_ListItem";

    public static final String KEY_List_ID = "List_ID";
    public static final String KEY_List_Title = "List_Title";
    public static final String KEY_List_Colour = "List_Colour";

    public static final int COLUMN_TASK_INDEX = 1;
    public static final int COLUMN_DATE_INDEX = 2;

    public static final String KEY_ListItem_ID = "ListItem_ID";
    public static final String KEY_ListItem_Title = "ListItem_Title";
    public static final String KEY_ListItem_Note = "ListItem_Note";
    public static final String KEY_ListItem_Priority = "ListItem_Priority";
    public static final String KEY_ListItem_DueDate = "ListItem_DueDate";
    public static final String KEY_ListItem_isDone = "ListItem_isDone";
    public static final String KEY_ListItem_reminder = "ListItem_reminder";
    public static final String KEY_ListItem_List_ID = "ListItem_List_ID";

    //Indexe noch setzen
    public static final int COLUMN_TASK_INDEX = 1;
    public static final int COLUMN_DATE_INDEX = 2;

    private ToDoDBOpenHelper dbHelper;

    private SQLiteDatabase db;

    public ListRepository(Context context) {
        dbHelper = new ToDoDBOpenHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
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

    public long insertList(ListItem list) {
        ContentValues newListValues = new ContentValues();

        newListValues.put(KEY_List_Title, list.getTitle());
        newListValues.put(KEY_List_Colour, list.getColour());

        return db.insert(DATABASE_tbl_List, null, newListValues);
    }
    public long insertListItem(ListItem item) {
        ContentValues newListItemValues = new ContentValues();

        newListItemValues.put(KEY_ListItem_Title, item.getTitle());
        newListItemValues.put(KEY_ListItem_Note, item.getNote());
        newListItemValues.put(KEY_ListItem_Priority, item.getPriority());
//ToDo: DueDate Datentyp noch anpassen
        newListItemValues.put(KEY_ListItem_DueDate, item.getDueDate());
        newListItemValues.put(KEY_ListItem_isDone, item.getIsDone());
        newListItemValues.put(KEY_ListItem_reminder, item.getReminder());
        newListItemValues.put(KEY_ListItem_List_ID, item.getListID());

        return db.insert(DATABASE_tbl_ListItem, null, newListItemValues);
    }

    //Bis hier angepasst
    public void removeList(ToDoItem item) {
        String whereClause = KEY_TASK + " = '" + item.getName() + "' AND "
                + KEY_DATE + " = '" + item.getFormattedDate() + "'";

        db.delete(DATABASE_TABLE, whereClause, null);
    }

    public void removeListItem(ListItem item) {
        String whereClause = KEY_TASK + " = '" + item.getName() + "' AND "
                + KEY_DATE + " = '" + item.getFormattedDate() + "'";

        db.delete(DATABASE_TABLE, whereClause, null);
    }
    //Bis hier hin Änderungen gemacht aber noch nicht fertig
    public ArrayList<ToDoItem> getAllToDoItems() {
        ArrayList<ToDoItem> items = new ArrayList<ToDoItem>();
        Cursor cursor = db.query(DATABASE_TABLE, new String[] { KEY_ID,
                KEY_TASK, KEY_DATE }, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String task = cursor.getString(COLUMN_TASK_INDEX);
                String date = cursor.getString(COLUMN_DATE_INDEX);

                Date formatedDate = null;
                try {
                    formatedDate = new SimpleDateFormat("dd.MM.yyyy",
                            Locale.GERMAN).parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar cal = Calendar.getInstance(Locale.GERMAN);
                cal.setTime(formatedDate);

                items.add(new ToDoItem(task, cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH), cal.get(Calendar.YEAR)));

            } while (cursor.moveToNext());
        }
        return items;
    }

    private class ToDoDBOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_CREATE = "create table "
                + DATABASE_TABLE + " (" + KEY_ID
                + " integer primary key autoincrement, " + KEY_TASK
                + " text not null, " + KEY_DATE + " text);";

        public ToDoDBOpenHelper(Context c, String dbname,
                                SQLiteDatabase.CursorFactory factory, int version) {
            super(c, dbname, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
