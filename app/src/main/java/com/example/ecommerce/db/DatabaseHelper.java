package com.example.ecommerce.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "email TEXT," +
                "password TEXT," +
                "role TEXT)";
        db.execSQL(sql);

        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE product (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT," +
                "image BLOB," +
                "price REAL)";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);

        String SQL_CREATE_CART_TABLE = "CREATE TABLE cart ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                " FOREIGN KEY (user_id) REFERENCES user(id)" +
                ")";

        db.execSQL(SQL_CREATE_CART_TABLE);

        String SQL_CREATE_CART_ITEM_TABLE = "CREATE TABLE cart_item ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cart_id INTEGER, " +
                "product_id INTEGER, " +
                "qty INTEGER, " +
                " FOREIGN KEY (cart_id) REFERENCES cart(id)" +
                " FOREIGN KEY (product_id) REFERENCES product(id)" +
                ")";

        db.execSQL(SQL_CREATE_CART_ITEM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS product");
        db.execSQL("DROP TABLE IF EXISTS cart");
        db.execSQL("DROP TABLE IF EXISTS cart_item");
        onCreate(db);
    }
}

