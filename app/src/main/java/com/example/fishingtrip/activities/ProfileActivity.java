package com.example.fishingtrip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fishingtrip.R;
import com.example.fishingtrip.databas.DBHelper;
import com.example.fishingtrip.models.AppUser;
import com.example.fishingtrip.recyclerView.AppUserRecyclerViewAdapter;

import static com.example.fishingtrip.constants.UserSharedPref.SHARED_PREF_LOGIN;
import static com.example.fishingtrip.constants.UserSharedPref.USER_NAME_DATA;

public class ProfileActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private String userLoginData;
    private DBHelper dbHelper;
    private RecyclerView appUserRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loadUserData();
        dbHelper = new DBHelper(this);
        appUserRecyclerView = findViewById(R.id.listAppUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setDataToRecyclerView();
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

    /**
     *  Create Actionbar menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_menu_fishingtrip_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        // Set menu to activity, inflater handel the print!
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     *  On clicked item in actionbar selector.
     */
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
     * Create Context Menu.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    /**
     *  Set onItemSelected listener.
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuItemUpdate:
                updateAppUser();
                break;
            case R.id.menuItemDelete:
                deleteAppUser();
                break;
        }

        return super.onContextItemSelected(item);
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


    /**
     *  Set Data from database to RecyclerView.
     */
    public void setDataToRecyclerView(){
        AppUserRecyclerViewAdapter appUserRecyclerAdapter = new AppUserRecyclerViewAdapter(this, dbHelper.getAllUsers());
        appUserRecyclerView.setAdapter(appUserRecyclerAdapter);
        appUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void updateAppUser() {
        // Update user...

    }

    private void deleteAppUser() {
        // Delete user....

    }
}