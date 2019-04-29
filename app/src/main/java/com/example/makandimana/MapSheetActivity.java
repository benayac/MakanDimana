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

import com.example.makandimana.adapter.RestoranAdapter;
import com.example.makandimana.adapter.menuMakananAdapter;
import com.example.makandimana.model.RestoranModel;
import com.example.makandimana.model.menuMakananModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import com.example.*;

public class MapSheetActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BottomSheetBehavior mBottomSheetBehavior;
    private GoogleMap mMap;
    private TextView mShowMore;
    private CardView cardView;
    private RecyclerView recyclerView;
    private RestoranAdapter restoranAdapter;
    private ArrayList<RestoranModel> restoranArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sheet);

        addData();

        cardView = findViewById(R.id.cvSortJarak);

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

        mShowMore = findViewById(R.id.tvShowMore);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

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

        recyclerView = findViewById(R.id.recylcerviewRestoran);
        restoranAdapter = new RestoranAdapter(restoranArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MapSheetActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(restoranAdapter);

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

    public void addData(){

        restoranArrayList = new ArrayList<>();

        restoranArrayList.add(new RestoranModel("Kafe TETI", "Snack", "Rp 4.000 - Rp 10.000",
                "1.0KM", "https://pasca.jteti.ugm.ac.id/wp-content/uploads/sites/404/2018/01/wellcome2-1024x527.jpg"));
        restoranArrayList.add(new RestoranModel("Burjo Mekar Mulya", "Nasi", "Rp 5.000 - Rp 10.000",
                "2.0KM", "https://lh3.googleusercontent.com/4g_JU68GDOWudpc00s_P8hHRh_MM2NeLKqHjcZ7fPOHgyBAr4G9vVYuo-GbSqSivRBpflcOt=s1280-p-no-v1"));
        restoranArrayList.add(new RestoranModel("Waroeng SS", "Nasi", "Rp 15.000 - Rp 30.000", "1.5KM",
                "http://2.bp.blogspot.com/-f1-eIW-9uOg/VlQRx-GGLFI/AAAAAAAALSs/DY4_6rej3AA/s1600/PB213707.JPG"));
        restoranArrayList.add(new RestoranModel("Bakmi Afui","Mie", "Rp 8.000 - Rp 15.000", "1.8KM",
                "http://www.waktumakan.com/wp-content/uploads/2016/04/Mie-Palembang-Afui-Kini-Hadir-di-Jogja.jpg"));
    }
}
