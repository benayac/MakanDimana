package com.example.makandimana.adapter;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makandimana.MapSheetActivity;
import com.example.makandimana.R;
import com.example.makandimana.model.*;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class RestoranAdapter extends FirebaseRecyclerAdapter<RestoranModel, RestoranAdapter.RestoViewHolder> {

    private static OnItemClickListener listener;
    public RestoranAdapter(@NonNull FirebaseRecyclerOptions<RestoranModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RestoViewHolder holder, int position, @NonNull RestoranModel model) {
        Location loc = new Location("");
        loc.setLatitude(model.getLangitude());
        loc.setLongitude(model.getLongitude());
        Location loc2 = new Location("");
        loc2.setLatitude(MapSheetActivity.locations.getLatitude());
        loc2.setLongitude(MapSheetActivity.locations.getLongitude());
        int distance = Math.round(loc.distanceTo(loc2));

        if(MapSheetActivity.myBudget >= model.getMinPrice() && model.getFoodType().equals(MapSheetActivity.myMenu)){
            holder.tvAvgPrice.setText("Rp " + model.getMinPrice() + " - Rp " + model.getMaxPrice());
            holder.tvFoodType.setText(String.valueOf(model.getFoodType()));
            holder.tvRestoName.setText(String.valueOf(model.getNamaResto()));
            if(distance >= 1000){
                holder.tvDistance.setText(distance / 1000 + "km");
            } else {
                holder.tvDistance.setText(distance + "m");
            }

            String imgUrl = model.getImgUrl();
            Picasso.get().load(imgUrl).resize(150, 150).centerInside().into(holder.imgResto);

        } else{
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

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
