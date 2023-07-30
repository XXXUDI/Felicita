package com.socompany.felicitashop.Prevalent;

import com.socompany.felicitashop.model.Product;
import com.socompany.felicitashop.model.Products;

import java.util.HashMap;

public class UserBasket {
    public static HashMap<String, Products> userBasket = new HashMap<>();

    public static HashMap<String, Products> getUserBasket() {
        return userBasket;
    }

    public static void setUserBasket(HashMap<String, Products> userBasket) {
        UserBasket.userBasket = userBasket;
    }

}
