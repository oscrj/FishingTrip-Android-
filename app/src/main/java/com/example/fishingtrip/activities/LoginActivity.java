package com.example.fishingtrip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fishingtrip.R;

public class LoginActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private TextView userName, password;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.inputTextLoginUserName);
        password = findViewById(R.id.inputTextLoginPassword);
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
        userName.addTextChangedListener(loginValidation);
        password.addTextChangedListener(loginValidation);

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

            if (userName.getText().toString().trim().equals("oscrj") && password.getText().toString().equals("password123")){
                btnLogin.setEnabled(true);

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                        home.putExtra("USERNAME_DATA", userName.getText().toString());
                        startActivity(home);
                    }
                });
            }else
                btnLogin.setEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}