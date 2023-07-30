package com.socompany.felicitashop.MainActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socompany.felicitashop.MainActivities.Admin.AddProductActivity;
import com.socompany.felicitashop.MainActivities.Admin.AdminMainActivity;
import com.socompany.felicitashop.Prevalent.Prevalent;
import com.socompany.felicitashop.Prevalent.UserBasket;
import com.socompany.felicitashop.R;
import com.socompany.felicitashop.Tools.AppPreferences;
import com.socompany.felicitashop.model.Product;
import com.socompany.felicitashop.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class ProductDetailsActivity extends AppCompatActivity {

    private String productId, userPhone;
    private ImageView productImage;
    private TextView productName, productDescription, productPrice;
    private FrameLayout backButton;

    private Button addToBasketButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        backButton= findViewById(R.id.details_backButton);

        Paper.init(this);

        productId = getIntent().getStringExtra("pid");
        userPhone = getIntent().getStringExtra("userPhone");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if(productId != null || !(productId.equals(""))) {
            initialize();

            displayProductInfo();
        }

        addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToBasket(productId, userPhone);
            }
        });

    }

    private void addProductToBasket(String productId, String userPhone) {
        if (userPhone != null) {
            DatabaseReference userBasketRef = FirebaseDatabase.getInstance().getReference()
                    .child("UserBasket").child(userPhone);

            // Для початку, вносимо id Продукта в БД Firebase.
            HashMap<String, Object> productIdMap = new HashMap<>();
            productIdMap.put(productId, productId);
            userBasketRef.updateChildren(productIdMap);

            userBasketRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Очищаем текущую мапу, чтобы избежать дублирования данных
                    if(UserBasket.userBasket != null) {
                        UserBasket.userBasket.clear();
                    } else if (UserBasket.getUserBasket().size() >= 16) {
                        Toast.makeText(ProductDetailsActivity.this, "У вашій коризні надто багато продуктів!", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // Якщо мапа пуста, створюємо нову
                        UserBasket.userBasket = new HashMap<>();
                    }


                    if(dataSnapshot.exists()) {
                        // Якщо данні існують в базі данних обновляємо мапу і додаємо новий продукт
                        loadNewUserBasket(dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Обработка ошибок, если чтение данных не удалось
                }
            });
        }
    }

    public void loadNewUserBasket(DataSnapshot dataSnapshot) {
        // Проходимся по всем идентификаторам продуктов в корзине пользователя
        for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
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
                        Toast.makeText(ProductDetailsActivity.this, "Помилка, повторіть пізніше.", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(ProductDetailsActivity.this, "Товар додано у корзину!", Toast.LENGTH_SHORT).show();

                    // Зберігаємо данні кошику у вигляді мапи локально на пристрої
                    AppPreferences.saveUserBasket(ProductDetailsActivity.this, UserBasket.userBasket);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Обработка ошибок, если чтение данных не удалось
                }
            });
        }
    }
    private void displayProductInfo() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    // Получаємо данні з БД
                    String image = snapshot.child("image").getValue().toString();
                    String name = snapshot.child("pname").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();

                    // Відображаємо данні на екрані
                    Picasso.get().load(image).into(productImage);
                    productName.setText(name);
                    productDescription.setText(description);
                    productPrice.setText(price + " грн");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialize() {
        // Ініціалізація змінних
        productImage = findViewById(R.id.details_image);
        productName = findViewById(R.id.details_pname);
        productDescription = findViewById(R.id.details_pdescription);
        productPrice = findViewById(R.id.details_pprice);
        addToBasketButton = findViewById(R.id.details_addButton);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}