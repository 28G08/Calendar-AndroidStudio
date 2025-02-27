package com.galuhsukma.kalendernya;

import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_REMINDER;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TambahReminder extends AppCompatActivity {
    String jenisreminder, idtgl;
    CardView cvrpuasa, cvrshalat;
    CalendarView kalenderreminder;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahreminder);

        cvrpuasa = findViewById(R.id.cvrpuasa);
        cvrshalat = findViewById(R.id.cvrshalat);
        kalenderreminder = findViewById(R.id.kalenderreminder);
        myDb = new DatabaseHelper(this);

        // Ambil data dari intent (jika ada)
        Intent intent = getIntent();
        jenisreminder = intent.getStringExtra("jenisreminder");
        idtgl = intent.getStringExtra("tglreminder");

        // Jika tidak ada intent, gunakan tanggal hari ini
        if (idtgl == null || idtgl.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            idtgl = sdf.format(new Date());  // Set default tanggal hari ini
        }

        // Set tanggal default di CalendarView
        setCalendarDate(idtgl);

        // Atur klik pada card view
        cvrpuasa.setOnClickListener(view -> {
            jenisreminder = "QADHA' PUASA";
            cvrpuasa.setCardBackgroundColor(Color.YELLOW);
            cvrshalat.setCardBackgroundColor(Color.WHITE);
        });

        cvrshalat.setOnClickListener(view -> {
            jenisreminder = "QADHA' SHALAT";
            cvrshalat.setCardBackgroundColor(Color.YELLOW);
            cvrpuasa.setCardBackgroundColor(Color.WHITE);
        });

        kalenderreminder.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            idtgl = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            Log.d("DEBUG", "Tanggal dipilih: " + idtgl);
        });


        // Panggil fetching data setelah intent diterima
        fetchingdatareminder();
    }
    private void setCalendarDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Log.d("DEBUG", "Parsing date: " + dateString);
            Date date = sdf.parse(dateString);
            if (date != null) {
                Log.d("DEBUG", "Setting CalendarView date: " + date.getTime());
                kalenderreminder.setDate(date.getTime());  // Set tanggal di CalendarView
            }
        } catch (ParseException e) {
            Log.e("ERROR", "Gagal mengonversi tanggal: " + e.getMessage());
        }
    }


    private void fetchingdatareminder() {
        Cursor cursor = myDb.getReadableDatabase().rawQuery("SELECT * FROM " + DB_TABLE_REMINDER + " WHERE tgl ='" + idtgl + "';", null);

        if (cursor != null && cursor.moveToFirst()) {
            // Ambil data dari database
            idtgl = cursor.getString(cursor.getColumnIndexOrThrow("tgl"));
            jenisreminder = cursor.getString(cursor.getColumnIndexOrThrow("jenisreminder"));

            // Set pilihan berdasarkan jenis reminder
            if ("QADHA' PUASA".equals(jenisreminder)) {
                cvrpuasa.callOnClick();
            } else if ("QADHA' SHALAT".equals(jenisreminder)) {
                cvrshalat.callOnClick();
            }

            // Set tanggal di CalendarView
            setCalendarDate(idtgl);
        }

        if (cursor != null) {
            cursor.close();  // Tutup cursor setelah digunakan
        }
    }

    public void simpanreminder(View view) {
        // Cek apakah `tgl` dan `jenisreminder` sudah dipilih
        if (idtgl == null || jenisreminder == null || jenisreminder.isEmpty()) {
            Toast.makeText(this, "Silakan pilih jenis reminder terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Coba update data dulu
            int result = myDb.updateDataReminder(idtgl, jenisreminder, getApplicationContext());

            if (result > 0) {
                Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show();
                Log.d("DatabaseHelper", "Reminder diperbarui: " + idtgl + " - " + jenisreminder);
            } else {
                // Jika update gagal (misalnya record tidak ada), lakukan insert
                long insertResult = myDb.insertDataReminder(idtgl, jenisreminder, getApplicationContext());

                if (insertResult != -1) {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    Log.d("DatabaseHelper", "Reminder berhasil disimpan: " + idtgl + " - " + jenisreminder);
                } else {
                    Toast.makeText(this, "Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
                    Log.e("DatabaseHelper", "Insert reminder gagal!");
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Terjadi kesalahan saat menyimpan data", Toast.LENGTH_SHORT).show();
            Log.e("ERROR", "Gagal menyimpan reminder: " + e.getMessage());
        }

        // Pindah kembali ke ReminderActivity
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCalendarDate(idtgl);
    }

    public void kembali(View view) {
        onBackPressed();
    }
}
