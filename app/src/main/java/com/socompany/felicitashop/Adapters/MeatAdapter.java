package com.socompany.felicitashop.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.socompany.felicitashop.R;
import com.socompany.felicitashop.ViewHolders.ProductViewHolder;
import com.socompany.felicitashop.model.Products;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MeatAdapter extends FirebaseRecyclerAdapter<Products, ProductViewHolder> {

    private View.OnClickListener onItemClickListener;
    private int RecursionCounter;

    public MeatAdapter(@NonNull FirebaseRecyclerOptions<Products> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
        holder.txtProductName.setText(model.getPname());
        holder.txtProductPrice.setText(model.getPrice() + " ₴");
        loadProductImage(model.getImage(), holder.imageView);


        // Тег для додаткової інформації про продукт. (Використовується в HomeFragment -> SetupProductListeners();).
        holder.itemView.setTag(model.getPid());

        holder.itemView.setOnClickListener(onItemClickListener);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ProductViewHolder(view);
    }
    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    private void loadProductImage(String imageUrl, final ImageView imageView) {
        Picasso.get().load(imageUrl).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                if(RecursionCounter != 10) {
                    RecursionCounter++;
                    loadProductImage(imageUrl, imageView);
                }
            }
        });
    }
}
