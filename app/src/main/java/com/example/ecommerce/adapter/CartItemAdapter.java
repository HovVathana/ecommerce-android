package com.example.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.db.CartManager;
import com.example.ecommerce.db.ProductManager;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;

import java.util.List;



public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    private OnQuantityChangedListener listener;

    public CartItemAdapter(Context context, List<CartItem> cartItemList, OnQuantityChangedListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    public interface OnQuantityChangedListener {
        void onQuantityChanged(List<CartItem> cartItemList);
    }



    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cart_list, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        ProductManager databaseManager = new ProductManager(context);
        databaseManager.open();
        Product product = databaseManager.getProductById(cartItem.getProduct_id());
        holder.productTitleTextView.setText(product.getTitle());
        holder.productImageView.setImageBitmap(product.getImage());
        holder.productPriceTextView.setText("$" + product.getPrice());
        holder.productQtyTextView.setText(String.valueOf(cartItem.getQty()));
        holder.productTotalPriceTextView.setText("$" + (product.getPrice() * cartItem.getQty()));

        holder.addQtyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQty = cartItem.getQty();

                int newQty = currentQty + 1;
                cartItem.setQty(newQty);

                // Update the quantity in the database
                CartManager cartManager = new CartManager(context);
                cartManager.open();
                cartManager.updateCartItemQuantity(cartItem.getCart_id(), cartItem.getProduct_id(), newQty);

                if (listener != null) {
                    cartItemList.set(position, cartItem);

                    listener.onQuantityChanged(cartItemList);
                }

                // Update the quantity TextView
                holder.productQtyTextView.setText(String.valueOf(newQty));

                // Calculate and update the total price TextView
                holder.productTotalPriceTextView.setText("$" + (product.getPrice() * newQty));
            }
        });

        holder.minusQtyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQty = cartItem.getQty();

                if (currentQty == 1) {
                    // If quantity is 1, remove the cart item from the list
                    cartItemList.remove(position);

                    // Notify the adapter that the item has been removed
                    notifyItemRemoved(position);

                    // Update the database to remove the cart item
                    CartManager cartManager = new CartManager(context);
                    cartManager.open();
                    cartManager.deleteCartItem(cartItem.getCart_id(), cartItem.getProduct_id());

                    // Notify the listener about the changes in the cart items list
                    if (listener != null) {
                        listener.onQuantityChanged(cartItemList);
                    }

                    return; // Exit the method since the item has been removed
                }

                // Decrease the quantity by 1
                int newQty = currentQty - 1;
                cartItem.setQty(newQty);

                // Update the quantity in the database
                CartManager cartManager = new CartManager(context);
                cartManager.open();
                cartManager.updateCartItemQuantity(cartItem.getCart_id(), cartItem.getProduct_id(), newQty);

                // Notify the listener about the changes in the cart items list
                if (listener != null) {
                    cartItemList.set(position, cartItem);
                    listener.onQuantityChanged(cartItemList);
                }

                // Update the quantity TextView
                holder.productQtyTextView.setText(String.valueOf(newQty));

                // Calculate and update the total price TextView
                holder.productTotalPriceTextView.setText("$" + (product.getPrice() * newQty));
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView, addQtyImageView, minusQtyImageView;
        TextView productTitleTextView, productPriceTextView, productTotalPriceTextView, productQtyTextView;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImg);
            productTitleTextView = itemView.findViewById(R.id.titleTxt);
            productPriceTextView = itemView.findViewById(R.id.priceTxt);
            productTotalPriceTextView = itemView.findViewById(R.id.totalPriceTxt);
            productQtyTextView = itemView.findViewById(R.id.qtyTxt);
            addQtyImageView = itemView.findViewById(R.id.addQtyBtn);
            minusQtyImageView = itemView.findViewById(R.id.minusQtyBtn);

        }
    }
}
