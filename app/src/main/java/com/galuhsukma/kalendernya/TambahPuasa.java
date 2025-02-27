package com.galuhsukma.kalendernya;

import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_PUASA;
import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_UTAMA;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TambahPuasa extends AppCompatActivity {

    TextView totalpuasa;
    Button btntambah, btnkurang;
    int datapuasaawal = 0, datapuasaakhir = 0; // Inisialisasi awal
    String textview;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahpuasa);

        // Inisialisasi database helper
        myDb = new DatabaseHelper(this);

        // Inisialisasi UI
        totalpuasa = findViewById(R.id.displaypuasa);
        btntambah = findViewById(R.id.puasatambah);
        btnkurang = findViewById(R.id.puasakurang);

        ambildatautama(); // Ambil data awal dari database dan perbarui totalpuasa

        btntambah.setOnClickListener(view -> {
            textview = totalpuasa.getText().toString();
            int pertambahan = Integer.parseInt(textview);

            totalpuasa.setText(String.valueOf(pertambahan+1)); // Konversi ke string
            String converter = totalpuasa.getText().toString();
            datapuasaakhir = Integer.parseInt(converter);
        });

        btnkurang.setOnClickListener(view -> {
            textview = totalpuasa.getText().toString();
            int pengurangan = Integer.parseInt(textview);
            if (pengurangan > 0) { // Cegah nilai negatif
                totalpuasa.setText(String.valueOf(pengurangan-1)); // Konversi ke string
                String converter = totalpuasa.getText().toString();
                datapuasaakhir = Integer.parseInt(converter);
            } else {
                Toast.makeText(this, "Jumlah puasa tidak bisa negatif!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ambildatautama() {
        SQLiteDatabase db = myDb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT haripuasa FROM " + DB_TABLE_PUASA + " WHERE sumber = 'UTAMA';", null);
        if (cursor != null && cursor.moveToFirst()) {
            datapuasaawal = cursor.getInt(cursor.getColumnIndexOrThrow("haripuasa"));
            totalpuasa.setText(String.valueOf(datapuasaawal)); // Set ke UI setelah diambil
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close(); // Tutup database setelah membaca data
    }

    public void kembali(View view) {
        onBackPressed();
    }

    public void simpanpuasa(View view) {
        SQLiteDatabase db = myDb.getWritableDatabase();

        try {
            db.beginTransaction(); // Mulai transaksi agar perubahan aman

            // Update menggunakan ContentValues
            ContentValues cv = new ContentValues();
            cv.put("haripuasa", datapuasaakhir);
            int rowsUpdated = db.update(DB_TABLE_PUASA, cv, "sumber = ?", new String[]{"UTAMA"});

            if (rowsUpdated > 0) { // Pastikan data berhasil diperbarui
                db.setTransactionSuccessful(); // Tandai transaksi sukses
                Toast.makeText(this, "Data puasa berhasil disimpan!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal menyimpan data puasa!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Commit atau rollback jika gagal
            db.close();
        }
    }

    public void hapuspuasa(View view) {
        SQLiteDatabase db = myDb.getWritableDatabase();

        try {
            db.beginTransaction(); // Mulai transaksi agar perubahan aman

            // Update haripuasa menjadi 0
            ContentValues cv = new ContentValues();
            cv.put("haripuasa", 0);
            int rowsUpdated = db.update(DB_TABLE_PUASA, cv, "sumber = ?", new String[]{"UTAMA"});

            if (rowsUpdated > 0) { // Pastikan data berhasil diperbarui
                db.setTransactionSuccessful(); // Tandai transaksi sukses

                // Set nilai totalpuasa jadi 0 di UI
                runOnUiThread(() -> totalpuasa.setText(String.valueOf(0)));

                Toast.makeText(this, "Data berhasil direset!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal mereset data!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Commit atau rollback jika gagal
            db.close();
        }
    }
}
