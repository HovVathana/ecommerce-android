package com.example.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.CONSTANT;
import com.example.ecommerce.R;
import com.example.ecommerce.adapter.ProductAdapter;
import com.example.ecommerce.adapter.RecentAdapter;
import com.example.ecommerce.db.CartManager;
import com.example.ecommerce.db.ProductManager;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecentAdapter adapter;
    private RecyclerView recyclerView;

    ImageView btnProfile;
    long cartId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetchUserData();




        recyclerView = findViewById(R.id.recentView);

        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });


        fetchProducts();
        SharedPreferences settings = getSharedPreferences(CONSTANT.LOGIN_PREF, 0);
        long id = settings.getLong("id_key", -1);

        int user_id = (int) id;

        findUserCart(user_id);

        BottomAppBar bottomAppBar = findViewById(R.id.appBar);
        setSupportActionBar(bottomAppBar);

        // Set up click listeners for each button
        findViewById(R.id.cartMenu).setOnClickListener(v -> {
            // Handle Explorer button click
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });


    }

    private void fetchUserData() {
        SharedPreferences settings = getSharedPreferences(CONSTANT.LOGIN_PREF, 0);

        String username = settings.getString("username_key", "");

        setContentView(R.layout.activity_main);

        TextView usernameTxt = findViewById(R.id.usernameTxt);
        usernameTxt.setText(username);
    }

    private void findUserCart(int userId) {

        CartManager cartManager = new CartManager(this);
        cartManager.open();

        cartId = cartManager.findCart(userId);

        if (cartId == -1) {
            createUserCart(userId);
        }

        SharedPreferences cartPref = getSharedPreferences(CONSTANT.CART_PREF, 0);
        SharedPreferences.Editor editor = cartPref.edit();
        editor.putLong("cart_id", cartId);
        editor.apply();

    }

    private void createUserCart(int userId) {
        CartManager cartManager = new CartManager(this);
        cartManager.open();

        cartId = cartManager.createCart(userId);

        if (cartId == -1) {
            Toast.makeText(getApplicationContext(), "Error creating cart!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchProducts() {
        ProductManager databaseManager = new ProductManager(this);
        databaseManager.open();
        List<Product> productList = databaseManager.getRecentProducts();

        adapter = new RecentAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }


}