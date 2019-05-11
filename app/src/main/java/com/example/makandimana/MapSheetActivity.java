package com.example.makandimana;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makandimana.adapter.RestoranAdapter;
import com.example.makandimana.adapter.menuMakananAdapter;
import com.example.makandimana.model.RestoranModel;
import com.example.makandimana.model.menuMakananModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import com.example.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MapSheetActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BottomSheetBehavior mBottomSheetBehavior;
    private GoogleMap mMap;
    private TextView mShowMore;
    private CardView cardView;
    private RecyclerView recyclerView;
    private RestoranAdapter restoranAdapter;
    private DatabaseReference db;
    private Query query;
    private List<String> restoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sheet);

        cardView = findViewById(R.id.cvSortJarak);
        mShowMore = findViewById(R.id.tvShowMore);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapSheetActivity.this, RestaurantDetailActivity.class);
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomSheetCallback();
        setUpRecyclerView();
        createRestoList();

        restoranAdapter.setOnItemClickListener(new RestoranAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MapSheetActivity.this, restoList.get(position), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng teti = new LatLng(-7.765874, 110.371725);
        LatLng mm = new LatLng(-7.761575, 110.375409);
        LatLng ss = new LatLng(-7.762638, 110.375731);
        LatLng afui = new LatLng(-7.761946, 110.378177);
        mMap.addMarker(new MarkerOptions().position(teti).title("Teti"));
        mMap.addMarker(new MarkerOptions().position(mm).title("Burjo Mekar Mulya"));
        mMap.addMarker(new MarkerOptions().position(ss).title("Waroeng SS"));
        mMap.addMarker(new MarkerOptions().position(afui).title("Bakmi Afui"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(teti));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void createRestoList(){
        db = FirebaseDatabase.getInstance().getReference("restaurant");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> restos = dataSnapshot.getChildren();
                restoList = new ArrayList<>();
                for(DataSnapshot resto : restos){
                    restoList.add(resto.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void bottomSheetCallback(){
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i){
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mShowMore.setText("");

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mShowMore.setText("");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mShowMore.setText("Show More");
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    public void setUpRecyclerView(){

        query = FirebaseDatabase.getInstance().getReference("restaurant");
        FirebaseRecyclerOptions<RestoranModel> options = new FirebaseRecyclerOptions.Builder<RestoranModel>()
                .setQuery(query, RestoranModel.class)
                .build();

        recyclerView = findViewById(R.id.recylcerviewRestoran);
        restoranAdapter = new RestoranAdapter(options);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MapSheetActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(restoranAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(restoranAdapter != null){
            restoranAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (restoranAdapter != null){
            restoranAdapter.stopListening();
        }
    }
}
