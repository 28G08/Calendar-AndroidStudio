package com.galuhsukma.kalendernya;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Wawasan extends AppCompatActivity {
    LinearLayout haidll, istihadhahll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wawasan);

        haidll = findViewById(R.id.haidsection);
        istihadhahll = findViewById(R.id.istihadhahsection);

    }
    public void haidwawasanbtn(View view) {
        haidll.setVisibility(VISIBLE);
        istihadhahll.setVisibility(GONE);
    }

    public void istihadhahwawasanbtn(View view) {
        istihadhahll.setVisibility(VISIBLE);
        haidll.setVisibility(GONE);
    }

    public void kembaliwawasan(View view) {
        onBackPressed();
    }
}
