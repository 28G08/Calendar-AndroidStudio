package com.galuhsukma.kalendernya;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "ReminderChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ReminderReceiver", "ReminderReceiver triggered!");
        DatabaseHelper myDb = new DatabaseHelper(context);
        SQLiteDatabase db = myDb.getReadableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = sdf.format(Calendar.getInstance().getTime());

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.DB_TABLE_REMINDER + " WHERE tgl = ?", new String[]{todayDate});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String jenisReminder = cursor.getString(cursor.getColumnIndexOrThrow("jenisreminder"));
                    Log.d("ReminderReceiver", "Found reminder: " + jenisReminder);
                    showNotification(context, todayDate, jenisReminder);
                } while (cursor.moveToNext());
            } else {
                Log.d("ReminderReceiver", "No reminders found for today.");
            }
        } catch (Exception e) {
            Log.e("ReminderReceiver", "Error reading database", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close(); // **WAJIB Ditutup**
        }
    }



    private void showNotification(Context context, String date, String jenisReminder) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Buat Notification Channel untuk Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Reminder Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        // Intent untuk membuka aplikasi saat notifikasi diklik
        Intent intent = new Intent(context, Reminder.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Bangun notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.mayoi) // Ganti dengan ikon notifikasi
                .setContentTitle("Reminder: " + jenisReminder)
                .setContentText("Jadwal untuk " + date)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Tampilkan notifikasi
        notificationManager.notify(date.hashCode(), builder.build());
    }
}
