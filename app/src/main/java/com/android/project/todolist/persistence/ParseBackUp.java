package com.android.project.todolist.persistence;

import android.content.Context;

import com.android.project.todolist.log.Log;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Patrick on 01.09.2015.
 */
public class ParseBackUp {

    private Context context;
    private ListRepository db;

    public ParseBackUp(Context context) {
        this.context = context;
        db = new ListRepository(context);
    }

    public boolean uploadBackUp(){
        ParseFile backUpFile = new ParseFile("backup.db", db.getDBByteArray());
        backUpFile.saveInBackground();
        ParseObject dbBackUp = new ParseObject("DBBackUp");
        dbBackUp.put("file", backUpFile);
        dbBackUp.put("User", ParseUser.getCurrentUser());
        dbBackUp.saveInBackground();
        return false;
    }

    public boolean downloadBackUp(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("DBBackUp");
        query.whereEqualTo("User", ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                } else {
                    ParseFile backUpFile = object.getParseFile("file");
                    try {
                        byte[] data = backUpFile.getData();
                        db.setDBByteArray(data);
                        Log.d("after: " + String.valueOf(data));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    Log.d("score", "Retrieved the object.");
                }
            }
        });
        return false;
    }
}
