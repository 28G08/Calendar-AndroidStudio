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
    public static final int DB_VER = 2;
    public static final String CREATE_TABLE_UTAMA =
            "CREATE TABLE " + DB_TABLE_UTAMA + " (" +
                    "id_tgl TEXT PRIMARY KEY, " +
                    "jenismark TEXT, " +
                    "waktushalat TEXT, " +
                    "centangshalat INTEGER, " +
                    "centangpuasa INTEGER);";
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

    public Long insertData(String tgl, String jmark, String qshalat, int cshalat, int cpuasa) {
        SQLiteDatabase db = this.getWritableDatabase();
        long rowId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("id_tgl", tgl);
            values.put("jenismark", jmark);
            values.put("waktushalat", qshalat);
            values.put("centangshalat", cshalat);
            values.put("centangpuasa", cpuasa);

            // Insert ke tabel utama
            rowId = db.insert(DB_TABLE_UTAMA, null, values);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Tutup database setelah insert selesai
        }

        return rowId;
    }


    public void editDataEntry(String tgl, String jmark, String qshalat, int cshalat, int cpuasa){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_tgl", tgl);
        values.put("jenismark", jmark);
        values.put("waktushalat", qshalat);
        values.put("centangshalat", cshalat);
        values.put("centangpuasa", cpuasa);

        db.update(DB_TABLE_UTAMA, values, "id_tgl=?", new String[]{tgl});
        db.close();
    }
}
