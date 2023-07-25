package com.socompany.felicitashop.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socompany.felicitashop.Interfaces.ItemClickListener;
import com.socompany.felicitashop.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasketItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public CircleImageView productImage;
    public ItemClickListener listener;

    public BasketItemsViewHolder(@NonNull View itemView) {
        super(itemView);

        productImage = itemView.findViewById(R.id.basket_product_image);
        txtProductName = itemView.findViewById(R.id.basket_product_name);
        txtProductPrice = itemView.findViewById(R.id.basket_product_price);
        txtProductDescription = itemView.findViewById(R.id.basket_product_description);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
