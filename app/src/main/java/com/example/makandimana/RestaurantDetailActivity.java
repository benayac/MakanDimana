package com.example.makandimana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.makandimana.adapter.*;
import com.example.makandimana.model.*;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class RestaurantDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private menuMakananAdapter menuAdapter;
    private ArrayList<menuMakananModel> makananArrayList;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        /*addData();

        recyclerView = findViewById(R.id.recylcerviewMenu);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantDetailActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(menuAdapter);
        */

        setUpRecyclerView();

    }

    public void addData(){
        makananArrayList = new ArrayList<>();
        makananArrayList.add(new menuMakananModel("Magelangan", 10000, "https://img-global.cpcdn.com/003_recipes/b0ff1cd295d6186a/751x532cq70/magelangan-ala-burjo-foto-resep-utama.jpg"));
        makananArrayList.add(new menuMakananModel("Orak Arik", 11000, "https://scontent-atl3-1.cdninstagram.com/vp/32a5ce6b391ac67122efc7ce47385c29/5D4A7113/t51.2885-15/e35/38868662_1676598872467442_6570355851758927872_n.jpg?_nc_ht=scontent-atl3-1.cdninstagram.com"));
        makananArrayList.add(new menuMakananModel("Nasi Telor", 9000, "https://cdn-image.hipwee.com/wp-content/uploads/2016/11/hipwee-3DRP8T34BF50808D11229Cmx.jpg"));
        makananArrayList.add(new menuMakananModel("Omelet", 10000, "https://3.bp.blogspot.com/-V-uZ3N1gxVI/V4591zp2C8I/AAAAAAAAAW0/mJOQHysrUqoCs9Nkf3TN4KXcOHIlC4J0ACLcB/s1600/P_20160719_115004_HDR.jpg"));
        makananArrayList.add(new menuMakananModel("Burjo", 5000, "https://img-global.cpcdn.com/003_recipes/d7e1edd176470715/751x532cq70/bubur-kacang-ijo-burjo-simple-foto-resep-utama.jpg"));
    }

    public void setUpRecyclerView(){

        Intent intent = getIntent();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("menuMakanan").child(intent.getStringExtra("EXTRA_RESTO"));

        query = db;

        FirebaseRecyclerOptions<menuMakananModel> options = new FirebaseRecyclerOptions.Builder<menuMakananModel>()
                .setQuery(query, menuMakananModel.class)
                .build();

        recyclerView = findViewById(R.id.recylcerviewMenu);
        menuAdapter = new menuMakananAdapter(options);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantDetailActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(menuAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(menuAdapter != null){
            menuAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(menuAdapter != null)
            menuAdapter.stopListening();
    }
}
