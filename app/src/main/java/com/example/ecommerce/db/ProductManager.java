package com.example.ecommerce.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.ecommerce.db.DatabaseHelper;
import com.example.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public ProductManager(Context c) {
        context = c;
    }

    public ProductManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long addProduct(String title, String description, byte[] image, double price) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("image", image);
        values.put("price", price);
        return database.insert("product", null, values);
    }

    public long updateProduct(int id, String title, String description, byte[] image, double price) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("image", image);
        values.put("price", price);

        // Specify the WHERE clause to update the row with the given ID
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(id)};

        // Perform the update operation
        return database.update("product", values, whereClause, whereArgs);
    }


    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();

        String[] columns = {
                "id",
                "title",
                "description",
                "image",
                "price"
        };

        Cursor cursor = database.query("product", columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
//                Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                double price = cursor.getDouble(cursor.getColumnIndex("price"));

                Product product = new Product(id, title, description, image, price);
                productList.add(product);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return productList;
    }

    public List<Product> getRecentProducts() {
        List<Product> productList = new ArrayList<>();

        String[] columns = {
                "id",
                "title",
                "description",
                "image",
                "price"
        };

        Cursor cursor = database.query("product", columns, null, null, null, null, "id DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));

                Product product = new Product(id, title, description, image, price);
                productList.add(product);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return productList;
    }


    public void delete(long _id) {
        database.delete("product", "id" + "=" + _id, null);
    }

    public Product getProductById(int productId) {
        Product product = null;

        String[] columns = {
                "id",
                "title",
                "description",
                "image",
                "price"
        };
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(productId) };

        Cursor cursor = database.query("product", columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));

            product = new Product(id, title, description, image, price);
            cursor.close();
        }

        return product;
    }


}
