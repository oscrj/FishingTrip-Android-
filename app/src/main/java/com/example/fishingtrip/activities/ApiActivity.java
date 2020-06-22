package com.example.fishingtrip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fishingtrip.R;
import com.example.fishingtrip.volley.VolleyReq;
import com.example.fishingtrip.models.UserModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.fishingtrip.constants.QueryMessages.REQ_RES_URL;
import static com.example.fishingtrip.constants.UserSharedPref.SHARED_PREF_LOGIN;
import static com.example.fishingtrip.constants.UserSharedPref.USER_NAME_DATA;

public class ApiActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private String userLoginData;
    private Button btnGET, btnPOST, btnPUT, btnDELETE;
    private TextView txtRequestResult, txtId, txtName, txtEmail;
    private ImageView profileImage;
    private ListView usersList;
    private ArrayAdapter arrayAdapter;
    private ArrayList<UserModel> userModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        loadUserData();
        txtRequestResult = findViewById(R.id.txtResult);
        txtId = findViewById(R.id.dialogTxtId);
        txtName = findViewById(R.id.dialogTxtFullname);
        txtEmail = findViewById(R.id.dialogTxtEmail);
        profileImage = findViewById(R.id.dialogImage);
        btnGET = findViewById(R.id.btnGet);
        btnPOST = findViewById(R.id.btnPost);
        btnPUT = findViewById(R.id.btnPut);
        btnDELETE = findViewById(R.id.btnDelete);
        usersList = findViewById(R.id.usersList);
        userModels = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnGET.setOnClickListener(this);
        btnPOST.setOnClickListener(this);
        btnPUT.setOnClickListener(this);
        btnDELETE.setOnClickListener(this);
        updateListView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnGet:
                JsonObjectRequest volleyGetRequest = new JsonObjectRequest(Request.Method.GET, REQ_RES_URL + "users?page=2", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataObject = response.getJSONArray("data");
                            for(int i = 0; i < dataObject.length(); i++){
                                JSONObject userObject = dataObject.getJSONObject(i);

                                UserModel tempUser = new UserModel(userObject.getInt("id"),
                                        userObject.getString("email"),
                                        userObject.getString("first_name"),
                                        userObject.getString("last_name"),
                                        userObject.getString("avatar"));

                                userModels.add(tempUser);
                                //Picasso.get().load(userObject.getString("avatar")).into(profileImage);
                                updateListView();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                VolleyReq.getInstance(this.getApplicationContext()).addToRequestQueue(volleyGetRequest);
                break;
            case R.id.btnPost:
                Toast.makeText(this, "POST", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnPut:
                Toast.makeText(this, "PUT", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnDelete:
                Toast.makeText(this, "DELETE", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     *  Create Actionbar menu
     */
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.contViewDetails) {
            showUserDetails(item);
        }
        return super.onContextItemSelected(item);
    }

    private void showUserDetails(MenuItem item) {

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
     * Update ListView
     */
    public void updateListView() {
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userModels);
        usersList.setAdapter(arrayAdapter);
    }

}