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
    public TextView dayselected;
    public String selected_date_string, selected_monthYear_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_markday);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dayselected = findViewById(R.id.dayselected);
        Intent intent = getIntent();
        selected_date_string = intent.getStringExtra("selected_date");
        selected_monthYear_string = intent.getStringExtra("selected_monthYear");
        selectedDate();
    }

    private void selectedDate() {
        dayselected.setText(selected_date_string+" "+selected_monthYear_string);
    }

    public void kembali(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }
}