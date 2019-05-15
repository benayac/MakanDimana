package com.example.makandimana.adapter;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.makandimana.R;
import com.example.makandimana.model.menuMakananModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class menuMakananAdapter extends FirebaseRecyclerAdapter<menuMakananModel, menuMakananAdapter.menuMakananViewHolder> {

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("menuMakanan");

    public menuMakananAdapter(@NonNull FirebaseRecyclerOptions<menuMakananModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull menuMakananViewHolder holder, int position, @NonNull menuMakananModel model) {
        holder.foodPrice.setText(String.valueOf(model.getFoodPrice()));
        holder.foodName.setText(String.valueOf(model.getFoodName()));
        //holder.openingHours.setText("08.00 - 12.00");
        //holder.namaResto.setText("Burjo Mekar Mulya");
        //holder.tipeResto.setText("Warmindo");

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

    public static class menuMakananViewHolder extends RecyclerView.ViewHolder{

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
