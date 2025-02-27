package com.galuhsukma.kalendernya;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderWorker extends Worker {
    private static final String CHANNEL_ID = "ReminderChannel";

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {
        Log.d("ReminderWorker", "doWork() called!");
        Context context = getApplicationContext();

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
                    Log.d("ReminderWorker", "Found reminder: " + jenisReminder);
                    // **Ambil keterangan tambahan berdasarkan jenis reminder**
                    String keterangan = getKeterangan(context, jenisReminder);
                    showNotification(context, todayDate, jenisReminder, keterangan);
                } while (cursor.moveToNext());
            } else {
                Log.d("ReminderWorker", "No reminders found for today.");
            }
        } catch (Exception e) {
            Log.e("ReminderWorker", "Error reading database", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close(); // **WAJIB Ditutup**
        }

        return Result.success();
    }

    private String getKeterangan(Context context, String jenisReminder) {
        DatabaseHelper myDb = new DatabaseHelper(context);
        SQLiteDatabase db = myDb.getReadableDatabase();
        String keterangan = "";

        if (jenisReminder.equalsIgnoreCase("QADHA' PUASA")) {
            // Ambil total puasa dari tabel utama
            Cursor cursor = db.rawQuery("SELECT haripuasa FROM " + DatabaseHelper.DB_TABLE_PUASA+" where sumber = 'UTAMA';", null);
            if (cursor != null && cursor.moveToFirst()) {
                int totalPuasa = cursor.getInt(0);
                keterangan = "Sisa Puasa: " + totalPuasa + " hari";
            }
            if (cursor != null) cursor.close();
        } else if (jenisReminder.equalsIgnoreCase("QADHA' SHALAT")) {
            // Ambil waktu shalat dari tabel utama yang display = 1
            Cursor cursor = db.rawQuery("SELECT waktushalat FROM " + DatabaseHelper.DB_TABLE_UTAMA + " WHERE display = 1", null);
            if (cursor != null && cursor.moveToFirst()) {
                String shalat = cursor.getString(0);
                keterangan = "Waktu Shalat: " + shalat;
            }
            if (cursor != null) cursor.close();
        }

        db.close();
        return keterangan;
    }



    public static void showNotification(Context context, String date, String jenisReminder, String keterangan) {
        Log.d("ReminderWorker", "showNotification() dipanggil untuk: " + jenisReminder + " pada " + date + " | Keterangan: " + keterangan);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Reminder Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            channel.setDescription("Notifikasi pengingat jadwal");
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, Reminder.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.mayoi) // Pastikan ikon ini ada
                .setContentTitle("Reminder: " + jenisReminder)
                .setContentText("Hari ini: " + date + "\n" + keterangan)
                .setContentIntent(pendingIntent)
                .setOngoing(true) // 🔥 Membuat notifikasi tidak bisa dihapus
                .setAutoCancel(false) // 🔥 Pastikan notifikasi tidak hilang sendiri
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM) // 🔥 Memberi tahu sistem bahwa ini penting
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE);// 🔥 WAJIB untuk Android 12+

        notificationManager.notify(date.hashCode(), builder.build()); // 🔥 Pakai ID tetap supaya tidak duplikasi
        Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.startForegroundService(new Intent(context, ReminderService.class));
        }

    }





}
