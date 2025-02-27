package com.galuhsukma.kalendernya;

import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_PUASA;
import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_REMINDER;
import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_UTAMA;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
                // Pastikan DatabaseHelper telah diinisialisasi
                if (myDb == null) {
                    myDb = new DatabaseHelper(Markday.this);
                }

                // Ambil nilai checkbox saat tombol ditekan
                cshalat = cbshalat.isChecked() ? 1 : 0;
                cpuasa = cbpuasa.isChecked() ? 1 : 0;

                if ("ISTIHADHAH".equals(jmark)) {
                    qshalat = "Tidak Ada";
                    cshalat = 0;
                }

                if ("HAID".equals(jmark) && !"Tidak Ada".equals(qshalat)) {
                    SQLiteDatabase db = myDb.getWritableDatabase();
                    Cursor cursor2 = db.rawQuery(
                            "SELECT display FROM " + DB_TABLE_UTAMA + " WHERE display = 1", null);
                    if (cursor2 != null) {
                        if (cursor2.moveToFirst()) {
                            int qdisplay = cursor2.getInt(cursor2.getColumnIndexOrThrow("display"));
                            // Gunakan nilai qdisplay sesuai kebutuhan
                        }
                        cursor2.close();
                    }

                    db.execSQL("UPDATE " + DB_TABLE_UTAMA + " SET display = 0 WHERE display = 1");

                    ContentValues cv = new ContentValues();
                    cv.put("display", 0);
                    db.update(DB_TABLE_UTAMA, cv, "display = ?", new String[]{"1"});
                    db.close();
                }

                // Cek apakah `tgl` dan `jmark` sudah dipilih
                if (tgl == null || jmark == null || jmark.isEmpty()) {
                    Toast.makeText(Markday.this, "Silakan pilih jenis mark terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Jika `cpuasa` tidak dicentang (0), hapus data di tabel `puasa`
                if (cpuasa == 0) {
                    Log.d("CHECK", "Masuk ke if cpuasa == 0");
                    int deleteResult = deleteDataPuasa(tgl);
                    if (deleteResult > 0) {
                        Log.d("puasa diapus", "berhasil");
                    } else {
                        Log.d("puasa gaada", "tidak apa-apa");
                    }
                    kurangiHariPuasaUTAMA();
                }

                if (cpuasa == 1) {
                    Log.d("CHECK", "Masuk ke if cpuasa == 1");
                    long insertResult = insertDataPuasa(tgl);
                    if (insertResult > 0) {
                        Log.d("puasa diapus", "berhasil");
                    } else {
                        Log.d("puasa gaada", "tidak apa-apa");
                    }
                    tambahHariPuasaUTAMA();
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

    public int deleteDataPuasa(String tgl) {
        SQLiteDatabase db = myDb.getWritableDatabase();
        int rowsDeleted = 0;
        try {
            rowsDeleted = db.delete(DB_TABLE_PUASA, "sumber = ?", new String[]{tgl});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return rowsDeleted;
    }
    public void kurangiHariPuasaUTAMA() {
        SQLiteDatabase db = myDb.getWritableDatabase();

        try {
            // Ambil nilai `haripuasa` pada sumber = 'UTAMA'
            Cursor cursor = db.rawQuery("SELECT haripuasa FROM " + DB_TABLE_PUASA + " WHERE sumber = 'UTAMA'", null);
            if (cursor != null && cursor.moveToFirst()) {
                int currentHaripuasa = cursor.getInt(cursor.getColumnIndexOrThrow("haripuasa"));
                cursor.close();

                // Jika haripuasa > 0, maka kurangi 1
                if (currentHaripuasa > 0) {
                    ContentValues cv = new ContentValues();
                    cv.put("haripuasa", currentHaripuasa - 1);
                    db.update(DB_TABLE_PUASA, cv, "sumber = ?", new String[]{"UTAMA"});

                    Log.d("UPDATE", "haripuasa pada sumber 'UTAMA' dikurangi menjadi " + (currentHaripuasa - 1));
                } else {
                    Log.d("UPDATE", "haripuasa pada sumber 'UTAMA' sudah 0, tidak dikurangi");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
    public long insertDataPuasa(String tgl) {
        SQLiteDatabase db = myDb.getWritableDatabase();
        long rowId = -1;

        try {
            // Cek apakah data dengan sumber = tgl sudah ada
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DB_TABLE_PUASA + " WHERE sumber = ?", new String[]{tgl});
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();

            // Jika data belum ada, maka insert
            if (count == 0) {
                ContentValues values = new ContentValues();
                values.put("sumber", tgl);
                values.put("haripuasa", 1); // Set haripuasa menjadi 1

                rowId = db.insert(DB_TABLE_PUASA, null, values);

                if (rowId != -1) {
                    Log.d("INSERT", "Data puasa dengan sumber = " + tgl + " berhasil dimasukkan");
                } else {
                    Log.d("INSERT", "Gagal memasukkan data puasa untuk sumber = " + tgl);
                }
            } else {
                Log.d("INSERT", "Data puasa dengan sumber = " + tgl + " sudah ada, tidak perlu insert");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return rowId;
    }

    public void tambahHariPuasaUTAMA() {
        SQLiteDatabase db = myDb.getWritableDatabase();

        try {
            // Ambil nilai `haripuasa` pada sumber = 'UTAMA'
            Cursor cursor = db.rawQuery("SELECT haripuasa FROM " + DB_TABLE_PUASA + " WHERE sumber = 'UTAMA'", null);
            if (cursor != null && cursor.moveToFirst()) {
                int currentHaripuasa = cursor.getInt(cursor.getColumnIndexOrThrow("haripuasa"));
                cursor.close();

                // Tambah haripuasa +1
                ContentValues cv = new ContentValues();
                cv.put("haripuasa", currentHaripuasa + 1);
                db.update(DB_TABLE_PUASA, cv, "sumber = ?", new String[]{"UTAMA"});

                Log.d("UPDATE", "haripuasa pada sumber 'UTAMA' ditambah menjadi " + (currentHaripuasa + 1));
            } else {
                Log.d("UPDATE", "Data puasa dengan sumber 'UTAMA' tidak ditemukan, tidak ada perubahan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    private void fetchdata() {
        Cursor cursor = myDb.getReadableDatabase().rawQuery("select * from "+DB_TABLE_UTAMA+" where id_tgl ='"+tgl+"';", null);
        if (cursor != null && cursor.moveToFirst()) {
            // Misalnya, jika urutan kolom adalah: id_tgl, jmark, qshalat, cshalat, cpuasa
            tgl = cursor.getString(cursor.getColumnIndexOrThrow("id_tgl"));
            jmark = cursor.getString(cursor.getColumnIndexOrThrow("jenismark"));
            qshalat = cursor.getString(cursor.getColumnIndexOrThrow("waktushalat"));
            cshalat = cursor.getInt(cursor.getColumnIndexOrThrow("centangshalat"));
            cpuasa = cursor.getInt(cursor.getColumnIndexOrThrow("centangpuasa"));
            qdisplay = cursor.getInt(cursor.getColumnIndexOrThrow("display"));
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
        Cursor cursor2 = myDb.getReadableDatabase().rawQuery("SELECT * FROM " + DB_TABLE_PUASA, null);
        if (cursor2.getCount() > 0) {
            while (cursor2.moveToNext()) {
                String sumber = cursor2.getString(0);
                int hari = cursor2.getInt(1);
                Log.d("Database Check puasa ", "sumber: " + sumber + ", Jenis: " + hari);
            }
        } else {
            Log.d("Database Check puasa", "Tidak ada data di tabel.");
        }
        cursor2.close();
    }
}