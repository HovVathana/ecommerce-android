package com.example.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.activity.AdminProductActivity;
import com.example.ecommerce.activity.AdminProductAddActivity;
import com.example.ecommerce.db.ProductManager;
import com.example.ecommerce.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_pup_list, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productTitleTextView.setText(product.getTitle());
        holder.productPriceTextView.setText("$ " + product.getPrice());
        holder.productImageView.setImageBitmap(product.getImage());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the position of the clicked item
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Remove the item from the list
                    productList.remove(adapterPosition);
                    ProductManager databaseManager = new ProductManager(context);
                    databaseManager.open();
                    databaseManager.delete(product.getId());
                    // Notify the adapter that an item has been removed
                    notifyItemRemoved(adapterPosition);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminProductAddActivity.class);
                intent.putExtra("product", product);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView, btnDelete;
        TextView productTitleTextView, productPriceTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImg);
            productTitleTextView = itemView.findViewById(R.id.titleTxt);
            productPriceTextView = itemView.findViewById(R.id.priceTxt);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
