package com.socompany.felicitashop.Adapters;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Target;

public class ChemistryAdapter extends FirebaseRecyclerAdapter<Products, ProductViewHolder> {
    private View.OnClickListener onItemClickListener;

    public ChemistryAdapter(@NonNull FirebaseRecyclerOptions<Products> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
        holder.txtProductName.setText(model.getPname());
        holder.txtProductPrice.setText(model.getPrice() + " ₴");
        Picasso.get().load(model.getImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Drawable image1 = holder.imageView.getDrawable();
                Picasso.get().load(model.getImage()).into(holder.imageView);
                Drawable image2 = holder.imageView.getDrawable();
                if(image1 == image2) {
                    holder.imageView.setImageResource(R.drawable.baseline_broken_image_24);
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

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
}
