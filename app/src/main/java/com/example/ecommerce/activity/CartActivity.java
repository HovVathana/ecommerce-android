package com.example.ecommerce.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.CONSTANT;
import com.example.ecommerce.R;
import com.example.ecommerce.adapter.CartItemAdapter;
import com.example.ecommerce.adapter.RecentAdapter;
import com.example.ecommerce.db.CartManager;
import com.example.ecommerce.db.ProductManager;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnQuantityChangedListener{

    private CartItemAdapter adapter;
    private RecyclerView recyclerView;
    private TextView totalPriceTxt, deliveryPriceTxt, taxPriceTxt, allTotalPriceTxt;
    double deliveryPrice = 2;
    double taxPercentage = 10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartItemView);
        totalPriceTxt = findViewById(R.id.totalPriceTxt);
        deliveryPriceTxt = findViewById(R.id.deliveryPriceTxt);
        taxPriceTxt = findViewById(R.id.taxPriceTxt);
        allTotalPriceTxt = findViewById(R.id.allTotalPriceTxt);

        deliveryPriceTxt.setText("$" + CONSTANT.decimalFormat.format(deliveryPrice));

        fetchCartItems();

    }

    @Override
    public void onQuantityChanged(List<CartItem> cartItemList) {
        setPrice(cartItemList);
    }

    private void fetchCartItems() {
        SharedPreferences settings = getSharedPreferences(CONSTANT.CART_PREF, 0);

        int cartId = (int) settings.getLong("cart_id", 1);

        CartManager databaseManager = new CartManager(this);
        databaseManager.open();
        List<CartItem> cartItemList = databaseManager.getCartItems(cartId);

        adapter = new CartItemAdapter(this, cartItemList, this::onQuantityChanged);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        setPrice(cartItemList);
    }

    private void setPrice(List<CartItem> cartItemList) {
        double subtotalPrice = 0;

        for (CartItem cartItem : cartItemList) {
            ProductManager productManager = new ProductManager(this);
            productManager.open();
            Product product = productManager.getProductById(cartItem.getProduct_id());
            subtotalPrice += product.getPrice() * cartItem.getQty();
        }

        double taxPrice = subtotalPrice * (taxPercentage / 100);
        double totalPrice = subtotalPrice + deliveryPrice + taxPrice;

        totalPriceTxt.setText("$" + CONSTANT.decimalFormat.format(subtotalPrice));
        taxPriceTxt.setText("$" + CONSTANT.decimalFormat.format(taxPrice));
        allTotalPriceTxt.setText("$" + CONSTANT.decimalFormat.format(totalPrice));
    }

}