package com.galuhsukma.kalendernya;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "HaIs";
    public static final String DB_TABLE_UTAMA = "AllMarkDay";
    public static final String DB_TABLE_PUASA = "AkumulasiHutangPuasa";
    public static final String DB_TABLE_REMINDER = "REMINDERQADHA";
    public static final int DB_VER = 9;
    public static final String CREATE_TABLE_UTAMA =
            "CREATE TABLE " + DB_TABLE_UTAMA + " (" +
                    "id_tgl TEXT PRIMARY KEY, " +
                    "jenismark TEXT, " +
                    "waktushalat TEXT, " +
                    "centangshalat INTEGER, " +
                    "centangpuasa INTEGER, " +
                    "display INTEGER);";
    public static final String CREATE_TABLE_PUASA = "CREATE TABLE " + DB_TABLE_PUASA + " ("
            + "sumber TEXT PRIMARY KEY, "
            + "haripuasa INTEGER);";

    public static final String CREATE_TABLE_REMINDER = "CREATE TABLE " + DB_TABLE_REMINDER + " ("
            + "tgl TEXT PRIMARY KEY, "
            + "jenisreminder TEXT NOT NULL);";

    // Masukkan data awal ke tabel puasa dengan haripuasa = 0 dan sumber = "utama"
    public static final String insertDefaultData = "INSERT INTO "+DB_TABLE_PUASA+" (sumber, haripuasa) VALUES ('UTAMA', 0);";
    Context ctx;

    public DatabaseHelper(Context ct){
        super(ct, DB_NAME, null, DB_VER);
        ctx = ct;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_UTAMA);
        db.execSQL(CREATE_TABLE_PUASA);
        db.execSQL(CREATE_TABLE_REMINDER);
        db.execSQL(insertDefaultData);
        Log.i("Database", "All Tables Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_UTAMA);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_PUASA);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_REMINDER);
        onCreate(db); // Buat ulang tabel
    }
    public Long insertData(String tgl, String jmark, String qshalat, int cshalat, int cpuasa, int qdisplay) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = -1;

        try {
            db.beginTransaction(); // Mulai transaksi

            // Insert ke tabel utama
            ContentValues values = new ContentValues();
            values.put("id_tgl", tgl);
            values.put("jenismark", jmark);
            values.put("waktushalat", qshalat);
            values.put("centangshalat", cshalat);
            values.put("centangpuasa", cpuasa);
            values.put("display", qdisplay);

            rowId = db.insert(DB_TABLE_UTAMA, null, values);

            db.setTransactionSuccessful(); // Commit transaksi

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Selesaikan transaksi (commit jika sukses, rollback jika gagal)
            db.close(); // Tutup database
        }

        return rowId;
    }

    public int updateData(String id_tgl, String jenismark, String waktushalat, int centangshalat, int centangpuasa, int qdisplay) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = 0;

        try {
            db.beginTransaction(); // Mulai transaksi

            ContentValues cv = new ContentValues();
            cv.put("jenismark", jenismark);
            cv.put("waktushalat", waktushalat);
            cv.put("centangshalat", centangshalat);
            cv.put("centangpuasa", centangpuasa);
            cv.put("display", qdisplay);

            // Update tabel utama
            rowsAffected = db.update(DB_TABLE_UTAMA, cv, "id_tgl = ?", new String[]{id_tgl});

            if (rowsAffected > 0) { // Jika ada perubahan di tabel utama, update juga tabel puasa
                ContentValues puasaValues = new ContentValues();
                puasaValues.put("haripuasa", centangpuasa);

                db.update(DB_TABLE_PUASA, puasaValues, "sumber = ?", new String[]{id_tgl});
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); // Commit atau rollback transaksi
            db.close();
        }

        return rowsAffected;
    }


    public int deleteData(String id_tgl) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = 0;
        Cursor cursor = null;

        try {
            db.beginTransaction(); // Mulai transaksi

            // Hapus dari tabel utama
            rowsDeleted = db.delete(DB_TABLE_UTAMA, "id_tgl = ?", new String[]{id_tgl});

            if (rowsDeleted > 0) { // Jika data dihapus dari tabel utama, hapus juga dari tabel puasa
                db.delete(DB_TABLE_PUASA, "sumber = ?", new String[]{id_tgl});

                // Ambil nilai `haripuasa` pada sumber = 'UTAMA'
                cursor = db.rawQuery("SELECT haripuasa FROM " + DB_TABLE_PUASA + " WHERE sumber = 'UTAMA'", null);

                if (cursor != null && cursor.moveToFirst()) {
                    int currentHaripuasa = cursor.getInt(cursor.getColumnIndexOrThrow("haripuasa"));

                    // Jika haripuasa > 0, maka kurangi 1
                    if (currentHaripuasa > 0) {
                        ContentValues cv = new ContentValues();
                        cv.put("haripuasa", currentHaripuasa - 1);
                        db.update(DB_TABLE_PUASA, cv, "sumber = ?", new String[]{"UTAMA"});

                        Log.d("UPDATE", "haripuasa pada sumber 'UTAMA' dikurangi menjadi " + (currentHaripuasa - 1));
                    } else {
                        Log.d("UPDATE", "haripuasa pada sumber 'UTAMA' sudah 0, tidak dikurangi");
                    }
                } else {
                    Log.d("UPDATE", "Tidak ada data dengan sumber 'UTAMA'");
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close(); // Tutup cursor untuk mencegah memory leak
            }
            db.endTransaction(); // Commit atau rollback transaksi
            db.close(); // Tutup database setelah transaksi selesai
        }

        return rowsDeleted;
    }




    public Long insertDataReminder(String idtgl, String jenisreminder, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("tgl", idtgl);
            values.put("jenisreminder", jenisreminder);

            // **Perbaikan di sini**
            rowId = db.insert(DB_TABLE_REMINDER, null, values); // <-- Sekarang rowId berisi hasil insert

            if (rowId != -1) { // <-- Sekarang bisa dicek dengan benar
                Log.d("DatabaseHelper", "Reminder berhasil disimpan: " + idtgl + " - " + jenisreminder);

                // **Langsung panggil showNotification()**
                ReminderWorker.showNotification(context, idtgl, jenisreminder, "Jangan lupa jadwalmu!");
            } else {
                Log.e("DatabaseHelper", "Gagal menyimpan reminder: " + idtgl + " - " + jenisreminder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // **Tutup database setelah insert selesai**
        }

        return rowId;
    }


    public List<ModelReminder> getAllReminders() {
        List<ModelReminder> modelReminderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE_REMINDER, null);

        if (cursor.moveToFirst()) {
            do {
                String jenisReminder = cursor.getString(cursor.getColumnIndexOrThrow("jenisreminder"));
                String tglReminder = cursor.getString(cursor.getColumnIndexOrThrow("tgl"));
                modelReminderList.add(new ModelReminder(jenisReminder, tglReminder));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return modelReminderList;
    }
    public int updateDataReminder(String idtgl, String jenisreminder, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsUpdated = 0;

        try {
            ContentValues cv = new ContentValues();
            cv.put("tgl", idtgl);
            cv.put("jenisreminder", jenisreminder);

            rowsUpdated = db.update(DB_TABLE_REMINDER, cv, "tgl = ?", new String[]{idtgl});

            if (rowsUpdated > 0) {
                Log.d("DatabaseHelper", "Reminder berhasil diperbarui: " + idtgl + " - " + jenisreminder);

                // Jalankan WorkManager langsung setelah update berhasil
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class).build();
                WorkManager.getInstance(context).enqueue(workRequest);
            } else {
                Log.e("DatabaseHelper", "Gagal memperbarui reminder: " + idtgl + " - " + jenisreminder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // **Tutup database setelah update selesai**
        }

        return rowsUpdated;
    }


    public void deleteReminder(String tanggal, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE_REMINDER, "tgl = ?", new String[]{tanggal});
        db.close();

        Log.d("DatabaseHelper", "Reminder dihapus: " + tanggal);

        // 🔥 Hapus Notifikasi Saat Data Reminder Dihapus
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(tanggal.hashCode()); // ID harus sama dengan yang dipakai di `showNotification()`
            Log.d("DatabaseHelper", "Notifikasi dihapus untuk reminder " + tanggal);
        }
    }


}
