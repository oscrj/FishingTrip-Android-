package com.example.fishingtrip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.fishingtrip.R;
import com.example.fishingtrip.databas.DBHelper;

import static com.example.fishingtrip.constants.UserSharedPref.SHARED_PREF_LOGIN;
import static com.example.fishingtrip.constants.UserSharedPref.USER_NAME_DATA;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister;
    private ActionBar actionBar;
    private String userLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if user already logged in
        loadUserData();

        if(userLoginData.isEmpty() || userLoginData.equals("User Not found!")){

            /**
             *  On click user start Login Activity
             */
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginActivity);
                }
            });

            /**
             *  On click user start Register Activity
             */
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent registerActivity = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(registerActivity);
                }
            });
        }else{

            Intent homeActivity = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homeActivity);
        }

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

        // Set menu to activity, inflater handel the print!
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu_no_permit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionBarRegister) {
            Intent register = new Intent(this, RegisterActivity.class);
            startActivity(register);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * SAVE User login session.
     */
    public void saveUserDATA(){

    }

    /**
     * load User data if user are logged in.
     */
    public void loadUserData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_LOGIN, MODE_PRIVATE);
        userLoginData = sharedPreferences.getString(USER_NAME_DATA, "User Not found!");
    }

    /**
     * clear data if user logout.
     */
    public void clearUserData(){

    }
}
