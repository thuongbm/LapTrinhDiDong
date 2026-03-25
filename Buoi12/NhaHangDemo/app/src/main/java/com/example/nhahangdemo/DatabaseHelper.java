package com.example.nhahangdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "Nhahang.db", null, 1);
    }
    public void deleteLowerRated(double rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("NhaHang", "rate < ?", new String[]{String.valueOf(rating)});
        db.close();
    }

    public List<NhaHang> getAllNhaHang() {
        List<NhaHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NhaHang", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new NhaHang(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Also add a method to insert
    public void addNhaHang(NhaHang nh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", nh.getTen());
        values.put("address", nh.getDiaChi());
        values.put("rate", nh.getDanhGia());
        db.insert("NhaHang", null, values);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql ="CREATE TABLE NhaHang(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "address TEXT," +
                "rate REAL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS NhaHang");
        onCreate(db);
    }
}
