package com.galuhsukma.kalendernya;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorker extends Worker {
    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Ambil data dari WorkManager
        String title = getInputData().getString("title");
        String message = getInputData().getString("message");

        // Tampilkan notifikasi
        ReminderNotification.showNotification(getApplicationContext(), title, message);

        return Result.success();
    }
}
