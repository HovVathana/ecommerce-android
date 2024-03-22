package com.example.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.UserManager;

import com.example.ecommerce.R;
import com.example.ecommerce.adapter.ProductAdapter;
import com.example.ecommerce.adapter.UserAdapter;
import com.example.ecommerce.db.DatabaseManager;
import com.example.ecommerce.db.ProductManager;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;

import java.util.List;

public class AdminUserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        recyclerView = findViewById(R.id.userView);

        fetchProducts();
    }

    private void fetchProducts() {
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.open();
        List<User> userList = databaseManager.fetchAllUser();

        adapter = new UserAdapter(this, userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}