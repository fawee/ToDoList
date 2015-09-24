package com.android.project.todolist.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.project.todolist.R;
import com.android.project.todolist.log.Log;
import com.android.project.todolist.persistence.ParseBackUp;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * A login screen that offers login via email/password.
 */
public class ParseActivity extends ActionBarActivity{

    private String user;
    private String password;
    private boolean loggedIn;
    private ParseUser currentUser;
    private ParseBackUp parseBackUp;
    // UI references.
    private EditText etUserView;
    private EditText etPasswordView;

    private TextView tvLoggedInUser;
    private TextView tvOr;

    private Button btnLogInButton;
    private Button btnRegisterButton;
    private Button btnCloudUpButton;
    private Button btnCloudDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);

        initParse();
        initUI();

    }

    private void initParse(){
        try {
            Parse.initialize(this, "CaBmyO31WPv6Q3B3ruuBSUSL34afvoGTzjpO95do", "ChZnNxYgNjL4KllEZddIdaGN3QV0tTLRlz7vkvLc");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(String.valueOf(e));
        }

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null){
            loggedIn = false;
        }
        else {
            loggedIn = true;
        }
        /*// Test
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        */
        parseBackUp = new ParseBackUp(this);
    }

    private void initUI() {
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        // Set up the login form.
        Typeface editTextFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Italic.ttf");
        etUserView = (EditText) findViewById(R.id.user_login);
        etPasswordView = (EditText) findViewById(R.id.password_register);
        etUserView.setTypeface(editTextFont);
        etPasswordView.setTypeface(editTextFont);

        tvLoggedInUser = (TextView) findViewById(R.id.parse_tv_LoggedInUser);
        tvOr = (TextView) findViewById(R.id.parse_tv_or);

        btnLogInButton = (Button) findViewById(R.id.login_button);
        btnRegisterButton = (Button) findViewById(R.id.open_register_activity_button);
        btnCloudUpButton = (Button) findViewById(R.id.cloud_up_button);
        btnCloudDownButton = (Button) findViewById(R.id.cloud_down_button);
        initEvents();
        switchLook();
    }

    private void initEvents() {
/*
        etPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogIn();
                    return true;
                }
                return false;
            }
        });
*/
        btnLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggedIn){
                    logOut();
                }
                else{
                    attemptLogIn();
                }
            }
        });

        btnRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ParseRegisterActivity.class);
                startActivity(i);
            }
        });

        btnCloudUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                parseBackUp.uploadBackUp();
            }
        });

        btnCloudDownButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                parseBackUp.downloadBackUp();
            }
        });
    }

    /**
     * Attempts to login
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogIn() {
        // Reset errors.
        etUserView.setError(null);
        etPasswordView.setError(null);

        // Store values at the time of the login attempt.
        user = etUserView.getText().toString();
        password = etPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a user and passwort
        if (TextUtils.isEmpty(password)) {
            etPasswordView.setError(getString(R.string.parse_error_field_required));
            focusView = etPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(user)) {
            etUserView.setError(getString(R.string.parse_error_field_required));
            focusView = etUserView;
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
                    loggedIn = true;
                    currentUser = ParseUser.getCurrentUser();
                    Log.d("Loged In");
                    switchLook();
                    // Hooray! The user is logged in.
                } else {
                    Log.d(String.valueOf(e));
                    //todo: toast, dass login fehlgeschlagen hat
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }

    private void logOut() {
        ParseUser.logOut();
        loggedIn = false;
        currentUser = ParseUser.getCurrentUser();
        switchLook();
    }

    private void switchLook() {
        if (loggedIn){

            // Set Visability
            tvLoggedInUser.setVisibility(View.VISIBLE);
            etUserView.setVisibility(View.GONE);
            etPasswordView.setVisibility(View.GONE);
            tvOr.setVisibility(View.INVISIBLE);
            btnRegisterButton.setVisibility(View.INVISIBLE);
            btnCloudUpButton.setVisibility(View.VISIBLE);
            btnCloudDownButton.setVisibility(View.VISIBLE);
            //Set label
            btnLogInButton.setText(R.string.parse_action_logout);
            String loggedInUser = getResources().getString(R.string.parse_loggedin_user);
            loggedInUser = loggedInUser.replace("[user]", currentUser.getUsername());
            tvLoggedInUser.setText(loggedInUser);
        }
        else {
            // Set Visability
            tvLoggedInUser.setVisibility(View.GONE);
            etUserView.setVisibility(View.VISIBLE);
            etPasswordView.setVisibility(View.VISIBLE);
            tvOr.setVisibility(View.VISIBLE);
            btnRegisterButton.setVisibility(View.VISIBLE);
            btnCloudUpButton.setVisibility(View.INVISIBLE);
            btnCloudDownButton.setVisibility(View.INVISIBLE);
            tvLoggedInUser.setVisibility(View.INVISIBLE);
            //Set label
            btnLogInButton.setText(R.string.parse_action_login);
        }
    }
}

