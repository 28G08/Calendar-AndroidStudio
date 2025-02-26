package com.galuhsukma.kalendernya;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmScheduler {
    public static void scheduleRepeatingAlarm(Context context) {
        Log.d("AlarmScheduler", "scheduleRepeatingAlarm() called!");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        long interval = 30 * 60 * 1000; // Setiap 30 menit

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
            Log.d("AlarmScheduler", "AlarmManager berhasil dijadwalkan!");
        } else {
            Log.e("AlarmScheduler", "AlarmManager adalah null!");
        }
    }
}


