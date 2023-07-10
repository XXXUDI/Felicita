package com.socompany.felicitashop.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.socompany.felicitashop.R;
import com.socompany.felicitashop.ViewHolders.ProductViewHolder;
import com.socompany.felicitashop.model.Products;
import com.squareup.picasso.Picasso;

public class RecommendationsAdapter extends FirebaseRecyclerAdapter<Products, ProductViewHolder> {
    private View.OnClickListener onItemClickListener;
    public RecommendationsAdapter(@NonNull FirebaseRecyclerOptions<Products> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
        // Установите данные для ViewHolder
        holder.txtProductName.setText(model.getPname());
        holder.txtProductPrice.setText(model.getPrice() + " грн");
        Picasso.get().load(model.getImage()).into(holder.imageView);

        // Тег для додаткової інформації про продукт. (Використовується в HomeFragment -> SetupProductListeners();).
        holder.itemView.setTag(model.getPid());

        holder.itemView.setOnClickListener(onItemClickListener);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создайте ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(view);
    }
    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
