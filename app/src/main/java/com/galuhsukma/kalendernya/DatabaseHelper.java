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
    public static final String DB_TABLE_HAID = "HaidTable";
    public static final String DB_TABLE_ISTIHADHAH = "IstihadhahTable";
    public static final int DB_VER = 1;
    public static final String CREATE_TABLE_UTAMA =
            "CREATE TABLE " + DB_TABLE_UTAMA + " (" +
                    "id_tgl TEXT PRIMARY KEY, " +
                    "jenismark TEXT, " +
                    "waktushalat TEXT, " +
                    "centangshalat INTEGER, " +
                    "centangpuasa INTEGER);";
    public static final String CREATE_TABLE_HAID = "CREATE TABLE " + DB_TABLE_HAID + " ("
            + "id_tgl STRING PRIMARY KEY, "
            + "jenismark TEXT NOT NULL CHECK(jenismark = 'HAID'))";

    public static final String CREATE_TABLE_ISTIHADHAH = "CREATE TABLE " + DB_TABLE_ISTIHADHAH + " ("
            + "id_tgl STRING PRIMARY KEY, "
            + "jenismark TEXT NOT NULL CHECK(jenismark = 'ISTIHADHAH'))";
    Context ctx;
    SQLiteDatabase myDB;

    public DatabaseHelper(Context ct){
        super(ct, DB_NAME, null, DB_VER);
        ctx = ct;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_UTAMA);
        db.execSQL(CREATE_TABLE_HAID);
        db.execSQL(CREATE_TABLE_ISTIHADHAH);
        Log.i("Database", "All Tables Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        String query = "drop table if exists "+DB_TABLE_UTAMA+"";
        db.execSQL(query);
    }

    public Long insertData(String tgl, String jmark, String qshalat, int cshalat, int cpuasa){
        myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_tgl", tgl);
        values.put("jenismark", jmark);
        values.put("waktusholat", qshalat);
        values.put("centangshalat", cshalat);
        values.put("centangpuasa", cpuasa);

        //tabel utama
        long rowId = myDB.insert(DB_TABLE_UTAMA, null, values);

        // Insert ke tabel HAID atau ISTIHADHAH jika sesuai
        if ("HAID".equalsIgnoreCase(jmark)) {
            ContentValues haidValues = new ContentValues();
            haidValues.put("id_tgl", tgl);
            haidValues.put("jenismark", jmark);
            myDB.insert(DB_TABLE_HAID, null, haidValues);
        } else if ("ISTIHADHAH".equalsIgnoreCase(jmark)) {
            ContentValues istihadhahValues = new ContentValues();
            istihadhahValues.put("id_tgl", tgl);
            istihadhahValues.put("jenismark", jmark);
            myDB.insert(DB_TABLE_ISTIHADHAH, null, istihadhahValues);
        }

        return rowId;
    }

    public void editDataEntry(String tgl, String jmark, String qshalat, int cshalat, int cpuasa){
        ContentValues values = new ContentValues();
        values.put("id_tgl", tgl);
        values.put("jenismark", jmark);
        values.put("waktusholat", qshalat);
        values.put("centangshalat", cshalat);
        values.put("centangpuasa", cpuasa);

        myDB = getReadableDatabase();
        myDB.update(DB_TABLE_UTAMA, values, "id_tgl="+tgl, null);
    }
}
