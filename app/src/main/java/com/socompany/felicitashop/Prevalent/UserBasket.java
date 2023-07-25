package com.socompany.felicitashop.Prevalent;

import com.socompany.felicitashop.model.Product;

import java.util.HashMap;

public class UserBasket {
    public static HashMap<String, Product> userBasket = new HashMap<>();

    public static HashMap<String, Product> getUserBasket() {
        return userBasket;
    }

    public static void setUserBasket(HashMap<String, Product> userBasket) {
        UserBasket.userBasket = userBasket;
    }

}
