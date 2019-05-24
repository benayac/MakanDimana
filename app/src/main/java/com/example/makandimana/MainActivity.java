package com.example.makandimana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

        String[] menuArray = getResources().getStringArray(R.array.menu_arrays);
        ArrayAdapter<String>arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, menuArray);
        final Spinner spinner = findViewById(R.id.spinnerJenismKanan);
        spinner.setAdapter(arrayAdapter);

        etBudget = findViewById(R.id.etDuit);
        Button btnCari = findViewById(R.id.btnCari);
        databaseReference = FirebaseDatabase.getInstance().getReference("menuMakanan");

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spinner.getSelectedItem().toString();
                Intent intent = new Intent(MainActivity.this, MapSheetActivity.class);
                intent.putExtra("EXTRA_SPINNER", text);
                intent.putExtra("EXTRA_BUDGET", etBudget.getText().toString());
                Toast.makeText(MainActivity.this, "Menu = " + text, Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
    }
}
