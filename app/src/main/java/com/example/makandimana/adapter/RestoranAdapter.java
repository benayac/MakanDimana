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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RestoranAdapter extends FirebaseRecyclerAdapter<RestoranModel, RestoranAdapter.RestoViewHolder> {

    private static OnItemClickListener listener;
    public RestoranAdapter(@NonNull FirebaseRecyclerOptions<RestoranModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull RestoViewHolder holder, int position, @NonNull RestoranModel model) {
        holder.tvAvgPrice.setText("Rp " + model.getMinPrice() + " - Rp " + model.getMaxPrice());
        holder.tvDistance.setText("KM123");
        holder.tvFoodType.setText(String.valueOf(model.getFoodType()));
        holder.tvRestoName.setText(String.valueOf(model.getNamaResto()));

        String imgUrl = model.getImgUrl();
        Picasso.get().load(imgUrl).resize(150, 150).centerInside().into(holder.imgResto);
    }

    @NonNull
    @Override
    public RestoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_restoran_card,
                viewGroup, false);
        return new RestoViewHolder(v);
    }

    public static class RestoViewHolder extends RecyclerView.ViewHolder{

        private TextView tvRestoName, tvFoodType, tvAvgPrice, tvDistance;
        private ImageView imgResto;

        public RestoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRestoName = itemView.findViewById(R.id.tvRestoName);
            tvFoodType = itemView.findViewById(R.id.tvFoodType);
            tvAvgPrice = itemView.findViewById(R.id.tvAvgPrice);
            tvDistance = itemView.findViewById(R.id.tvRestoDistance);
            imgResto = itemView.findViewById(R.id.imgResto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(v, position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
