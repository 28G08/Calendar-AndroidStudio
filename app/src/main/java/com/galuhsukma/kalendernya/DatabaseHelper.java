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
    public static final int DB_VER = 1;
    public static final String CREATE_TABLE_UTAMA = "create table "+DB_TABLE_UTAMA+" (id_tgl String);";

    Context ctx;
    SQLiteDatabase myDB;

    public DatabaseHelper(Context ct){
        super(ct, DB_NAME, null, DB_VER);
        ctx = ct;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_UTAMA);
        Log.i("Database", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        String query = "drop table if exists "+DB_TABLE_UTAMA+"";
        db.execSQL(query);
    }

    public Long insertData(String tgl){
        myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_tgl", tgl);

        long rowId = myDB.insert(DB_TABLE_UTAMA, null, values);

        return rowId;
    }

    public void editDataEntry(String tgl){
        ContentValues values = new ContentValues();
        values.put("id_tgl", tgl);

        myDB = getReadableDatabase();
        myDB.update(DB_TABLE_UTAMA, values, "id_tgl="+tgl, null);
    }
}
