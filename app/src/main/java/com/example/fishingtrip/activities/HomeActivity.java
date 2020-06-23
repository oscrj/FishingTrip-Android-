package com.example.fishingtrip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fishingtrip.R;
import com.example.fishingtrip.databas.DBHelper;

import static com.example.fishingtrip.constants.UserSharedPref.SHARED_PREF_LOGIN;
import static com.example.fishingtrip.constants.UserSharedPref.USER_NAME_DATA;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private TextView homeUserName;
    private Button btnNewTrip, btnAllTrips, btnAPI;
    private String userLoginData;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadUserData();
        homeUserName = findViewById(R.id.txtHeaderHome);
        btnNewTrip = findViewById(R.id.btnNewFishingtrip);
        btnAllTrips = findViewById(R.id.btnTrips);
        btnAPI = findViewById(R.id.btnApi);
        dbHelper = new DBHelper(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeUserName.setText(userLoginData);
        btnNewTrip.setOnClickListener(this);
        btnAllTrips.setOnClickListener(this);
        btnAPI.setOnClickListener(this);
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
        assert actionBar != null;
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_menu_fishingtrip_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        // Set menu to activity, inflater handel the print!
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionBarProfile:
                Intent profileActivity = new Intent(this, ProfileActivity.class);
                startActivity(profileActivity);
                break;
            case R.id.actionBarHome:
                Intent homeActivity = new Intent(this, HomeActivity.class);
                startActivity(homeActivity);
                break;
            case R.id.actionBarTrips:
                Intent tripsActivity = new Intent(this, FishingTripsActivity.class);
                startActivity(tripsActivity);
                break;
            case R.id.actionBarLogout:
                clearUserData();
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * load User data if user are logged in.
     */
    public void loadUserData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_LOGIN, MODE_PRIVATE);
        userLoginData = sharedPreferences.getString(USER_NAME_DATA, "Username not found!");
    }

    /**
     * clear data if user logout.
     */
    public void clearUserData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_NAME_DATA);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnNewFishingtrip:
                checkIfUserHasActiveTrips();
                break;
            case R.id.btnTrips:
                Intent allTrips = new Intent(this, FishingTripsActivity.class);
                startActivity(allTrips);
                break;
            case R.id.btnApi:
                Intent apiActivity = new Intent(this, ApiActivity.class);
                startActivity(apiActivity);
                break;
        }
    }

    /**
     *
     */
    private void checkIfUserHasActiveTrips() {
        boolean activeTrips = dbHelper.checkForActiveTrips(userLoginData);

        if (activeTrips){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
            alertDialogBuilder.setMessage("YOU HAVE AN ACTIVE TRIP! \n\nYou have to end trip before creating a new trip.");

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent fishingTripsActivity = new Intent(HomeActivity.this, FishingTripsActivity.class);
                    startActivity(fishingTripsActivity);
                }
            });
            AlertDialog activeTripsFound = alertDialogBuilder.create();
            activeTripsFound.show();

        }else{
            Intent createNewTrip = new Intent(this, CreateNewTripActivity.class);
            startActivity(createNewTrip);
        }
    }
}