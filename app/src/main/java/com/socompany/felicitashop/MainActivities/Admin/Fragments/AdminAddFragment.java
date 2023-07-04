package com.socompany.felicitashop.MainActivities.Admin.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socompany.felicitashop.MainActivities.Admin.AddProductActivity;
import com.socompany.felicitashop.R;

public class AdminAddFragment extends Fragment {

    private String category;
    private TextView categoryDisplay;
    private ImageView wine, cheese, pasta, drinks, seafood, meat, chemistry, sauces;
    private Button addButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add, container, false);

        initialize(view);

        setupListeners();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category != null) {
                    Intent addProductIntent = new Intent(getActivity(), AddProductActivity.class);
                    addProductIntent.putExtra("category", category);
                    startActivity(addProductIntent);
                } else {
                    Toast.makeText(getActivity(), "Виберіть категорію", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void setupListeners() {
        wine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Вина";
                displayCategory();
            }
        });
        cheese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Сири";
                displayCategory();
            }
        });
        pasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Паста";
                displayCategory();
            }
        });
        meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "М'ясо";
                displayCategory();
            }
        });
        seafood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Морепродукти";
                displayCategory();
            }
        });
        sauces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Соуси";
                displayCategory();
            }
        });
        chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Хімія";
                displayCategory();
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Напитки";
                displayCategory();
            }
        });
    }

    private void displayCategory() {
        if(category != null) {
            categoryDisplay.setText("Ви вибрали категорію: " + category);
        }
    }

    private void initialize(View view) {
        categoryDisplay = view.findViewById(R.id.add_category_text);
        wine = view.findViewById(R.id.add_wine);
        cheese = view.findViewById(R.id.add_cheese);
        pasta = view.findViewById(R.id.add_pasta);
        drinks = view.findViewById(R.id.add_drinks);
        seafood = view.findViewById(R.id.add_seafood);
        meat = view.findViewById(R.id.add_meat);
        chemistry = view.findViewById(R.id.add_chemistry);
        sauces = view.findViewById(R.id.add_sauces);
        addButton = view.findViewById(R.id.add_nextButton);
    }
}