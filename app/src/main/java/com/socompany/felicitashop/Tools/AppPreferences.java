package com.socompany.felicitashop.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socompany.felicitashop.MainActivities.ProductDetailsActivity;
import com.socompany.felicitashop.Prevalent.UserBasket;
import com.socompany.felicitashop.model.Product;
import com.socompany.felicitashop.model.Products;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.HashMap;

public class AppPreferences {
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String USER_BASKET_KEY = "UserBasket";

    public static void saveUserBasket(Context context, HashMap<String, Products> userBasket) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userBasket);
        editor.putString(USER_BASKET_KEY, json);
        editor.apply();
    }

    public static HashMap<String, Products> loadUserBasket(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(USER_BASKET_KEY, "");
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Products>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void deleteUserBasket(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(USER_BASKET_KEY);
        editor.apply();
    }

    public static void loadUserBasketFromDatabase(Context context, String phone) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserBasket").child(phone);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        String productId = productSnapshot.getValue(String.class);

                        // Загружаем данные о продукте из узла "Products"
                        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference()
                                .child("Products").child(productId);

                        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Получаем данные продукта и преобразуем их в объект Product
                                Products product = dataSnapshot.getValue(Products.class);

                                // Перевірка чи правильно получили данні продукту
                                if(product != null && product.getPname().length() >= 1) {
                                    // Добавляем продукт в мапу userBasket
                                    UserBasket.userBasket.put(productId, product);
                                } else{
                                    // Якщо данні не вдалось получити з dataSnapshot.getValue(Products.class);
                                    Toast.makeText(context, "Помилка, при оновленні", Toast.LENGTH_SHORT).show();
                                }
                                // Зберігаємо данні кошику у вигляді мапи локально на пристрої
                                saveUserBasket(context, UserBasket.userBasket);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Обработка ошибок, если чтение данных не удалось
                            }
                        });
                    }
                } else { // Якщо в БД не знайдено товарів, тоді корзина пуста.
                    // Даємо знати користувачу, що кошик порожній.
                    Toast.makeText(context, "Схоже, ваша коризна пуста", Toast.LENGTH_SHORT).show();
                    // Оновлюємо данні, щоб запобігти подальших помилок.
                    deleteUserBasket(context);
                    UserBasket.setUserBasket(new HashMap<>());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}

