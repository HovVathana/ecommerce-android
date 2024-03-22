package com.example.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.CONSTANT;
import com.example.ecommerce.R;
import com.example.ecommerce.db.CartManager;
import com.example.ecommerce.db.ProductManager;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Product;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    Product product;
    ImageView productImg;
    TextView titleTxt, descriptionTxt, priceTxt;
    Button btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImg = findViewById(R.id.productImg);
        titleTxt = findViewById(R.id.titleTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        priceTxt = findViewById(R.id.priceTxt);
        btnCart = findViewById(R.id.btnCart);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product = extras.getParcelable("product");
            productImg.setImageBitmap(product.getImage());
            titleTxt.setText(product.getTitle());
            descriptionTxt.setText(product.getDescription());
            priceTxt.setText("$" + product.getPrice());
        }

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences settings = getSharedPreferences(CONSTANT.CART_PREF, 0);

                int cartId = (int) settings.getLong("cart_id", 1);

                CartManager databaseManager = new CartManager(getApplicationContext());
                databaseManager.open();
                databaseManager.addCartItem(cartId, product.getId());

                Toast.makeText(getApplicationContext(), "Added to cart!", Toast.LENGTH_SHORT).show();

                databaseManager.close();

            }
        });


    }
}