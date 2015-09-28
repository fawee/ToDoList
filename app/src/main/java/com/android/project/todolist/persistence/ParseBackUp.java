package com.android.project.todolist.persistence;

import android.content.Context;
import android.widget.Toast;

import com.android.project.todolist.R;
import com.android.project.todolist.log.Log;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;

/**
 * This class is used as interface between the ParseActivity and the SQL-Database, to manage the BackUp and Restore
 */

public class ParseBackUp {

    private Context context;
    private ListRepository db;

    public ParseBackUp(Context context) {
        this.context = context;
        db = new ListRepository(context);
    }

    public void uploadBackUp(){
        ParseFile backUpFile = new ParseFile("backup.db", db.getDBByteArray());
        backUpFile.saveInBackground();
        ParseObject dbBackUp = new ParseObject("DBBackUp");
        dbBackUp.put("file", backUpFile);
        dbBackUp.put("User", ParseUser.getCurrentUser());
        dbBackUp.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(context, context.getString(R.string.toast_cloud_backup_success), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void downloadBackUp(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("DBBackUp");
        query.whereEqualTo("User", ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                    Toast.makeText(context, context.getString(R.string.toast_cloud_restore_nobackup), Toast.LENGTH_LONG).show();
                } else {
                    ParseFile backUpFile = object.getParseFile("file");
                    try {
                        byte[] data = backUpFile.getData();
                        db.setDBByteArray(data);
                        Toast.makeText(context, context.getString(R.string.toast_cloud_restore_success), Toast.LENGTH_LONG).show();
                    } catch (ParseException e1) {
                        Toast.makeText(context, context.getString(R.string.toast_cloud_restore_fail), Toast.LENGTH_LONG).show();
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        Toast.makeText(context, context.getString(R.string.toast_cloud_restore_fail), Toast.LENGTH_LONG).show();
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
