package com.example.ecommerce.model;

public class CartItem {

    private int cart_id;
    private int product_id;
    private int qty;

    public CartItem(int cart_id, int product_id, int qty) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.qty = qty;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
