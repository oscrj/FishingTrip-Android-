package com.example.fishingtrip.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingtrip.R;
import com.example.fishingtrip.databas.DBHelper;
import com.example.fishingtrip.models.AppUser;

public class RegisterActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView inputUsername, inputPassword, inputFirstName, inputLastName, inputEmail;
    private Button btnRegister;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUsername = findViewById(R.id.inputUserName);
        inputPassword = findViewById(R.id.inputPassword);
        inputFirstName = findViewById(R.id.inputFirstname);
        inputLastName = findViewById(R.id.inputLastName);
        inputEmail = findViewById(R.id.inputEmail);
        btnRegister = findViewById(R.id.btnRegisterSubmit);
        dbHelper = new DBHelper(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Set onclick listener on button register and create new user from inputs by User.
         */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUser newUser = new AppUser(-1, inputUsername.getText().toString(),
                        inputPassword.getText().toString(), inputFirstName.getText().toString(),
                        inputLastName.getText().toString(), inputEmail.getText().toString());

                boolean status = dbHelper.addAppUser(newUser);

                if(status){
                    Toast.makeText(RegisterActivity.this, "User Registered!", Toast.LENGTH_SHORT).show();
                    Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginActivity);

                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    alertDialogBuilder.setMessage("User was NOT added!");

                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent registerActivity = new Intent(RegisterActivity.this, RegisterActivity.class);
                            startActivity(registerActivity);
                        }
                    });
                    AlertDialog addedUserFailed = alertDialogBuilder.create();
                    addedUserFailed.show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_menu_fishingtrip_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu_no_permit, menu);

        return super.onCreateOptionsMenu(menu);
    }
}