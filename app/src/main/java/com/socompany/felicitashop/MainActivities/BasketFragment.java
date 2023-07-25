package com.socompany.felicitashop.MainActivities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socompany.felicitashop.Adapters.BasketAdapter;
import com.socompany.felicitashop.Prevalent.UserBasket;
import com.socompany.felicitashop.R;
import com.socompany.felicitashop.model.Product;

import java.util.HashMap;

import io.paperdb.Paper;

public class BasketFragment extends Fragment {
    private BasketAdapter basketAdapter;
    private RecyclerView recyclerView;

    private HashMap<String, Product> userBasket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);

        Paper.init(getContext());

        initialize(view);

        return view;
    }

    private void initialize(View view) {
        if (UserBasket.userBasket != null && !UserBasket.userBasket.isEmpty()) {
            userBasket = UserBasket.userBasket;
        } else {
            // Если данных нет или они некорректны, сделайте действие по умолчанию,
            // например, перенаправьте пользователя на другой экран или отобразите сообщение об отсутствии данных
            // Ниже пример перенаправления на EditProfileActivity
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
            return; // Выходим из метода, чтобы не продолжать работу с пустым userBasket
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