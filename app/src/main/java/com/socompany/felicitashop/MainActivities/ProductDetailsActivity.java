package com.socompany.felicitashop.MainActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.socompany.felicitashop.R;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    private String productId;
    private ImageView productImage, backArrow;
    private TextView productName, productDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        backArrow = findViewById(R.id.pd_back);

        productId = getIntent().getStringExtra("pid");
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(productId != null || !(productId.equals(""))) {
            initialize();

            displayProductInfo();
        }
    }

    private void displayProductInfo() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String image = snapshot.child("image").getValue().toString();
                    String name = snapshot.child("pname").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();

                    Picasso.get().load(image).into(productImage);
                    productName.setText(name);
                    productDescription.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialize() {
        productImage = findViewById(R.id.pd_product_image);
        productName = findViewById(R.id.pd_product_name);
        productDescription = findViewById(R.id.pd_product_description);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}