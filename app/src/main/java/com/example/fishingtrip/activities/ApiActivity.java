package com.example.fishingtrip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import static com.example.fishingtrip.constants.QueryMessages.DELETE_REQ_URL;
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
        registerForContextMenu(usersList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGet:
                doGETReq();
                break;
            case R.id.btnPost:
                doPOSTReq();
                break;
            case R.id.btnPut:
                doPUTReq();
                break;
            case R.id.btnDelete:
                doDELETEReq();
                break;
        }
    }

    private void doGETReq(){
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
    }

    private void doPOSTReq() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("id", "4321");
            postData.put("name", "Oscar");
            postData.put("email", "oscar@mail.com");

            JsonObjectRequest volleyPostRequest = new JsonObjectRequest(Request.Method.POST, REQ_RES_URL + "users", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    txtRequestResult.setText(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            VolleyReq.getInstance(this.getApplicationContext()).addToRequestQueue(volleyPostRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void doPUTReq() {
        JSONObject putData = new JSONObject();
        try {

            putData.put("name", "Oscar");
            putData.put("job", "Full Stack Developer");

            JsonObjectRequest volleyPutRequest = new JsonObjectRequest(Request.Method.PUT, REQ_RES_URL + "users", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    txtRequestResult.setText(response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            VolleyReq.getInstance(this.getApplicationContext()).addToRequestQueue(volleyPutRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void doDELETEReq() {
        JsonObjectRequest volleyDeleteRequest = new JsonObjectRequest(Request.Method.DELETE, DELETE_REQ_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                txtRequestResult.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                txtRequestResult.setText(error.toString());
            }
        });
        VolleyReq.getInstance(this.getApplicationContext()).addToRequestQueue(volleyDeleteRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        actionBar = getSupportActionBar();
        assert actionBar != null;
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

    /**
     * Create userDetails dialog and show profile picture and user details.
     * @param item - use item to select which user to reveal.
     */
    private void showUserDetails(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.user_dialog, null);
        dialogBuilder.setView(dialogView);

        txtId = dialogView.findViewById(R.id.dialogTxtId);
        txtName = dialogView.findViewById(R.id.dialogTxtFullname);
        txtEmail = dialogView.findViewById(R.id.dialogTxtEmail);
        profileImage = dialogView.findViewById(R.id.dialogImage);

        Picasso.get().load(userModels.get(info.position).getAvatar()).into(profileImage);

        txtId.setText(String.format("ID: %d", userModels.get(info.position).getId()));
        txtName.setText(String.format("Name: %s %s", userModels.get(info.position).getFirstName(), userModels.get(info.position).getLastName()));
        txtEmail.setText(String.format("Email: %s", userModels.get(info.position).getEmail()));

        final AlertDialog updateDialog = dialogBuilder.create();
        updateDialog.show();
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
        arrayAdapter = new ArrayAdapter(this, R.layout.list_view_layout, R.id.txtListViewColor, userModels);
        usersList.setAdapter(arrayAdapter);
    }
}