package com.example.ecommerce.model;

public class Cart {

    private int id;
    private int user_id;

    public Cart(int id, int user_id) {
        this.id = id;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
