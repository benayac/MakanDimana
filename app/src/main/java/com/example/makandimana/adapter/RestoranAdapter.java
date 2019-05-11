package com.example.makandimana.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.makandimana.R;
import com.example.makandimana.model.*;
import com.squareup.picasso.Picasso;

public class RestoranAdapter extends RecyclerView.Adapter<RestoranAdapter.RestoranViewHolder> {

    private ArrayList<RestoranModel> restoranList;

    public RestoranAdapter(ArrayList<RestoranModel> restoranList) {
        this.restoranList = restoranList;
    }

    @NonNull
    @Override
    public RestoranViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.list_restoran_card, viewGroup, false);
        return new RestoranViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoranViewHolder restoranViewHolder, int i) {
        /*String imgUrl = restoranList.get(i).getImgUrl();

        restoranViewHolder.tvRestoName.setText(restoranList.get(i).getNamaResto());
        restoranViewHolder.tvFoodType.setText(restoranList.get(i).getFoodType());
        restoranViewHolder.tvDistance.setText(restoranList.get(i).getDistance());
        restoranViewHolder.tvAvgPrice.setText(restoranList.get(i).getAveragePrice());

        Picasso.get().load(imgUrl).resize(150, 150).centerInside().into(restoranViewHolder.imgResto);
        */

    }

    @Override
    public int getItemCount() {
        return (restoranList != null) ? restoranList.size() : 0;
    }

    public class RestoranViewHolder extends RecyclerView.ViewHolder{

        private TextView tvRestoName, tvFoodType, tvAvgPrice, tvDistance;
        private ImageView imgResto;


        public RestoranViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestoName = itemView.findViewById(R.id.tvRestoName);
            tvFoodType = itemView.findViewById(R.id.tvFoodType);
            tvAvgPrice = itemView.findViewById(R.id.tvAvgPrice);
            tvDistance = itemView.findViewById(R.id.tvRestoDistance);
            imgResto = itemView.findViewById(R.id.imgResto);
        }
    }


}
