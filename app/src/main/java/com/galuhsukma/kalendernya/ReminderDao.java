package com.galuhsukma.kalendernya;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ReminderDao {
    @Insert
    void insert(ReminderEntity reminder);

    @Query("SELECT * FROM reminder WHERE timestamp >= :currentTime ORDER BY timestamp ASC LIMIT 1")
    ReminderEntity getNextReminder(long currentTime);
}
