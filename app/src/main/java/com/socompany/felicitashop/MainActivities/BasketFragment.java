package com.socompany.felicitashop.MainActivities;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.socompany.felicitashop.Adapters.BasketAdapter;
import com.socompany.felicitashop.Prevalent.Prevalent;
import com.socompany.felicitashop.Prevalent.UserBasket;
import com.socompany.felicitashop.R;
import com.socompany.felicitashop.Tools.AppPreferences;
import com.socompany.felicitashop.model.Product;

import java.util.HashMap;

import com.socompany.felicitashop.model.Products;
import io.paperdb.Paper;

public class BasketFragment extends Fragment {
    private BasketAdapter basketAdapter;
    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private HashMap<String, Products> userBasket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);

        Paper.init(getContext());

        initializeBasket(view);


        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                AppPreferences.loadUserBasketFromDatabase(getContext(), Paper.book().read(Prevalent.userPhoneKey));

                Toast.makeText(getActivity(), "Данні оновлені, перезагрузіть сторінку", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void initializeBasket(View view) {
        if(UserBasket.getUserBasket().isEmpty() && UserBasket.getUserBasket().size() >= 1) {
            // Если данных нету, вывожу сообщение о том что их нету.
            Toast.makeText(getActivity(), "Basket is empty", Toast.LENGTH_SHORT).show();
            return; // Выходим из метода, чтобы не продолжать работу с пустым userBasket
        } else {
            userBasket = UserBasket.userBasket;
        }

        recyclerView = view.findViewById(R.id.basket_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Проверяем, что productsList не пустой, чтобы избежать NullPointerException
        if (basketAdapter == null) {
            basketAdapter = new BasketAdapter(getContext(), userBasket);
            recyclerView.setAdapter(basketAdapter);
        } else {
            // Если адаптер уже был создан, просто обновляем данные в нем
            basketAdapter.setProductsList(userBasket);
            basketAdapter.notifyDataSetChanged();
        }
    }
}