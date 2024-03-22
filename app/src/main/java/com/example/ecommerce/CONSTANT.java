package com.example.ecommerce;

import java.text.DecimalFormat;

public final class CONSTANT {

    private CONSTANT() {
        // No need to instantiate the class, we can hide its constructor
    }

    public static final String LOGIN_PREF = "LoginPrefs";
    public static final String CART_PREF = "CartPrefs";

    public static DecimalFormat decimalFormat = new DecimalFormat("#0.00");

}