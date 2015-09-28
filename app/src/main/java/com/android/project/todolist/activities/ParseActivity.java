package com.android.project.todolist.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.project.todolist.R;
import com.android.project.todolist.log.Log;
import com.android.project.todolist.persistence.ParseBackUp;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import static com.android.project.todolist.tools.Tools.hideKeyboard;

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

    private Button btnLogInButton;
    private Button btnRegisterButton;
    private Button btnInfoButton;
    private Button btnCloudUpButton;
    private Button btnCloudDownButton;

    private LinearLayout llLoggedInForm;
    private LinearLayout llLogInForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse);

        initParse();
        initUI();
    }

    private void initParse(){
        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null){
            loggedIn = false;
        }
        else {
            loggedIn = true;
        }

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

        btnLogInButton = (Button) findViewById(R.id.login_button);
        btnRegisterButton = (Button) findViewById(R.id.open_register_activity_button);
        btnInfoButton = (Button) findViewById(R.id.open_info);
        btnCloudUpButton = (Button) findViewById(R.id.cloud_up_button);
        btnCloudDownButton = (Button) findViewById(R.id.cloud_down_button);

        llLoggedInForm = (LinearLayout) findViewById(R.id.parse_loggedin_form);
        llLogInForm = (LinearLayout) findViewById(R.id.parse_login_form);
        initEvents();
        switchLook();
    }

    private void initEvents() {
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

        btnInfoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showHowToDialog();
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

        // Check for a user and password
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
                    switchLook();
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_login_successful), Toast.LENGTH_LONG).show();
                } else {
                    Log.d(String.valueOf(e));
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_login_fail), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void logOut() {
        ParseUser.logOut();
        loggedIn = false;
        currentUser = ParseUser.getCurrentUser();
        switchLook();
        Toast.makeText(getApplicationContext(), getString(R.string.toast_logout_successful), Toast.LENGTH_LONG).show();
    }

    private void switchLook() {
        if (loggedIn){
            hideKeyboard(this);
            //Set label
            btnLogInButton.setText(R.string.parse_action_logout);
            String loggedInUser = getResources().getString(R.string.parse_loggedin_user);
            loggedInUser = loggedInUser.replace("[user]", currentUser.getUsername());
            tvLoggedInUser.setText(loggedInUser);
            // Set Visibility
            tvLoggedInUser.setVisibility(View.VISIBLE);
            llLoggedInForm.setVisibility(View.VISIBLE);
            //etUserView.setVisibility(View.GONE);
            //etPasswordView.setVisibility(View.GONE);
            llLogInForm.setVisibility(View.GONE);
            btnRegisterButton.setVisibility(View.INVISIBLE);
            btnInfoButton.setVisibility(View.VISIBLE);
            btnCloudUpButton.setVisibility(View.VISIBLE);
            btnCloudDownButton.setVisibility(View.VISIBLE);
        }
        else {
            // Set Visibility
            tvLoggedInUser.setVisibility(View.GONE);
            llLoggedInForm.setVisibility(View.GONE);
            //etUserView.setVisibility(View.VISIBLE);
            //etPasswordView.setVisibility(View.VISIBLE);
            llLogInForm.setVisibility(View.VISIBLE);
            btnRegisterButton.setVisibility(View.VISIBLE);
            btnInfoButton.setVisibility(View.INVISIBLE);
            btnCloudUpButton.setVisibility(View.INVISIBLE);
            btnCloudDownButton.setVisibility(View.INVISIBLE);
            //Set label
            btnLogInButton.setText(R.string.parse_action_login);
        }
    }

    private void showHowToDialog(){
        String title = "How to use the Cloud BackUp";
        String text = "If you use the backup or restore function , you will informed by a toast (Notification at the bottom of the screen) if the task has been performed successfully." +
                " Did you have a bad or no internet connection the app can´t give you a response. It is still trying to perform the backup or restore in the background.";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(text);
        dialogBuilder.setPositiveButton("Got it!", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = dialogBuilder.create(); dialog.show();
    }
}

