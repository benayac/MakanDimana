package com.example.makandimana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//berikut merupakan class MainActivity, dimana user menginput budget dan pilihan menu.

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText etBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Membuat sebuah spinner yang berisikan menu makanan dengan String yang disimpan pada array.
        String[] menuArray = getResources().getStringArray(R.array.menu_arrays);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, menuArray);
        final Spinner spinner = findViewById(R.id.spinnerJenismKanan);
        spinner.setAdapter(arrayAdapter);

        //Menghubungkan view pada xml dengan backend
        etBudget = findViewById(R.id.etDuit);
        Button btnCari = findViewById(R.id.btnCari);

        //Aplikasi ini menggunakan database Firebase milik Google
        databaseReference = FirebaseDatabase.getInstance().getReference("menuMakanan");

        /*
            method onClick yang akan membuka activity MapSheetActivity dan mengirimkan data
            yang berasal dari editText dan pilihan menu yang berasal dari spinner.
            Hal ini menerapkan pilar PBO yaitu polimorfisme karena mengimplementasikan interface OnClickListener.
        */
        btnCari.setOnClickListener(new View.OnClickListener() {
            //implementasi dari polimorfisme
            @Override
            public void onClick(View v) {
                String text = spinner.getSelectedItem().toString();
                Intent intent = new Intent(MainActivity.this, MapSheetActivity.class);
                intent.putExtra("EXTRA_SPINNER", text);
                intent.putExtra("EXTRA_BUDGET", etBudget.getText().toString());
                startActivity(intent);

            }
        });
    }
}
