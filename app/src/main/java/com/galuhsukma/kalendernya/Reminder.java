package com.galuhsukma.kalendernya;

import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_UTAMA;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class Reminder extends AppCompatActivity {
    private TextView qadhashalat, qadhapuasa;
    String infoshalat;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reminder);
        qadhashalat = findViewById(R.id.qshalatinfo);
        qadhapuasa = findViewById(R.id.qpuasainfo);

        myDb = new DatabaseHelper(this);

        ambildatabase();
    }

    private void ambildatabase() {
        Cursor cursor = myDb.getReadableDatabase().rawQuery("Select waktushalat from "+DB_TABLE_UTAMA+" WHERE display = 1;", null);
        if (cursor.moveToFirst()) {
            infoshalat = cursor.getString(cursor.getColumnIndex("waktushalat"));
        }
        qadhashalat.setText(infoshalat);
    }

    public void kembali(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }
}
