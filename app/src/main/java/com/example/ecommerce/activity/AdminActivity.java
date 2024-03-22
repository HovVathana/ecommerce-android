package com.example.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ecommerce.CONSTANT;
import com.example.ecommerce.R;

public class AdminActivity extends AppCompatActivity {

    LinearLayout productBox;
    LinearLayout userBox;
    ImageView btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        SharedPreferences settings = getSharedPreferences(CONSTANT.LOGIN_PREF, 0);

        String username = settings.getString("username_key", "");


        TextView usernameTxt = findViewById(R.id.usernameTxt);
        usernameTxt.setText(username);

        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, ProfileActivity.class));
            }
        });


        productBox = findViewById(R.id.productBox);
        productBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminProductActivity.class);
                startActivity(intent);

            }
        });

        userBox = findViewById(R.id.userBox);
        userBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminUserActivity.class);
                startActivity(intent);

            }
        });


    }

}