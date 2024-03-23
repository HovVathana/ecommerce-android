package com.example.ecommerce.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;


import com.example.ecommerce.CONSTANT;
import com.example.ecommerce.R;
import com.example.ecommerce.activity.admin.AdminActivity;
import com.example.ecommerce.db.DatabaseHelper;
import com.example.ecommerce.db.DatabaseManager;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private DatabaseHelper dbHelper;

    private DatabaseManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        dbHelper = new DatabaseHelper(this);
        dbManager = new DatabaseManager(this);

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this::onLoginClicked);

        SharedPreferences settings = getSharedPreferences(CONSTANT.LOGIN_PREF, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            if (settings.getString("role_key", "user").toString().equals("admin")) {
                Intent intent = new Intent(SignInActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

    }

    public void goToRegister(View view) {
        Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view){


        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Cursor cursor = dbManager.checkEmailPassword(email, password);

        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String userEmail = cursor.getString(cursor.getColumnIndex("email"));
            String userPassword = cursor.getString(cursor.getColumnIndex("password"));
            String userRole = cursor.getString(cursor.getColumnIndex("role"));

            SharedPreferences settings = getSharedPreferences(CONSTANT.LOGIN_PREF, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong("id_key", id);
            editor.putString("username_key", username);
            editor.putString("email_key", userEmail);
            editor.putString("password_key", userPassword);
            editor.putString("role_key", userRole);
            editor.putString("logged", "logged");
            editor.apply();

            if (userRole.equals("admin")) {
                Intent intent = new Intent(SignInActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }

            cursor.close();
        } else {
            // No matching row found
            Toast.makeText(getApplicationContext(), "Wrong authentication", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close database connection when the activity is destroyed
        dbManager.close();
    }
}