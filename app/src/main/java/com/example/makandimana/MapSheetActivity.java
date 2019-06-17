package com.example.makandimana;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
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
import com.example.makandimana.model.RestoranModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MapSheetActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowClickListener {

    /*
        MapSheetActivity merupakan activity utama dimana user dapat mencari restoran melalui
        list dan marker pada map.
     */

    private static final int REQUEST_CODE = 101;
    public static Location locations;
    public static int myBudget;
    public static String myMenu;
    private BottomSheetBehavior mBottomSheetBehavior;
    private GoogleMap mMap;
    private RestoranAdapter restoranAdapter;
    private DatabaseReference db;
    private List<String> restoList;
    private List<String> restoListMarker;
    private List<String> restoTitleList;
    private FusedLocationProviderClient client;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_sheet);

        //Mengambil data budget dan pilihan menu yang berasal dari activity sebelumnya
        Intent intent = getIntent();
        myBudget = Integer.valueOf(intent.getStringExtra("EXTRA_BUDGET"));
        myMenu = intent.getStringExtra("EXTRA_SPINNER");

        //Menghubungkan xml dengan backend dan menambahkan interface onClickListener
        CardView cvSortJarak = findViewById(R.id.cvSortJarak);
        CardView cvSortHarga = findViewById(R.id.cvSortHarga);
        CardView cvChooseMenu = findViewById(R.id.cvChooseMenu);
        cvSortJarak.setOnClickListener(this);
        cvSortHarga.setOnClickListener(this);
        cvChooseMenu.setOnClickListener(this);

        //Mendapatkan lokasi user
        client = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        //Membuat list restoran untuk pertama kali
        createRestoList();

        //Menghubungkan xml bottomsheet dengan backend
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        //Membangkitkan recyclerview
        setUpRecyclerView();

        //Mengimplementasikan interface OnClickListener untuk tiap list recyclerview
        restoranAdapter.setOnItemClickListener(new RestoranAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MapSheetActivity.this, RestaurantDetailActivity.class);
                intent.putExtra("EXTRA_RESTO", restoList.get(position));
                startActivity(intent);
            }
        });
    }

    /*
        berikut merupakan method fetchLastLocation yang mengimplementasikan konsep enkapsulasi
        method ini digunakan untuk meminta user mengaktifkan GPS lalu mendapatkan lokasi user saat itu
     */
    private void fetchLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            //implementasi dari konsep polimorfise
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

    /*
        implementasi polimorfisme, method ini akan membuat marker sesuai jumlah restoran
        dan dijalankan sebelum map dimunculkan. Fitur Map menggunakan API Map milik Google.
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnInfoWindowClickListener(this);

        LatLng latlng = new LatLng(locations.getLatitude(), locations.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latlng)
                .title("My Location");
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
        Bitmap b = bitmapDrawable.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        mMap.addMarker(markerOptions);

        //Mengiterasi database untuk membuat marker sesuai dengan data pada database
        db = FirebaseDatabase.getInstance().getReference("restaurant");
        db.orderByChild("minPrice").endAt(myBudget).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> restos = dataSnapshot.getChildren();
                for (DataSnapshot resto : restos) {
                    if (myMenu.equals(dataSnapshot.child(resto.getKey()).child("foodType").getValue(String.class))) {
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

    //method untuk membuat list berisi daftar restoran dari database, menerapkan prinsip enkapsulasi
    public void createRestoList() {
        db = FirebaseDatabase.getInstance().getReference("restaurant");
        db.orderByChild("minPrice").endAt(myBudget).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> restos = dataSnapshot.getChildren();
                restoList = new ArrayList<>();
                restoTitleList = new ArrayList<>();
                restoListMarker = new ArrayList<>();
                for (DataSnapshot resto : restos) {
                    restoList.add(resto.getKey());
                    restoTitleList.add(resto.child("namaResto").getValue(String.class));
                    restoListMarker.add(resto.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
        method untuk membuat list berisi daftar restoran yang sudah diurutkan dari database,
        menerapkan prinsip enkapsulasi
     */
    public void createSortedRestoList(String sortMethod) {
        db = FirebaseDatabase.getInstance().getReference("restaurant");
        db.orderByChild(sortMethod).addValueEventListener(new ValueEventListener() {
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

    /*
        method setUpRecylerView digunakan untuk menyiapkan recyclerView
        merupakan implementasi dari konsep enkapsulasi
     */
    public void setUpRecyclerView() {
        Query query = FirebaseDatabase.getInstance().getReference("restaurant").orderByChild("minPrice").endAt(myBudget);
        FirebaseRecyclerOptions<RestoranModel> options = new FirebaseRecyclerOptions.Builder<RestoranModel>()
                .setQuery(query, RestoranModel.class)
                .build();
        restoranAdapter = new RestoranAdapter(options);
        recyclerView = findViewById(R.id.recylcerviewRestoran);
        layoutManager = new LinearLayoutManager(MapSheetActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(restoranAdapter);
    }

    //Menerapkan pilar polimorfisme dengan adanya overried pada kedua method di bawah ini
    //Override method onStart akan membuat adapter mengecek isi database dan onStop akan menghentikan pengecekan
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

    //Mengoverride method onRequestPermissionsResult
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

    //Method onClick ini merupakan implementasi dari interface View.OnClickListener, sehingga menerapkan konsep polimorfisme
    @Override
    public void onClick(View v) {
        Query query;
        FirebaseRecyclerOptions<RestoranModel> options;

        //akan melakukan pengecekan switch case dengan case tiap tombol yang berbeda
        //untuk tiap tombol akan membangkitkan recyclerview kembali dengan method sorting / query database yang berbeda
        switch (v.getId()) {
            case R.id.cvSortHarga:
                String sortMethod = "minPrice";
                query = FirebaseDatabase.getInstance().getReference("restaurant").orderByChild(sortMethod);
                options = new FirebaseRecyclerOptions.Builder<RestoranModel>()
                        .setQuery(query, RestoranModel.class)
                        .build();

                restoranAdapter = new RestoranAdapter(options);
                restoranAdapter.startListening();
                recyclerView.setAdapter(restoranAdapter);
                Toast.makeText(MapSheetActivity.this, "Sorted by Price", Toast.LENGTH_SHORT).show();
                createSortedRestoList(sortMethod);
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
                createSortedRestoList(sortMethod);
                break;

            case R.id.cvChooseMenu:
                finish();
                break;
        }
    }

    /*
        method onInfoWindowClick ini merupakan implementasi dari interface GoogleMap.OnInfoWindowClickListener
        method ini digunakan untuk membuka menu restoran yang diklik markernya
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            int index = restoTitleList.indexOf(marker.getTitle());
            Intent intent = new Intent(this, RestaurantDetailActivity.class);
            intent.putExtra("EXTRA_RESTO", restoListMarker.get(index));
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Ini Lokasimu sekarang!", Toast.LENGTH_SHORT).show();
        }
    }
}
