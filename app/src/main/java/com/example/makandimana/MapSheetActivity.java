package com.example.makandimana;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.makandimana.adapter.RestoranAdapter;
import com.example.makandimana.adapter.menuMakananAdapter;
import com.example.makandimana.model.RestoranModel;
import com.example.makandimana.model.menuMakananModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.*;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapSheetActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final int REQUEST_CODE = 101;
    private BottomSheetBehavior mBottomSheetBehavior;
    private GoogleMap mMap;
    private TextView mShowMore;
    private RestoranAdapter restoranAdapter;
    private DatabaseReference db;
    private List<String> restoList;
    private FusedLocationProviderClient client;
    public static Location locations;
    public static int myBudget;
    public static String myMenu;
    private String sortMethod;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sheet);

        recyclerView = findViewById(R.id.recylcerviewRestoran);
        layoutManager = new LinearLayoutManager(MapSheetActivity.this);

        Intent intent = getIntent();
        myBudget = Integer.valueOf(intent.getStringExtra("EXTRA_BUDGET"));
        myMenu = intent.getStringExtra("EXTRA_SPINNER");

        CardView cvSortJarak = findViewById(R.id.cvSortJarak);
        CardView cvSortHarga = findViewById(R.id.cvSortHarga);
        cvSortJarak.setOnClickListener(this);
        cvSortHarga.setOnClickListener(this);

        client = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        createRestoList();
        mShowMore = findViewById(R.id.tvShowMore);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetCallback();
        setUpRecyclerView();
        restoranAdapter.setOnItemClickListener(new RestoranAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MapSheetActivity.this, restoList.get(position), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MapSheetActivity.this, RestaurantDetailActivity.class);
                intent.putExtra("EXTRA_RESTO", restoList.get(position));
                startActivity(intent);
            }
        });
    }

    private void fetchLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    locations = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapSheetActivity.this);
                } else {
                    Toast.makeText(MapSheetActivity.this, "Kamu lagi dimana sih? :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlng = new LatLng(locations.getLatitude(), locations.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latlng)
                .title("My Location");
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable));

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        mMap.addMarker(markerOptions);

        db = FirebaseDatabase.getInstance().getReference("restaurant");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> restos = dataSnapshot.getChildren();
                for (DataSnapshot resto : restos) {
                    if (myBudget >= dataSnapshot.child(resto.getKey()).child("minPrice").getValue(Integer.class)
                            && myMenu.equals(dataSnapshot.child(resto.getKey()).child("foodType").getValue(String.class))) {
                        Double lang = dataSnapshot.child(resto.getKey()).child("langitude").getValue(Double.class);
                        Double lot = dataSnapshot.child(resto.getKey()).child("longitude").getValue(Double.class);
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lang, lot))
                                .title(dataSnapshot.child(resto.getKey()).child("namaResto").getValue(String.class)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void createRestoList() {
        db = FirebaseDatabase.getInstance().getReference("restaurant");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> restos = dataSnapshot.getChildren();
                restoList = new ArrayList<>();
                for (DataSnapshot resto : restos) {
                    restoList.add(resto.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void bottomSheetCallback() {
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mShowMore.setText("");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        mShowMore.setText("");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mShowMore.setText("Tampilkan Daftar");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    public void setUpRecyclerView() {
        Query query = FirebaseDatabase.getInstance().getReference("restaurant");
        //query = FirebaseDatabase.getInstance().getReference("restaurant").orderByChild("minPrice");
        FirebaseRecyclerOptions<RestoranModel> options = new FirebaseRecyclerOptions.Builder<RestoranModel>()
                .setQuery(query, RestoranModel.class)
                .build();

        restoranAdapter = new RestoranAdapter(options);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(restoranAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (restoranAdapter != null) {
            restoranAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (restoranAdapter != null) {
            restoranAdapter.stopListening();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Toast.makeText(MapSheetActivity.this,
                            "Kamu lagi dimana sih? :(", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onClick(View v) {
        Query query;
        FirebaseRecyclerOptions<RestoranModel> options;
        switch (v.getId()) {
            case R.id.cvSortHarga:
                sortMethod = "minPrice";
                query = FirebaseDatabase.getInstance().getReference("restaurant").orderByChild(sortMethod);
                options = new FirebaseRecyclerOptions.Builder<RestoranModel>()
                    .setQuery(query, RestoranModel.class)
                    .build();

                restoranAdapter = new RestoranAdapter(options);
                restoranAdapter.startListening();
                recyclerView.setAdapter(restoranAdapter);
                Toast.makeText(MapSheetActivity.this, "Sorted by Price", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cvSortJarak:
                sortMethod = "langitude";
                query = FirebaseDatabase.getInstance().getReference("restaurant").orderByChild(sortMethod);
                options = new FirebaseRecyclerOptions.Builder<RestoranModel>()
                    .setQuery(query, RestoranModel.class)
                    .build();

                restoranAdapter = new RestoranAdapter(options);
                restoranAdapter.startListening();
                recyclerView.setAdapter(restoranAdapter);
                Toast.makeText(MapSheetActivity.this, "Sorted by Range", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
