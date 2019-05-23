package com.example.makandimana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.makandimana.model.RestoranModel;
import com.example.makandimana.model.menuMakananModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    EditText etBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etBudget = findViewById(R.id.etDuit);
        Button btnCari = findViewById(R.id.btnCari);
        databaseReference = FirebaseDatabase.getInstance().getReference("menuMakanan");

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapSheetActivity.class);
                intent.putExtra("EXTRA_BUDGET", etBudget.getText().toString());
                startActivity(intent);

            }
        });
    }
}
