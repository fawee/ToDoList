package com.android.project.todolist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.android.project.todolist.R;
import com.android.project.todolist.log.Log;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * A login screen that offers login via email/password.
 */
public class ParseActivity extends Activity{

    private String user;
    private String password;
    // UI references.
    private EditText userView;
    private EditText passwordView;

    private Button logInButton;
    private Button registerButton;
    private Button cloudUpButton;
    private Button cloudDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);

        initParse();
        initUI();

    }

    private void initParse(){
        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);

        Parse.initialize(this, "CaBmyO31WPv6Q3B3ruuBSUSL34afvoGTzjpO95do", "ChZnNxYgNjL4KllEZddIdaGN3QV0tTLRlz7vkvLc");

        // Test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

    private void initUI() {
        // Set up the login form.
        userView = (EditText) findViewById(R.id.user_login);
        passwordView = (EditText) findViewById(R.id.password_register);

        logInButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.open_register_activity_button);
        cloudUpButton = (Button) findViewById(R.id.cloud_up_button);
        cloudDownButton = (Button) findViewById(R.id.cloud_up_button);
        initEvents();
    }

    private void initEvents() {
/*
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
*/
        logInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ParseRegisterActivity.class);
                startActivity(i);
            }
        });

        cloudUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //CloudUpload();
            }
        });

        cloudDownButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //CloudDownload();
            }
        });
    }


    /**
     * Attempts to login
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        // Reset errors.
        userView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        user = userView.getText().toString();
        password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a user and passwort
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(user)) {
            userView.setError(getString(R.string.error_field_required));
            focusView = userView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            parseLogin();
        }
    }

    private void parseLogin() {
        ParseUser.logInInBackground(user, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.d("Loged In");
                    // Hooray! The user is logged in.
                } else {
                    Log.d(String.valueOf(e));
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }
}

