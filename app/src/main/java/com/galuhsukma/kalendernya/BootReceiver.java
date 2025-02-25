package com.galuhsukma.kalendernya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.WorkManager;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            WorkManager.getInstance(context).cancelAllWork();
            Intent mainActivity = new Intent(context, MainActivity.class);
            mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivity);
        }
    }
}
