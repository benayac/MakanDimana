package com.example.makandimana;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.makandimana.adapter.*;
import com.example.makandimana.model.*;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RestaurantDetailActivity extends AppCompatActivity {

    //RestaurantDetailActivity merupakan activity yang menampilkan detail restoran
    //Detail restoran berupa nama restoran, gambar, jam buka, dan list menu makanan
    private RecyclerView recyclerView;
    private MenuMakananAdapter menuAdapter;
    private Query query;
    private TextView TVrestoName, TVrestoType, TVOpenHour;
    private ImageView imgResto, imgGMap;
    private DatabaseReference db;
    private Intent intent;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        //Menghubungkan xml dengan backend
        TVrestoName = findViewById(R.id.tvNamaResto);
        TVrestoType = findViewById(R.id.tvRestoType);
        TVOpenHour = findViewById(R.id.tvOpenHourBeneran);
        imgResto = findViewById(R.id.restoImageDetail);
        imgGMap = findViewById(R.id.btnGMap);

        //Mengambil intent
        intent = getIntent();

        //Memanggil method untuk mengubah tulisan, membangkitkan recycerview dan mengambil koordinat restoran dari database
        changeTextView();
        setUpRecyclerView();
        getLatLong();

        //berikut adalah implementasi dari interface onClickListener,
        //digunakan untuk membuka google map yang mengarahkan lokasi restoran yang diklik
        imgGMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse(uri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

    }

    //method ini digunakan untuk membangkitkan recyclerView
    public void setUpRecyclerView() {
        db = FirebaseDatabase.getInstance().getReference("menuMakanan");
        query = db.child(intent.getStringExtra("EXTRA_RESTO"));
        FirebaseRecyclerOptions<MenuMakananModel> options = new FirebaseRecyclerOptions.Builder<MenuMakananModel>()
                .setQuery(query, MenuMakananModel.class)
                .build();
        recyclerView = findViewById(R.id.recylcerviewMenu);
        menuAdapter = new MenuMakananAdapter(options);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantDetailActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(menuAdapter);
    }

    //Implementasi polimorfisme dengan fungsi sama seperti pada class sebelumnya
    @Override
    protected void onStart() {
        super.onStart();
        if (menuAdapter != null) {
            menuAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (menuAdapter != null)
            menuAdapter.stopListening();
    }

    //method ini digunakan untuk mengambil data pada database kemudian menampilkannya pada TextView dan gambar
    public void changeTextView() {
        db = FirebaseDatabase.getInstance().getReference("restaurant");
        db.child(intent.getStringExtra("EXTRA_RESTO")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String restoName = dataSnapshot.child("namaResto").getValue(String.class);
                String restoType = dataSnapshot.child("foodType").getValue(String.class);
                String openHour = dataSnapshot.child("openHr").getValue(String.class);
                String closeHour = dataSnapshot.child("closeHr").getValue(String.class);
                String imgUrl = dataSnapshot.child("imgUrl").getValue(String.class);
                Log.v("namaResto", restoName);
                TVrestoName.setText(restoName);
                TVrestoType.setText(restoType);
                TVOpenHour.setText(openHour + "-" + closeHour);

                Picasso.get().load(imgUrl).resize(150, 150).centerInside().into(imgResto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //method ini digunakan untuk mengambil koordinat tiap restoran dari database dan digabungkan menjadi perintah String
    public void getLatLong(){
        db = FirebaseDatabase.getInstance().getReference("restaurant");
        db.child(intent.getStringExtra("EXTRA_RESTO")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uriBegin = "geo:" + dataSnapshot.child("langitude").getValue(Double.class)
                        + ","
                        + dataSnapshot.child("longitude").getValue(Double.class);
                String query = dataSnapshot.child("langitude").getValue(Double.class)
                        + ","
                        + dataSnapshot.child("longitude").getValue(Double.class)
                        + "("
                        + dataSnapshot.child("namaResto").getValue(String.class)
                        + ")";
                String encodedQuery = Uri.encode(query);
                uri = uriBegin + "?q=" + encodedQuery + "&z=16";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
