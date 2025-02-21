package com.galuhsukma.kalendernya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.jar.JarEntry;

public class Markday extends AppCompatActivity {
    DatabaseHelper myDb;
    private TextView dayselected;
    private String selected_date_string, selected_monthYear_string, tgl_database, jenismark, qadhashalat;
    private int centangshalat, centangpuasa;
    private CardView cvmhaid, cvmistihadhah, cvsubuh, cvdhuhur, cvashar, cvmaghrib, cvisha;
    private CheckBox cbshalat, cbpuasa;
    Button simpanbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_markday);

        myDb = new DatabaseHelper(this);

        Intent intent = getIntent();
        selected_date_string = intent.getStringExtra("chosenDay");
        selected_monthYear_string = intent.getStringExtra("monthyear");
        tgl_database = intent.getStringExtra("tgldatabase");

        dayselected = findViewById(R.id.dayselected);
        cvmhaid = findViewById(R.id.cvmarkhaid);
        cvmistihadhah = findViewById(R.id.cvmarkistihadhah);
        cvsubuh = findViewById(R.id.cvsubuh);
        cvdhuhur = findViewById(R.id.cvdhuhur);
        cvashar = findViewById(R.id.cvashar);
        cvmaghrib = findViewById(R.id.cvmaghrib);
        cvisha = findViewById(R.id.cvisha);
        cbshalat = findViewById(R.id.cekboxshalat);
        cbpuasa = findViewById(R.id.cekboxpuasa);
        simpanbtn = findViewById(R.id.btnsimpan);

        //nampilin tanggal
        dayselected.setText(selected_date_string+" "+selected_monthYear_string);

        //pilih jenis mark tanggal (H/I)
        cvmhaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenismark = "HAID";
            }
        });
        cvmistihadhah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenismark = "ISTIHADHAH";
            }
        });

        //pilih sholat (gambar)
        cvsubuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qadhashalat = "Shalat Subuh";
            }
        });
        cvdhuhur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qadhashalat = "Shalat Dhuhur";
            }
        });
        cvashar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qadhashalat = "Shalat Ashar";
            }
        });
        cvmaghrib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qadhashalat = "Shalat Maghrib";
            }
        });
        cvisha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qadhashalat = "Shalat Isha'";
            }
        });

        //centangin sholat puasa
        if (cbshalat.isChecked()) {
            centangshalat = 1;
        } else {
            centangshalat = 0;
        }

        if (cbpuasa.isChecked()) {
            centangpuasa = 1;
        } else {
            centangpuasa = 0;
        }


        //tombol simpan data
        simpanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDb.insertData(tgl_database, jenismark, qadhashalat, centangshalat, centangpuasa);
                Toast.makeText(Markday.this,"Data Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void kembali(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }

}