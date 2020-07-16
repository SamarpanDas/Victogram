/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener
{
    TextView loginTextView;
    Boolean signUpModeActive = true;
    EditText usernameEditText;
    EditText passwordEditText;


    public void showUserList()
    {
        Intent intent = new Intent(getApplicationContext(), UerListActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent)       // for the View.OnKeyListener
    {


        if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
        {
            signUpClicked(view);
        }


        return false;
    }

    public void onClick(View view)                     // for the implement onClickListener
    {



        if(view.getId() == R.id.loginTextView)
        {
            Log.i("SignUp / Login Switch was tapped", "Confirmed !!!!!");

            Button signUpButton = findViewById(R.id.signUpButton);

            if(signUpModeActive == true)
            {
                // go to login mode

                signUpModeActive = false;
                signUpButton.setText("Login");

                loginTextView.setText("Sign Up");
            }

            else
            {
                // go to signUp mode

                signUpModeActive = true;
                signUpButton.setText("Sign Up");

                loginTextView.setText("Login");

            }



        }

        else if(view.getId() == R.id.backgroundLayout || view.getId() == R.id.logoImageView){

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }



    }


    public void signUpClicked(View view)
    {

        if(usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches(""))
        {
            Toast.makeText(this, "Enter your username and password !!", Toast.LENGTH_SHORT).show();

        }
        else {

            if(signUpModeActive == true)
            {

                //  NEW USER SIGNING UP !!

                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());


                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Log.i("SignUp, Success", "Done");

                            showUserList();

                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.i("SignUp Failed", "Not Done !!!!!");
                        }

                    }
                });
            }

            else
            {
                // LOGIN SITUATION

                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e)
                    {
                        if(user != null)
                        {
                            Log.i("Login Successfull", "Inside App, Logged In");
                            showUserList();                                                          ////////////////////////////// Have myself added this line
                        }
                        else {
                            //e.printStackTrace();
                            Log.i("Login Failed", "Not Logged In");

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }
        }

    }


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


      loginTextView = findViewById(R.id.loginTextView);
      loginTextView.setOnClickListener(this);



      usernameEditText = findViewById(R.id.usernameEditText);
      passwordEditText = findViewById(R.id.passwordEditText);

      ImageView logoImageView = findViewById(R.id.logoImageView);
      RelativeLayout backgroundLayout = findViewById(R.id.backgroundLayout);

      logoImageView.setOnClickListener(this);
      backgroundLayout.setOnClickListener(this);



      passwordEditText.setOnKeyListener(this);


      if(ParseUser.getCurrentUser() != null)
      {
          showUserList();
      }




      ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}