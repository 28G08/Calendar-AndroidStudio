package com.galuhsukma.kalendernya;

import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_UTAMA;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import java.util.List;

public class Reminder extends AppCompatActivity {
    private TextView qadhashalat, qadhapuasa;
    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reminder);
        qadhashalat = findViewById(R.id.qshalatinfo);
        qadhapuasa = findViewById(R.id.qpuasainfo);

        myDb = new DatabaseHelper(this);

        ambildatabase();

        recyclerView = findViewById(R.id.reminderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myDb = new DatabaseHelper(this);
        List<ModelReminder> modelReminderList = myDb.getAllReminders();

        reminderAdapter = new ReminderAdapter(this, modelReminderList);
        recyclerView.setAdapter(reminderAdapter);
    }

    private void ambildatabase() {
        qadhashalat.setText("Tidak Ada");
        // Dapatkan instance database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

// Query untuk mengambil data dengan kondisi display = 1
        String query2 = "SELECT waktushalat FROM " + DB_TABLE_UTAMA + " WHERE display = 1;";
        Cursor cursor2 = db.rawQuery(query2, null);

// Jika ada data, update infoqshalat
        if (cursor2.moveToFirst()) {
            String waktushalat = cursor2.getString(cursor2.getColumnIndex("waktushalat"));
            if (waktushalat != null && !waktushalat.isEmpty()) {
                qadhashalat.setText(waktushalat);
            }
        }
        cursor2.close();
    }

    public void kembali(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }

    public void clearshalat(View view) {
        myDb.getWritableDatabase().execSQL(
                "UPDATE " + DB_TABLE_UTAMA + " SET display = 0 WHERE display = 1");

        ContentValues cv = new ContentValues();
        cv.put("display", 0);
        myDb.getWritableDatabase().update(DB_TABLE_UTAMA, cv, "display = ?", new String[]{"1"});
        qadhashalat.setText("Tidak Ada");
    }

    public void tambahreminderbtn(View view) {
        Intent intent = new Intent(this, TambahReminder.class);
        startActivity(intent);
    }
}
