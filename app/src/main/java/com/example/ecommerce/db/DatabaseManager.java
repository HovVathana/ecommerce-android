package com.example.ecommerce.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ecommerce.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DatabaseManager(Context c) {
        context = c;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String username, String email, String password) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("email", email);
        contentValue.put("username", username);
        contentValue.put("password", password);
        contentValue.put("role", "user");
        return database.insert("user", null, contentValue);

    }

    public Cursor checkEmailPassword(String email, String password) {
        Cursor cursor = database.rawQuery("SELECT * FROM user WHERE email = ? AND password = ?",
                new String[]{email, password});
        return cursor;
    }

    public List<User> fetchAllUser() {
        List<User> userList = new ArrayList<>();
        String[] columns = new String[] { "id", "username", "email", "role" };
        Cursor cursor = database.query("user", columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String role = cursor.getString(cursor.getColumnIndex("role"));
                // Create a new User object without the password
                User user = new User(id, username, email, role);
                userList.add(user);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return userList;
    }


    public int update(long _id, String username, String email, String password) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("username", username);
        contentValue.put("email", email);
        contentValue.put("password", password);
        int i = database.update("user", contentValue, "id" + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete("user", "id" + "=" + _id, null);
    }

}
