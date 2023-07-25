package com.socompany.felicitashop.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socompany.felicitashop.model.Product;

import java.lang.reflect.Type;
import java.util.HashMap;

public class AppPreferences {
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String USER_BASKET_KEY = "UserBasket";

    public static void saveUserBasket(Context context, HashMap<String, Product> userBasket) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userBasket);
        editor.putString(USER_BASKET_KEY, json);
        editor.apply();
    }

    public static HashMap<String, Product> loadUserBasket(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(USER_BASKET_KEY, "");
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Product>>() {}.getType();
        return gson.fromJson(json, type);
    }
}

