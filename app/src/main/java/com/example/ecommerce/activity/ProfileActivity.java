package com.example.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ecommerce.CONSTANT;
import com.example.ecommerce.R;
import com.example.ecommerce.db.DatabaseManager;
import com.example.ecommerce.db.ProductManager;
import com.example.ecommerce.model.Product;

public class ProfileActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etOldPassword, etNewPassword;
    Button btnConfirm;
    LinearLayout btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnLogout = findViewById(R.id.btnLogout);
        btnConfirm = findViewById(R.id.btnConfirm);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);

        SharedPreferences settings = getSharedPreferences(CONSTANT.LOGIN_PREF, 0);

        long id = settings.getLong("id_key", -1);
        String username = settings.getString("username_key", "");
        String password = settings.getString("password_key", "");
        String email = settings.getString("email_key", "");
        String role = settings.getString("role_key", "");

        etUsername.setText(username);
        etEmail.setText(email);

        btnConfirm.setOnClickListener(view -> updateProfile(id, password, role));
        btnLogout.setOnClickListener(view -> logout());

    }

    private void updateProfile(long id, String password, String role) {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword)) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!oldPassword.equals(password)) {
            Toast.makeText(getApplicationContext(), "Old password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.open();
        databaseManager.update(id, username, email, newPassword);

        SharedPreferences settings = getSharedPreferences(CONSTANT.LOGIN_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("id_key", id);
        editor.putString("username_key", username);
        editor.putString("email_key", email);
        editor.putString("password_key", newPassword);
        editor.putString("role_key", role);
        editor.putString("logged", "logged");
        editor.apply();

        Toast.makeText(getApplicationContext(), "User information updated successfully", Toast.LENGTH_SHORT).show();

        if (role.equals("admin")) {
            startActivity(new Intent(ProfileActivity.this, AdminActivity.class));
        } else {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));

        }

    }

    private void logout() {
        SharedPreferences settings = getSharedPreferences(CONSTANT.LOGIN_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);

    }
}