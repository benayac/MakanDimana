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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class menuMakananAdapter extends RecyclerView.Adapter<menuMakananAdapter.MenuMakananViewHolder> {

    private ArrayList<menuMakananModel> listMakanan;

    public menuMakananAdapter(ArrayList<menuMakananModel> listMakanan){
        this.listMakanan = listMakanan;
    }

    @NonNull
    @Override
    public MenuMakananViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_makanan_card, viewGroup, false);
        return new MenuMakananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuMakananViewHolder menuMakananViewHolder, int i) {

        String imageUrl = listMakanan.get(i).getImgUrl();
        menuMakananViewHolder.tvHargaMakanan.setText("Rp " + Integer.toString(listMakanan.get(i).getFoodPrice()));
        menuMakananViewHolder.tvNamaMakanan.setText(listMakanan.get(i).getFoodName());

        Picasso.get().load(imageUrl).resize(200, 200).centerInside().into(menuMakananViewHolder.imgFood);
    }

    @Override
    public int getItemCount() {
        return (listMakanan != null) ? listMakanan.size() : 0;
    }

    public class MenuMakananViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNamaMakanan, tvHargaMakanan;
        private ImageView imgFood;

        public MenuMakananViewHolder(View itemView) {
            super(itemView);

            imgFood = itemView.findViewById(R.id.imgFood);
            tvNamaMakanan = itemView.findViewById(R.id.tvFoodName);
            tvHargaMakanan = itemView.findViewById(R.id.tvFoodPrice);
        }
    }
}
