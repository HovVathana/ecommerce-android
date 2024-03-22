package com.example.ecommerce.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ecommerce.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;


    public CartManager(Context c) {
        context = c;
    }

    public CartManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long createCart(int userId) {
        long cartId = -1; // Default value if cart creation fails

        ContentValues values = new ContentValues();
        values.put("user_id", userId);

        try {
            cartId = database.insertOrThrow("cart", null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartId;
    }


    public long findCart(int userId) {
        long cartId = -1; // Default value if cart is not found

        String[] columns = {"id"};
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = database.query("cart", columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cartId = cursor.getLong(cursor.getColumnIndex("id"));
            cursor.close();
        }

        return cartId;
    }

    public void deleteCart(int userId) {
        String selectionCart = "user_id = ?";
        String[] selectionArgsCart = {String.valueOf(userId)};

        // Delete cart item first
        database.delete("cart_item", "cart_id IN (SELECT id FROM cart WHERE user_id = ?)", selectionArgsCart);

        // Then delete the cart
        database.delete("cart", selectionCart, selectionArgsCart);
    }

    public long addCartItem(int cartId, int productId) {
        // Check if the product already exists in the cart
        String[] columns = { "id", "qty" };
        String selection = "cart_id = ? AND product_id = ?";
        String[] selectionArgs = { String.valueOf(cartId), String.valueOf(productId) };
        Cursor cursor = database.query("cart_item", columns, selection, selectionArgs, null, null, null);

        long rowId;
        if (cursor != null && cursor.moveToFirst()) {
            // Product already exists, update the quantity
            int existingQuantity = cursor.getInt(cursor.getColumnIndex("qty"));
            int newQuantity = existingQuantity + 1;

            ContentValues updateValues = new ContentValues();
            updateValues.put("qty", newQuantity);

            rowId = database.update("cart_item", updateValues, selection, selectionArgs);
        } else {
            // Product does not exist, insert a new cart item
            ContentValues values = new ContentValues();
            values.put("cart_id", cartId);
            values.put("product_id", productId);
            values.put("qty", 1);
            rowId = database.insert("cart_item", null, values);
        }

        if (cursor != null) {
            cursor.close();
        }

        return rowId;
    }

    public int updateCartItemQuantity(int cartId, int productId, int newQuantity) {
        // Create a content values object to store the new quantity
        ContentValues values = new ContentValues();
        values.put("qty", newQuantity);

        // Define the WHERE clause to identify the cart item to update
        String whereClause = "cart_id = ? AND product_id = ?";
        String[] whereArgs = {String.valueOf(cartId), String.valueOf(productId)};

        // Perform the update operation and return the number of rows affected
        return database.update("cart_item", values, whereClause, whereArgs);
    }


    public List<CartItem> getCartItems(int cartId) {
        List<CartItem> cartItems = new ArrayList<>();
        Cursor cursor = database.query("cart_item", null,
                "cart_id=?", new String[]{String.valueOf(cartId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int productId = cursor.getInt(cursor.getColumnIndex("product_id"));
                int quantity = cursor.getInt(cursor.getColumnIndex("qty"));
                // Create CartItem object and add to list
                cartItems.add(new CartItem(cartId, productId, quantity));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return cartItems;
    }

}