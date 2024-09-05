package com.galuhsukma.kalendernya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MarkdayActivity extends AppCompatActivity {
    String chosenDay;
    TextView day;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.markday);
        Intent intent = getIntent();
        chosenDay = intent.getStringExtra("chosenDay");
        day = findViewById(R.id.display_ChosenDay);
        day.setText(chosenDay);

    }

    public void back(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }
}
