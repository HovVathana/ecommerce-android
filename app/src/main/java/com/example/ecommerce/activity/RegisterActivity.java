package com.example.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.db.DatabaseHelper;
import com.example.ecommerce.db.DatabaseManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        dbManager = new DatabaseManager(this);

        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this::onRegisterClicked);

    }

    public void onRegisterClicked(View view){

        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        long result = dbManager.insert(username, email, password);

        // Check if insertion was successful
        if (result != -1) {
            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dbManager.close();
    }

}