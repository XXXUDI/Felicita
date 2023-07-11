package com.socompany.felicitashop.MainActivities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.socompany.felicitashop.Adapters.CheeseAdapter;
import com.socompany.felicitashop.Adapters.ChemistryAdapter;
import com.socompany.felicitashop.Adapters.CoffeeAdapter;
import com.socompany.felicitashop.Adapters.MeatAdapter;
import com.socompany.felicitashop.Adapters.PastaAdapter;
import com.socompany.felicitashop.Adapters.RecommendationsAdapter;
import com.socompany.felicitashop.Adapters.SaucesAdapter;
import com.socompany.felicitashop.Adapters.SweetsAdapter;
import com.socompany.felicitashop.Prevalent.Prevalent;
import com.socompany.felicitashop.R;
import com.socompany.felicitashop.ViewHolders.ProductViewHolder;
import com.socompany.felicitashop.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.paperdb.Paper;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewRecommendation, recyclerViewSweets, recyclerViewCoffee, recyclerViewCheese, recyclerViewMeat,
            recyclerViewChemistry, recyclerViewSauces, recyclerViewPasta;

    private DatabaseReference productsRef;

    private RecommendationsAdapter recommendationsAdapter;
    private SweetsAdapter sweetsAdapter;
    private ChemistryAdapter cheAdapter;
    private CoffeeAdapter coffeeAdapter;
    private PastaAdapter pastaAdapter;
    private CheeseAdapter cheeseAdapter;
    private MeatAdapter meatAdapter;
    private SaucesAdapter saucesAdapter;

    // This is editText where customs can search the products by typing the name of product
    private EditText searchPanel;
    private TextView recommendationsText, recommendationsLoremIpsum;

    private  RecyclerView.LayoutManager layoutManagerRec, layoutManagerSweet, layoutManagerCoffee, layoutManagerCheese, layoutManagerMeat,
    layoutManagerChemistry, layoutManagerSauces, layoutManagerPasta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initialize(view);

        Paper.init(getContext());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        //adapter to sort products to their category
        FirebaseRecyclerOptions<Products> recommendationOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo("Рекомендації"), Products.class).build();
        recommendationsAdapter = new RecommendationsAdapter(recommendationOptions);
        recyclerViewRecommendation.setAdapter(recommendationsAdapter);
        recommendationsAdapter.startListening();

        FirebaseRecyclerOptions<Products> sweetsOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo("Солодощі"), Products.class).build();
        sweetsAdapter = new SweetsAdapter(sweetsOptions);
        recyclerViewSweets.setAdapter(sweetsAdapter);
        sweetsAdapter.startListening();

        FirebaseRecyclerOptions<Products> chemistryOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo("Хімія"), Products.class).build();
        cheAdapter = new ChemistryAdapter(chemistryOptions);
        recyclerViewChemistry.setAdapter(cheAdapter);
        cheAdapter.startListening();

        FirebaseRecyclerOptions<Products> coffeeOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo("Кава"), Products.class).build();
        coffeeAdapter = new CoffeeAdapter(coffeeOptions);
        recyclerViewCoffee.setAdapter(coffeeAdapter);
        coffeeAdapter.startListening();

        FirebaseRecyclerOptions<Products> pastaOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo("Паста"), Products.class).build();
        pastaAdapter = new PastaAdapter(pastaOptions);
        recyclerViewPasta.setAdapter(pastaAdapter);
        pastaAdapter.startListening();

        FirebaseRecyclerOptions<Products> cheeseOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo("Сири"), Products.class).build();
        cheeseAdapter = new CheeseAdapter(cheeseOptions);
        recyclerViewCheese.setAdapter(cheeseAdapter);
        cheeseAdapter.startListening();

        FirebaseRecyclerOptions<Products> meatOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo("М'ясо"), Products.class).build();
        meatAdapter = new MeatAdapter(meatOptions);
        recyclerViewMeat.setAdapter(meatAdapter);
        meatAdapter.startListening();

        FirebaseRecyclerOptions<Products> saucesOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef.orderByChild("category").equalTo("Соуси"), Products.class).build();
        saucesAdapter = new SaucesAdapter(saucesOptions);
        recyclerViewSauces.setAdapter(saucesAdapter);
        saucesAdapter.startListening();

        // Цей метод ініціалізує прослуховування на натискання на продукт (onClickListener)
        setupProductListeners();

        searchPanelWathcer();

    }

    private void searchPanelWathcer() {
        searchPanel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Получаем текст из EditText
                String searchText = s.toString();

                if(searchText.trim().length() != 0) {
                    recommendationsText.setText("Результати пошуку");
                    recommendationsLoremIpsum.setText("Результат пошуку по запросу: " + searchText);

                    //TODO можна добавити перевірку з нижнім регістром,
                    // і поміняти систему пошуку
                    String searchTextLower = searchText.toLowerCase();


                    hideAllUnwantedViews();

                    Query query = productsRef.orderByChild("pname").startAt(searchText).endAt(searchText + "\uf8ff");

                    // Создаем новый объект FirebaseRecyclerOptions с обновленным запросом
                    FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(query, Products.class)
                            .build();

                    // Обновляем адаптер с новыми данными
                    recommendationsAdapter.updateOptions(options);

                    // Уведомляем адаптер об изменениях
                    recommendationsAdapter.notifyDataSetChanged();
                } else {
                    FirebaseRecyclerOptions<Products> recommendationOptions = new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(productsRef.orderByChild("category").equalTo("Рекомендації"), Products.class).build();
                    recommendationsAdapter.updateOptions(recommendationOptions);
                    recommendationsAdapter.notifyDataSetChanged();

                    recommendationsText.setText("Рекомендації");
                    recommendationsLoremIpsum.setText("Lorem Ipsum");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void hideAllUnwantedViews() {

    }


    private void setupProductListeners() {
        View.OnClickListener productClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String productId = (String) view.getTag();

                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("pid", productId);
                startActivity(intent);
            }
        };

        recommendationsAdapter.setOnItemClickListener(productClickListener);
        sweetsAdapter.setOnItemClickListener(productClickListener);
        cheAdapter.setOnItemClickListener(productClickListener);
        coffeeAdapter.setOnItemClickListener(productClickListener);
        pastaAdapter.setOnItemClickListener(productClickListener);
        cheeseAdapter.setOnItemClickListener(productClickListener);
        meatAdapter.setOnItemClickListener(productClickListener);
        saucesAdapter.setOnItemClickListener(productClickListener);
    }

    private void initialize(View view) {
        recommendationsText = view.findViewById(R.id.home_recommendations);
        recommendationsLoremIpsum = view.findViewById(R.id.home_recommendations_loremIpsum);

        searchPanel = view.findViewById(R.id.home_search_panel);

        layoutManagerRec = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecommendation = view.findViewById(R.id.recycler_view_recommendations);
        recyclerViewRecommendation.setHasFixedSize(true);
        recyclerViewRecommendation.setLayoutManager(layoutManagerRec);

        layoutManagerSweet = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSweets = view.findViewById(R.id.recycler_view_sweets);
        recyclerViewSweets.setHasFixedSize(true);
        recyclerViewSweets.setLayoutManager(layoutManagerSweet);

        layoutManagerChemistry = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewChemistry = view.findViewById(R.id.recycler_view_chemistry);
        recyclerViewChemistry.setLayoutManager(layoutManagerChemistry);
        recyclerViewChemistry.setHasFixedSize(true);

        layoutManagerCoffee = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCoffee = view.findViewById(R.id.recycler_view_coffee);
        recyclerViewCoffee.setHasFixedSize(true);
        recyclerViewCoffee.setLayoutManager(layoutManagerCoffee);

        layoutManagerPasta = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPasta = view.findViewById(R.id.recycler_view_pasta);
        recyclerViewPasta.setHasFixedSize(true);
        recyclerViewPasta.setLayoutManager(layoutManagerPasta);

        layoutManagerCheese = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCheese = view.findViewById(R.id.recycler_view_cheese);
        recyclerViewCheese.setHasFixedSize(true);
        recyclerViewCheese.setLayoutManager(layoutManagerCheese);

        layoutManagerMeat = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMeat = view.findViewById(R.id.recycler_view_meat);
        recyclerViewMeat.setHasFixedSize(true);
        recyclerViewMeat.setLayoutManager(layoutManagerMeat);

        layoutManagerSauces = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSauces = view.findViewById(R.id.recycler_view_sauces);
        recyclerViewSauces.setHasFixedSize(true);
        recyclerViewSauces.setLayoutManager(layoutManagerSauces);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
    }
}