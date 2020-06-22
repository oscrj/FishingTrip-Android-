package com.example.fishingtrip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingtrip.R;
import com.example.fishingtrip.databas.DBHelper;
import com.example.fishingtrip.models.Catch;
import com.example.fishingtrip.models.FishingTrip;
import com.example.fishingtrip.recyclerView.CatchRecyclerViewAdapter;


import static com.example.fishingtrip.constants.UserSharedPref.SHARED_PREF_LOGIN;
import static com.example.fishingtrip.constants.UserSharedPref.USER_NAME_DATA;

public class NewTripActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private String userLoginData;
    private TextView txtLocation;
    private Button btnAddCatch, btnEndTrip;
    private Intent getFishingTripData;
    private DBHelper dbHelper;
    private Spinner species;
    private EditText length, weight;
    private Button btnConfirmAddCatch;
    private final String[] inputSpecies = new String[1];
    private RecyclerView catchRecyclerView;
    private CatchRecyclerViewAdapter catchRecyclerViewAdapter;
    private FishingTrip thisFishingTrip = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        loadUserData();
        txtLocation = findViewById(R.id.txtHeaderFishingTrip);
        btnAddCatch = findViewById(R.id.btnAddCatch);
        btnEndTrip = findViewById(R.id.btnEndTrip);
        catchRecyclerView = findViewById(R.id.listOfCatches);
        dbHelper = new DBHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromIntent();
        setDataToRecyclerView();

        btnAddCatch.setOnClickListener(this);
        btnEndTrip.setOnClickListener(this);
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
     * SAVE User login session.
     */
    public void saveUserDATA(){

    }

    /**
     *  Set inItemSelected on context menu.
     * @param item - the item user clicked on.
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 100:
                updateCatch(item);
                break;
            case 101:
                catchRecyclerViewAdapter.deleteFishCaught(item.getGroupId());
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Update caught fish.
     * @param item
     */
    private void updateCatch(MenuItem item) {

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
     *  Set Header to trip location and receive data from fishingTrip.
     */
    private void getDataFromIntent() {
        getFishingTripData = getIntent();
        thisFishingTrip = dbHelper.getFishingTripById(getFishingTripData.getStringExtra("FISHING_TRIP_ID"));
        txtLocation.setText(thisFishingTrip.getLocation());
    }

    public void setDataToRecyclerView(){
        catchRecyclerViewAdapter = new CatchRecyclerViewAdapter(this, dbHelper.getCatchWithFishTripId(String.valueOf(thisFishingTrip.getFishingTripId())));
        catchRecyclerView.setAdapter(catchRecyclerViewAdapter);
        catchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddCatch:
                addCatchDialog();
                break;
            case R.id.btnEndTrip:
                break;
        }
    }

    public void addCatchDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_catch_dialog, null);
        alertDialogBuilder.setView(dialogView);

        species = dialogView.findViewById(R.id.spinSpecies);
        length = dialogView.findViewById(R.id.inputLength);
        weight = dialogView.findViewById(R.id.inputWeight);
        btnConfirmAddCatch = dialogView.findViewById(R.id.btnAddCatchSubmit);

        final AlertDialog addCatchDialog = alertDialogBuilder.create();
        addCatchDialog.show();

        ArrayAdapter adp = ArrayAdapter.createFromResource(this, R.array.species, android.R.layout.simple_spinner_item);
        species.setAdapter(adp);
        species.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputSpecies[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnConfirmAddCatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(length.getText().toString().isEmpty()){
                    length.setText("0.0");
                }
                if (weight.getText().toString().isEmpty()){
                    weight.setText("0.0");
                }

                double fishLength = Double.parseDouble(length.getText().toString());
                double fishWeight = Double.parseDouble(weight.getText().toString());

                Catch newCatch = new Catch(-1, inputSpecies[0], fishLength, fishWeight, String.valueOf(thisFishingTrip.getFishingTripId()));
                boolean status = dbHelper.addCatch(newCatch);
                addCatchDialog.hide();
                if (status){
                    setDataToRecyclerView();
                    Toast.makeText(NewTripActivity.this, "ADDED", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NewTripActivity.this, "NOT ADDED", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}