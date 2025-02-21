package com.galuhsukma.kalendernya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Markday extends AppCompatActivity {
    DatabaseHelper myDb;
    private TextView dayselected;
    private String selected_date_string, selected_monthYear_string, tgl_database;
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
        simpanbtn = findViewById(R.id.btnsimpan);

        dayselected.setText(selected_date_string+" "+selected_monthYear_string);

        simpanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDb.insertData(tgl_database);
                Toast.makeText(Markday.this,"Data Saved Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void kembali(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }

}