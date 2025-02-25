package com.galuhsukma.kalendernya;


import androidx.room.Database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {ReminderEntity.class}, version = 1)
public abstract class ReminderDatabase extends RoomDatabase {
    private static volatile ReminderDatabase INSTANCE;

    public abstract ReminderDao reminderDao();

    public static ReminderDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ReminderDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ReminderDatabase.class, "reminder_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
