package com.socompany.felicitashop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.socompany.felicitashop.Prevalent.UserBasket;
import com.socompany.felicitashop.R;
import com.socompany.felicitashop.ViewHolders.BasketItemsViewHolder;
import com.socompany.felicitashop.model.Product;
import com.socompany.felicitashop.model.Products;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Products> productsList;

    public BasketAdapter(Context context, HashMap<String, Products> userBasket) {
        this.context = context;
        // Конвертируем значения из мапы userBasket в список продуктов
        this.productsList = new ArrayList<>(UserBasket.getUserBasket().values());
    }

    public void setProductsList(HashMap<String, Products> productsList) {
        this.productsList = new ArrayList<>(productsList.values());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_basket_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = productsList.get(position);

        holder.productNameTextView.setText(product.getPname());
        holder.productDescriptionTextView.setText(product.getDescription());
        holder.productPriceTextView.setText(String.valueOf(product.getPrice()));

        Picasso.get().load(product.getImage()).into(holder.productImageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Обработка нажатия на продукт в корзине

            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView productDescriptionTextView;
        TextView productPriceTextView;
        CardView cardView;
        CircleImageView productImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.basket_product_name);
            productDescriptionTextView = itemView.findViewById(R.id.basket_product_description);
            productPriceTextView = itemView.findViewById(R.id.basket_product_price);
            cardView = itemView.findViewById(R.id.basket_cardview);
            productImageView = itemView.findViewById(R.id.basket_product_image);
        }
    }
}
