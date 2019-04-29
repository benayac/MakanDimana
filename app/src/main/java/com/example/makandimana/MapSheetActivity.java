package com.example.makandimana;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapSheetActivity extends AppCompatActivity implements OnMapReadyCallback {

    private BottomSheetBehavior mBottomSheetBehavior;
    private GoogleMap mMap;
    private TextView mShowMore;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sheet);

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng teti = new LatLng(-7.765874, 110.371725);
        LatLng mm = new LatLng(-7.761575, 110.375409);
        LatLng ss = new LatLng(-7.762638, 110.375731);
        LatLng afui = new LatLng(-7.761946, 110.378177);
        mMap.addMarker(new MarkerOptions().position(teti).title("Marker in Teti"));
        mMap.addMarker(new MarkerOptions().position(mm).title("Marker in Burjo Mekar Mulya"));
        mMap.addMarker(new MarkerOptions().position(ss).title("Marker in Waroeng Super Sambal"));
        mMap.addMarker(new MarkerOptions().position(afui).title("Marker in Bakmi Afui"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(teti));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
