package com.example.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ecommerce.R;
import com.example.ecommerce.adapter.ProductAdapter;
import com.example.ecommerce.db.ProductManager;
import com.example.ecommerce.model.Product;

import java.util.List;

public class AdminProductActivity extends AppCompatActivity {

    ImageView addProduct;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        addProduct = findViewById(R.id.addProduct);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminProductAddActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.productView);

        fetchProducts();

    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchProducts();
    }

    private void fetchProducts() {
        ProductManager databaseManager = new ProductManager(this);
        databaseManager.open();
        List<Product> productList = databaseManager.getAllProducts();

        adapter = new ProductAdapter(this, productList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }
}