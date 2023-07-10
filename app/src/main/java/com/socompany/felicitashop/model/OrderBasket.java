package com.socompany.felicitashop.model;

import java.util.ArrayList;

public class OrderBasket {

    public ArrayList<String> addedProducts = new ArrayList<>();

    public ArrayList<String> getListOfProducts() {
        return addedProducts;
    }
}
