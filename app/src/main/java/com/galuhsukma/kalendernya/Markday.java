package com.galuhsukma.kalendernya;

import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_PUASA;
import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_REMINDER;
import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_UTAMA;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private int cshalat, cpuasa, qdisplay;
    private CardView cvmhaid, cvmistihadhah, cvsubuh, cvdhuhur, cvashar, cvmaghrib, cvisha;
    private CheckBox cbshalat, cbpuasa;
    LinearLayout konten, centang2, llbutton;
    Button simpanbtn, hapusbtn;
    TextView perintahcentangistihadhah;
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
        qshalat = "Tidak Ada";
        qdisplay = 0;

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
        hapusbtn = findViewById(R.id.btnhapus);
        konten = findViewById(R.id.konten);
        centang2 = findViewById(R.id.centang2);
        llbutton = findViewById(R.id.llbutton);
        perintahcentangistihadhah = findViewById(R.id.centangqadha2);

        //nampilin tanggal
        dayselected.setText(selected_date_string+" "+selected_monthYear_string);

        //pilih jenis mark tanggal (H/I)
        cvmhaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jmark = "HAID";
                cvmhaid.setCardBackgroundColor(Color.YELLOW);
                cvmistihadhah.setCardBackgroundColor(Color.WHITE);
                // Menampilkan konten LinearLayout
                perintahcentangistihadhah.setVisibility(View.GONE);
                konten.setVisibility(View.VISIBLE);
                centang2.setVisibility(View.VISIBLE);
                llbutton.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) centang2.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, konten.getId());
                centang2.setLayoutParams(params);
            }
        });
        cvmistihadhah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jmark = "ISTIHADHAH";
                cvmistihadhah.setCardBackgroundColor(Color.YELLOW);
                cvmhaid.setCardBackgroundColor(Color.WHITE);
                // Menghide konten LinearLayout
                perintahcentangistihadhah.setVisibility(View.VISIBLE);
                konten.setVisibility(View.GONE);
                centang2.setVisibility(View.VISIBLE);
                llbutton.setVisibility(View.VISIBLE);
            }
        });

        //pilih sholat (gambar)
        cvsubuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Subuh";
                qdisplay = 1;
                cvsubuh.setCardBackgroundColor(Color.YELLOW);
                cvdhuhur.setCardBackgroundColor(Color.WHITE);
                cvashar.setCardBackgroundColor(Color.WHITE);
                cvmaghrib.setCardBackgroundColor(Color.WHITE);
                cvisha.setCardBackgroundColor(Color.WHITE);
            }
        });
        cvdhuhur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Dhuhur";
                qdisplay = 1;
                cvsubuh.setCardBackgroundColor(Color.WHITE);
                cvdhuhur.setCardBackgroundColor(Color.YELLOW);
                cvashar.setCardBackgroundColor(Color.WHITE);
                cvmaghrib.setCardBackgroundColor(Color.WHITE);
                cvisha.setCardBackgroundColor(Color.WHITE);
            }
        });
        cvashar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Ashar";
                qdisplay = 1;
                cvsubuh.setCardBackgroundColor(Color.WHITE);
                cvdhuhur.setCardBackgroundColor(Color.WHITE);
                cvashar.setCardBackgroundColor(Color.YELLOW);
                cvmaghrib.setCardBackgroundColor(Color.WHITE);
                cvisha.setCardBackgroundColor(Color.WHITE);
            }
        });
        cvmaghrib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Maghrib";
                qdisplay = 1;
                cvsubuh.setCardBackgroundColor(Color.WHITE);
                cvdhuhur.setCardBackgroundColor(Color.WHITE);
                cvashar.setCardBackgroundColor(Color.WHITE);
                cvmaghrib.setCardBackgroundColor(Color.YELLOW);
                cvisha.setCardBackgroundColor(Color.WHITE);
            }
        });
        cvisha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qshalat = "Shalat Isha'";
                qdisplay = 1;
                cvsubuh.setCardBackgroundColor(Color.WHITE);
                cvdhuhur.setCardBackgroundColor(Color.WHITE);
                cvashar.setCardBackgroundColor(Color.WHITE);
                cvmaghrib.setCardBackgroundColor(Color.WHITE);
                cvisha.setCardBackgroundColor(Color.YELLOW);
            }
        });

        fetchdata();

        //tombol simpan data
        simpanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ambil nilai checkbox saat tombol ditekan
                cshalat = cbshalat.isChecked() ? 1 : 0;
                cpuasa = cbpuasa.isChecked() ? 1 : 0;

                if ("ISTIHADHAH".equals(jmark)) {
                    qshalat = "Tidak Ada";
                    cshalat = 0;
                }

                if ("HAID".equals(jmark) && !"Tidak Ada".equals(qshalat)){
                    Cursor cursor2 = myDb.getReadableDatabase().rawQuery(
                            "SELECT display FROM " + DB_TABLE_UTAMA + " WHERE display = 1", null);
                    if (cursor2 != null && cursor2.moveToFirst()) {
                        int qdisplay = cursor2.getInt(cursor2.getColumnIndex("display"));
                        // Gunakan nilai qdisplay sesuai kebutuhan
                    }
                    cursor2.close();

                    myDb.getWritableDatabase().execSQL(
                            "UPDATE " + DB_TABLE_UTAMA + " SET display = 0 WHERE display = 1");

                    ContentValues cv = new ContentValues();
                    cv.put("display", 0);
                    myDb.getWritableDatabase().update(DB_TABLE_UTAMA, cv, "display = ?", new String[]{"1"});
                }

                // Cek apakah `tgl` dan `jmark` sudah dipilih
                if (tgl == null || jmark == null || jmark.isEmpty()) {
                    Toast.makeText(Markday.this, "Silakan pilih jenis mark terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Simpan data ke SQLite
                int result = myDb.updateData(tgl, jmark, qshalat, cshalat, cpuasa, qdisplay);

                if (result > 0) {
                    Toast.makeText(Markday.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Jika update gagal (misalnya record tidak ada), lakukan insert
                    long insertResult = myDb.insertData(tgl, jmark, qshalat, cshalat, cpuasa, qdisplay);
                    if (insertResult != -1) {
                        Toast.makeText(Markday.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Markday.this, "Failed to Save Data", Toast.LENGTH_SHORT).show();
                    }
                }
                // Pindah kembali ke MainActivity
                Intent intent = new Intent(Markday.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        hapusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pastikan id tgl tidak null atau kosong
                if (tgl == null || tgl.isEmpty()) {
                    Toast.makeText(Markday.this, "Tidak ada data yang dipilih untuk dihapus", Toast.LENGTH_SHORT).show();
                    return;
                }
                int result = myDb.deleteData(tgl);
                if (result > 0) {
                    Toast.makeText(Markday.this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Markday.this, "Failed to Delete Data", Toast.LENGTH_SHORT).show();
                }

                // Pindah kembali ke MainActivity
                Intent intent = new Intent(Markday.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchdata() {
        Cursor cursor = myDb.getReadableDatabase().rawQuery("select * from "+DB_TABLE_UTAMA+" where id_tgl ='"+tgl+"';", null);
        if (cursor != null && cursor.moveToFirst()) {
            // Misalnya, jika urutan kolom adalah: id_tgl, jmark, qshalat, cshalat, cpuasa
            tgl = cursor.getString(cursor.getColumnIndex("id_tgl"));
            jmark = cursor.getString(cursor.getColumnIndex("jenismark"));
            qshalat = cursor.getString(cursor.getColumnIndex("waktushalat"));
            cshalat = cursor.getInt(cursor.getColumnIndex("centangshalat"));
            cpuasa = cursor.getInt(cursor.getColumnIndex("centangpuasa"));
            qdisplay = cursor.getInt(cursor.getColumnIndex("display"));
        }
        if (cursor != null) {
            cursor.close();
        }


        //jmark
        if ("HAID".equals(jmark)) {
            cvmhaid.callOnClick();
        } else if ("ISTIHADHAH".equals(jmark)) {
            cvmistihadhah.callOnClick();
        }

        //waktushalat
        if ("Shalat Subuh".equals(qshalat)) {
            cvsubuh.callOnClick();
        } else if ("Shalat Dhuhur".equals(qshalat)) {
            cvdhuhur.callOnClick();
        } else if ("Shalat Ashar".equals(qshalat)) {
            cvashar.callOnClick();
        } else if ("Shalat Maghrib".equals(qshalat)) {
            cvmaghrib.callOnClick();
        } else if ("Shalat Isha'".equals(qshalat)) {
            cvisha.callOnClick();
        }

        //centang shalat-puasa
        if (cshalat == 1) {
            cbshalat.setChecked(true);
        }

        if (cpuasa == 1) {
            cbpuasa.setChecked(true);
        }
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
                Integer Rqadha = cursor.getInt(5);
                Log.d("Database Check", "Tanggal: " + tanggal + ", Jenis: " + jenisMark+ ", Qshalat: " + Qshalat+ ", sudah sholat: " + Rshalat+ ", nambah hutang puasa ga: " + Rpuasa+" tampil "+Rqadha);
            }
        } else {
            Log.d("Database Check", "Tidak ada data di tabel.");
        }
        cursor.close();
    }
}