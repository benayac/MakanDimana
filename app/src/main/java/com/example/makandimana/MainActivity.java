package com.example.makandimana;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.example.makandimana.model.RestoranModel;
import com.example.makandimana.model.menuMakananModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCari = findViewById(R.id.btnCari);
        databaseReference = FirebaseDatabase.getInstance().getReference("menuMakanan");

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AddToFireabase();
                Intent intent = new Intent(MainActivity.this, MapSheetActivity.class);
                startActivity(intent);

            }
        });
    }

    public void AddToFireabase(){
        /*RestoranModel kaftet = new RestoranModel("Kafe TETI", "Snack", "https://pasca.jteti.ugm.ac.id/wp-content/uploads/sites/404/2018/01/wellcome2-1024x527.jpg",
                -7.765874, 110.371725, 5000, 10000);
        RestoranModel mm = new RestoranModel("Burjo Mekar Mulya", "Nasi", "https://lh3.googleusercontent.com/4g_JU68GDOWudpc00s_P8hHRh_MM2NeLKqHjcZ7fPOHgyBAr4G9vVYuo-GbSqSivRBpflcOt=s1280-p-no-v1",
                -7.761575, 110.375409,5000,10000);
        RestoranModel ss = new RestoranModel("Waroeng SS", "Nasi", "http://2.bp.blogspot.com/-f1-eIW-9uOg/VlQRx-GGLFI/AAAAAAAALSs/DY4_6rej3AA/s1600/PB213707.JPG",
                -7.762638, 110.375731, 15000, 30000);
        RestoranModel afui = new RestoranModel("Bakmi Afui","Mie", "http://www.waktumakan.com/wp-content/uploads/2016/04/Mie-Palembang-Afui-Kini-Hadir-di-Jogja.jpg",
                -7.761946, 110.378177, 8000, 15000);

        databaseReference.child("mm").setValue(mm);
        databaseReference.child("kaftet").setValue(kaftet);
        databaseReference.child("ss").setValue(ss);
        databaseReference.child("afui").setValue(afui);
        */

        menuMakananModel menuMM1 = new menuMakananModel("Magelangan", 10000, "https://img-global.cpcdn.com/003_recipes/b0ff1cd295d6186a/751x532cq70/magelangan-ala-burjo-foto-resep-utama.jpg");
        menuMakananModel menuMM2 = new menuMakananModel("Orak Arik", 11000, "https://scontent-atl3-1.cdninstagram.com/vp/32a5ce6b391ac67122efc7ce47385c29/5D4A7113/t51.2885-15/e35/38868662_1676598872467442_6570355851758927872_n.jpg?_nc_ht=scontent-atl3-1.cdninstagram.com");
        menuMakananModel menuMM3 = new menuMakananModel("Nasi Telor", 9000, "https://cdn-image.hipwee.com/wp-content/uploads/2016/11/hipwee-3DRP8T34BF50808D11229Cmx.jpg");
        menuMakananModel menuMM4 = new menuMakananModel("Omelet", 10000, "https://3.bp.blogspot.com/-V-uZ3N1gxVI/V4591zp2C8I/AAAAAAAAAW0/mJOQHysrUqoCs9Nkf3TN4KXcOHIlC4J0ACLcB/s1600/P_20160719_115004_HDR.jpg");
        menuMakananModel menuMM5 = new menuMakananModel("Burjo", 5000, "https://img-global.cpcdn.com/003_recipes/d7e1edd176470715/751x532cq70/bubur-kacang-ijo-burjo-simple-foto-resep-utama.jpg");

        databaseReference.child("mm").child(menuMM1.getFoodName()).setValue(menuMM1);
        databaseReference.child("mm").child(menuMM2.getFoodName()).setValue(menuMM2);
        databaseReference.child("mm").child(menuMM3.getFoodName()).setValue(menuMM3);
        databaseReference.child("mm").child(menuMM4.getFoodName()).setValue(menuMM4);
        databaseReference.child("mm").child(menuMM5.getFoodName()).setValue(menuMM5);
    }
}
