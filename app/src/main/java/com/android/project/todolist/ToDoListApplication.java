package com.android.project.todolist;

import android.app.Application;

import com.parse.Parse;


public class ToDoListApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        initParse();
    }

    private void initParse() {
        try {
            Parse.initialize(this, "CaBmyO31WPv6Q3B3ruuBSUSL34afvoGTzjpO95do", "ChZnNxYgNjL4KllEZddIdaGN3QV0tTLRlz7vkvLc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
