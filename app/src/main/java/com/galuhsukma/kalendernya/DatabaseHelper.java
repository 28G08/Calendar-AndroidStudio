package com.galuhsukma.kalendernya;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "HaIs";
    public static final String DB_TABLE_UTAMA = "AllMarkDay";
    public static final String DB_TABLE_PUASA = "AkumulasiHutangPuasa";
    public static final String DB_TABLE_ISTIHADHAH = "IstihadhahTable";
    public static final int DB_VER = 5;
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

    public static final String CREATE_TABLE_ISTIHADHAH = "CREATE TABLE " + DB_TABLE_ISTIHADHAH + " ("
            + "id_tgl TEXT PRIMARY KEY, "
            + "jenismark TEXT NOT NULL CHECK(jenismark = 'ISTIHADHAH'))";
    Context ctx;

    public DatabaseHelper(Context ct){
        super(ct, DB_NAME, null, DB_VER);
        ctx = ct;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_UTAMA);
        db.execSQL(CREATE_TABLE_PUASA);
        db.execSQL(CREATE_TABLE_ISTIHADHAH);
        Log.i("Database", "All Tables Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_UTAMA);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_PUASA);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_ISTIHADHAH);
        onCreate(db); // Buat ulang tabel
    }

    public Long insertData(String tgl, String jmark, String qshalat, int cshalat, int cpuasa, int qdisplay) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("id_tgl", tgl);
            values.put("jenismark", jmark);
            values.put("waktushalat", qshalat);
            values.put("centangshalat", cshalat);
            values.put("centangpuasa", cpuasa);
            values.put("display", qdisplay);

            // Insert ke tabel utama
            rowId = db.insert(DB_TABLE_UTAMA, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Tutup database setelah insert selesai
        }

        return rowId;
    }

    public int updateData(String id_tgl, String jenismark, String waktushalat, int centangshalat, int centangpuasa, int qdisplay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("jenismark", jenismark);
        cv.put("waktushalat", waktushalat);
        cv.put("centangshalat", centangshalat);
        cv.put("centangpuasa", centangpuasa);
        cv.put("display", qdisplay);

        // update record yang memiliki id_tgl yang sesuai
        return db.update(DB_TABLE_UTAMA, cv, "id_tgl = ?", new String[]{id_tgl});
    }

    public int deleteData(String id_tgl) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Menghapus data berdasarkan id_tgl
        return db.delete(DB_TABLE_UTAMA, "id_tgl = ?", new String[]{id_tgl});
    }
}
