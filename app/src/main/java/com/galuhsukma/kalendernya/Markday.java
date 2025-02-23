package com.galuhsukma.kalendernya;

import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_PUASA;
import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_ISTIHADHAH;
import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_UTAMA;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
    private String selected_date_string, selected_monthYear_string, tgl, jmark, qshalat;
    private int cshalat, cpuasa;
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
        tgl = intent.getStringExtra("tgldatabase");

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
                jmark = "HAID";
            }
        });
        cvmistihadhah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jmark = "ISTIHADHAH";
            }
        });

        //pilih sholat (gambar)
        cvsubuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Subuh";
            }
        });
        cvdhuhur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Dhuhur";
            }
        });
        cvashar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Ashar";
            }
        });
        cvmaghrib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Maghrib";
            }
        });
        cvisha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Isha'";
            }
        });

        //tombol simpan data
        simpanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ambil nilai checkbox saat tombol ditekan
                cshalat = cbshalat.isChecked() ? 1 : 0;
                cpuasa = cbpuasa.isChecked() ? 1 : 0;

                // Cek apakah `tgl` dan `jmark` sudah dipilih
                if (tgl == null || jmark == null || jmark.isEmpty()) {
                    Toast.makeText(Markday.this, "Silakan pilih jenis mark terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Simpan data ke SQLite
                long result = myDb.insertData(tgl, jmark, qshalat, cshalat, cpuasa);

                if (result != -1) {
                    Toast.makeText(Markday.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Markday.this, "Failed to Save Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void kembali(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }

    public void debug(View view) {
        Cursor cursor = myDb.getReadableDatabase().rawQuery("SELECT * FROM " + DB_TABLE_UTAMA, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String tanggal = cursor.getString(0);
                String jenisMark = cursor.getString(1);
                String Qshalat = cursor.getString(2);
                Integer Rshalat = cursor.getInt(3);
                Integer Rpuasa = cursor.getInt(4);
                Log.d("Database Check", "Tanggal: " + tanggal + ", Jenis: " + jenisMark+ ", Qshalat: " + Qshalat+ ", sudah sholat: " + Rshalat+ ", nambah hutang puasa ga: " + Rpuasa);
            }
        } else {
            Log.d("Database Check", "Tidak ada data di tabel.");
        }
        cursor.close();
    }
}