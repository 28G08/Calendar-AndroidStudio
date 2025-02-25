package com.galuhsukma.kalendernya;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "reminder")
public class ReminderEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String message;
    public long timestamp; // Waktu dalam millis

    public ReminderEntity(String title, String message, long timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }
}
