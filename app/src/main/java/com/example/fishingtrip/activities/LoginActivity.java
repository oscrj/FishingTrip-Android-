package com.example.fishingtrip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishingtrip.R;
import com.example.fishingtrip.databas.DBHelper;

import static com.example.fishingtrip.constants.UserSharedPref.SHARED_PREF_LOGIN;
import static com.example.fishingtrip.constants.UserSharedPref.USER_NAME_DATA;

public class LoginActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView userNameInput, passwordInput;
    private Button btnLogin;
    private String userLoginData;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameInput = findViewById(R.id.inputTextLoginUserName);
        passwordInput = findViewById(R.id.inputTextLoginPassword);
        btnLogin = findViewById(R.id.btnLoginSubmit);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Makes it only possible to submit if correct value is given
        btnLogin.setEnabled(false);
        userNameInput.addTextChangedListener(loginValidation);
        passwordInput.addTextChangedListener(loginValidation);

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

    private TextWatcher loginValidation = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            btnLogin.setEnabled(true);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //verifyUserExist();

                    // temporary login process....
                    if (userNameInput.getText().toString().trim().equals("oscar") && passwordInput.getText().toString().equals("password123")){

                        btnLogin.setEnabled(true);

                        btnLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                                userLoginData = userNameInput.getText().toString();
                                saveUserDATA();
                                startActivity(home);
                            }
                        });
                    }else
                        btnLogin.setEnabled(false);

                }
            });

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * Verify that user with username and password exist in database.
     */
    public void verifyUserExist(){

        if(dbHelper.checkUser(userNameInput.getText().toString().trim(), passwordInput.getText().toString().trim())){

            userLoginData = userNameInput.getText().toString();
            saveUserDATA();
            Intent home = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(home);

        }else{
            btnLogin.setEnabled(false);
            Toast.makeText(LoginActivity.this, "User not Found!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * SAVE User login session.
     */
    public void saveUserDATA(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_LOGIN, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME_DATA, userLoginData);

        editor.apply();
    }

    /**
     * load User data if user are logged in.
     */
    public void loadUserData(){

    }

    /**
     * clear data if user logout.
     */
    public void clearUserData(){

    }

}