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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fishingtrip.R;
import com.example.fishingtrip.databas.DBHelper;
import com.example.fishingtrip.models.FishingTrip;

import static com.example.fishingtrip.constants.UserSharedPref.SHARED_PREF_LOGIN;
import static com.example.fishingtrip.constants.UserSharedPref.USER_NAME_DATA;

public class CreateNewTripActivity extends AppCompatActivity {

    private Spinner fishingMethod, waterType;
    private String inputFishingMethod, inputWaterType;
    private TextView inputLocation;
    private Button btnAddTrip;
    private String userLoginData;
    private ActionBar actionBar;
    private DBHelper dbHelper;
    private ArrayAdapter adapterMethod, adapterWaterType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_trip);

        loadUserData();
        fishingMethod = findViewById(R.id.spinFishingMethod);
        waterType = findViewById(R.id.spinWaterType);
        inputLocation = findViewById(R.id.inputFishingLocation);
        btnAddTrip = findViewById(R.id.btnAddFishingTrip);
        dbHelper = new DBHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getInputFromFishingMethod();
        getInputFromWaterType();

        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputLocation.getText().toString().isEmpty()){
                    inputLocation.setText("Secret Location");
                }

                FishingTrip newFishingTrip = new FishingTrip(-1, inputFishingMethod, inputWaterType, inputLocation.getText().toString(),
                        userLoginData, true);

                boolean status = dbHelper.addFishingTrip(newFishingTrip);

                if (status){
                    Intent fishingTrips = new Intent(CreateNewTripActivity.this, FishingTripsActivity.class);
                    startActivity(fishingTrips);
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateNewTripActivity.this);
                    alertDialogBuilder.setMessage("Your Trip was NOT added!");

                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent createNewTripActivity = new Intent(CreateNewTripActivity.this, CreateNewTripActivity.class);
                            startActivity(createNewTripActivity);
                        }
                    });
                    AlertDialog addedTripFailed = alertDialogBuilder.create();
                    addedTripFailed.show();
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

    /**
     *  Create Spinner fishingMethod.
     *  Get input from Spinner fishingMethod and set selected value to inputFishingMethod.
     */
    private void getInputFromFishingMethod() {
        adapterMethod = ArrayAdapter.createFromResource(this, R.array.fishingMethod, android.R.layout.simple_spinner_item);
        fishingMethod.setAdapter(adapterMethod);
        fishingMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputFishingMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *  Create Spinner waterType.
     *  Get input from Spinner WaterType and set selected value to inputWaterType.
     */
    private void getInputFromWaterType() {
        adapterWaterType = ArrayAdapter.createFromResource(this, R.array.waterType, android.R.layout.simple_spinner_item);
        waterType.setAdapter(adapterWaterType);
        waterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputWaterType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}