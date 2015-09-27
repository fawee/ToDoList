package com.android.project.todolist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.android.project.todolist.R;
import com.android.project.todolist.log.Log;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * A login screen that offers login via email/password.
 */
public class ParseRegisterActivity extends ActionBarActivity {

    private String user;
    private String password;
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parse_register);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Set up the login form.
        Typeface editTextFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Italic.ttf");

        mEmailView = (EditText) findViewById(R.id.user_register);
        mEmailView.setTypeface(editTextFont);

        mPasswordView = (EditText) findViewById(R.id.password_register);
        mPasswordView.setTypeface(editTextFont);

        Button mEmailSignInButton = (Button) findViewById(R.id.register_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptSignin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        user = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a user and passwort
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.parse_error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(user)) {
            mEmailView.setError(getString(R.string.parse_error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            parseRegister();
        }
    }

    private void parseRegister() {
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(user);
        parseUser.setPassword(password);

        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.d("Signed Up");
                    Toast.makeText(getApplicationContext(), "Your Sigh In were successful. Now you can login.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.d(String.valueOf(e));
                    Toast.makeText(getApplicationContext(), "Your Sigh In were not successful.", Toast.LENGTH_LONG).show();
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}

