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
import com.example.ecommerce.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_user_list, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.idTxt.setText(String.valueOf(user.getId()));
        holder.emailTxt.setText("Email: " + user.getEmail());
        holder.usernameTxt.setText("Username: " + user.getUsername());
        holder.roleTxt.setText("Role: " + user.getRole());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTxt, emailTxt, roleTxt, idTxt;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            idTxt = itemView.findViewById(R.id.idText);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            usernameTxt = itemView.findViewById(R.id.usernameTxt);
            roleTxt = itemView.findViewById(R.id.roleTxt);
        }
    }
}
