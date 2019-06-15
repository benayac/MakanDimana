package com.example.makandimana.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.makandimana.R;
import com.example.makandimana.model.MenuMakananModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class MenuMakananAdapter extends FirebaseRecyclerAdapter<MenuMakananModel, MenuMakananAdapter.menuMakananViewHolder> {

    /*
        MenuMakananAdapter merupakan class adapter yang menghubungkan antara RecyclerView pada aplikasi
        yang berisi list menu makanan tiap restoran, dengan data-data yang ada pada database.
        Class ini berisi berbagai macam method yang menerapkan konsep polimorfisme.
     */

    public MenuMakananAdapter(@NonNull FirebaseRecyclerOptions<MenuMakananModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull menuMakananViewHolder holder, int position, @NonNull MenuMakananModel model) {
        holder.foodPrice.setText(String.valueOf(model.getFoodPrice()));
        holder.foodName.setText(String.valueOf(model.getFoodName()));

        String imgUrl = String.valueOf(model.getImgUrl());

        Picasso.get().load(imgUrl).resize(150, 150).centerInside().into(holder.foodImg);
    }

    @NonNull
    @Override
    public menuMakananViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_makanan_card,
                viewGroup, false);
        return new menuMakananViewHolder(v);
    }

    public static class menuMakananViewHolder extends RecyclerView.ViewHolder {

        private TextView foodName, foodPrice;
        private ImageView foodImg;

        public menuMakananViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.tvFoodName);
            foodPrice = itemView.findViewById(R.id.tvFoodPrice);
            foodImg = itemView.findViewById(R.id.imgFood);
        }
    }
}
