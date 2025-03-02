package com.galuhsukma.kalendernya;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_PUASA;
import static com.galuhsukma.kalendernya.DatabaseHelper.DB_TABLE_UTAMA;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    String chosenDay, tgldatabase;
    TextView infoqshalat, infoqpuasa;
    Button info;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        selectedDate = LocalDate.now();
        chosenDay = String.valueOf(selectedDate.getDayOfMonth());
        tgldatabase = monthYearFromSelectedDate()+"-"+chosenDay;
        initWidgets();
        setMonthView();
        Log.d("ini isi tgldatabase", "tanggal yang diambil"+tgldatabase);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMonthView();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        infoqshalat = findViewById(R.id.infoshalat);
        infoqpuasa = findViewById(R.id.infopuasa);
        info = findViewById(R.id.infobtn);
    }

    private void setMonthView() {
        infoqshalat.setText("Tidak Ada");
        // Dapatkan instance database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

// Query untuk mengambil data dengan kondisi display = 1
        String query2 = "SELECT waktushalat FROM " + DB_TABLE_UTAMA + " WHERE display = 1;";
        Cursor cursor2 = db.rawQuery(query2, null);

// Jika ada data, update infoqshalat
        if (cursor2.moveToFirst()) {
            String waktushalat = cursor2.getString(cursor2.getColumnIndexOrThrow("waktushalat"));
            if (waktushalat != null && !waktushalat.isEmpty()) {
                infoqshalat.setText(waktushalat);
            }
        }
        cursor2.close();

        infoqpuasa.setText("0");

        String query3 = "SELECT haripuasa FROM " + DB_TABLE_PUASA + " WHERE sumber = 'UTAMA';";
        Cursor cursor3 = db.rawQuery(query3, null);

        if (cursor3.moveToFirst()) {  // Perbaikan: Gunakan cursor3, bukan cursor2
            int haripuasa = cursor3.getInt(cursor3.getColumnIndexOrThrow("haripuasa")); // Perbaikan: Gunakan getInt()
            infoqpuasa.setText(String.valueOf(haripuasa)); // Perbaikan: Konversi int ke String sebelum setText()
        }
        cursor3.close();

        String query = "SELECT id_tgl, jenismark FROM "+DB_TABLE_UTAMA;
        Cursor cursor = db.rawQuery(query, null);

// Inisialisasi dua list untuk masing-masing jenis
        List<String> listHaid = new ArrayList<>();
        List<String> listIstihadhah = new ArrayList<>();

// Iterasi cursor untuk mengambil data
        if (cursor.moveToFirst()) {
            do {
                // Ambil nilai tanggal dan jenis
                String tanggal = cursor.getString(cursor.getColumnIndexOrThrow("id_tgl"));
                String jenis = cursor.getString(cursor.getColumnIndexOrThrow("jenismark"));

                // Kelompokkan data berdasarkan jenis
                if ("haid".equalsIgnoreCase(jenis)) {
                    listHaid.add(tanggal);
                } else if ("istihadhah".equalsIgnoreCase(jenis)) {
                    listIstihadhah.add(tanggal);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Tampilkan isi list di Logcat
        Log.d("Database", "List Haid: " + listHaid.toString());
        Log.d("Database", "List Istihadhah: " + listIstihadhah.toString());

        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        String currentMonthYear = monthYearFromSelectedDate();
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, listHaid, listIstihadhah, this, currentMonthYear);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++)
        {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                int dayOfMonth = i - dayOfWeek;
                LocalDate dateForCell = selectedDate.withDayOfMonth(dayOfMonth);
                daysInMonthArray.add(dateForCell.toString());
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("id", "ID"));
        String formattedDate = selectedDate.format(formatter);
        monthYearText.setText(formattedDate);
        return date.format(formatter);
    }
    private String monthYearFromSelectedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return selectedDate.format(formatter);
    }
    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }
    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        chosenDay = dayText.substring(dayText.lastIndexOf("-") + 1);
        tgldatabase = monthYearFromSelectedDate()+"-"+chosenDay;
        if (!dayText.isEmpty()){
            String message = "Selected Date "+ dayText;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    public void markday(View view) {
        Intent intent = new Intent(this, Markday.class);
        intent.putExtra("chosenDay", chosenDay);
        intent.putExtra("monthyear", monthYearFromDate(selectedDate));
        intent.putExtra("tgldatabase", tgldatabase);
        startActivity(intent);
    }

    public void reminder(View view) {
        Intent intent = new Intent(this, Reminder.class);
        startActivity(intent);
    }

    public void wawasan(View view) {
        Intent intent = new Intent(this, Wawasan.class);
        startActivity(intent);
    }

    public void tentang(View view) {
        Intent intent = new Intent(this, Tentang.class);
        startActivity(intent);
    }
}