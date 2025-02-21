package com.galuhsukma.kalendernya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Markday extends AppCompatActivity {
    private TextView dayselected;
    private String selected_date_string, selected_monthYear_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_markday);
        Intent intent = getIntent();
        selected_date_string = intent.getStringExtra("chosenDay");
        selected_monthYear_string = intent.getStringExtra("monthyear");
        dayselected = findViewById(R.id.dayselected);
        dayselected.setText(selected_date_string+" "+selected_monthYear_string);
    }

    public void kembali(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }

}