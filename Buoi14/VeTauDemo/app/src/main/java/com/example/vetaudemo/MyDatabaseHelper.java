package com.example.vetaudemo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VeTauManager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "VeTau";

    // Các cột
    private static final String COLUMN_ID = "maVe";
    private static final String COLUMN_GA_DI = "gaDi";
    private static final String COLUMN_GA_DEN = "gaDen";
    private static final String COLUMN_GIA = "donGia";
    private static final String COLUMN_LOAI = "loaiVe";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_GA_DI + " TEXT, "
                + COLUMN_GA_DEN + " TEXT, "
                + COLUMN_GIA + " REAL, "
                + COLUMN_LOAI + " INTEGER" + ")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Thêm vé mới
    public void addVeTau(VeTau ve) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, ve.getMaVe());
        values.put(COLUMN_GA_DI, ve.getGaDi());
        values.put(COLUMN_GA_DEN, ve.getGaDen());
        values.put(COLUMN_GIA, ve.getDonGia());
        values.put(COLUMN_LOAI, ve.isLoaiVe() ? 1 : 0);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Lấy toàn bộ danh sách vé
    public List<VeTau> getAllVeTau() {
        List<VeTau> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                VeTau ve = new VeTau();
                ve.setMaVe(cursor.getInt(0));
                ve.setGaDi(cursor.getString(1));
                ve.setGaDen(cursor.getString(2));
                ve.setDonGia(cursor.getFloat(3));
                ve.setLoaiVe(cursor.getInt(4) == 1);
                list.add(ve);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Kiểm tra xem database có dữ liệu chưa
    public int getCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Thêm vào trong lớp MyDatabaseHelper.java
    public String getThongKe() {
        int khuHoi = 0;
        int motChieu = 0;
        float tongTien = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT loaiVe, donGia FROM VeTau", null);

        if (cursor.moveToFirst()) {
            do {
                int loai = cursor.getInt(0); // 1 là Khứ hồi, 0 là Một chiều
                float gia = cursor.getFloat(1);

                if (loai == 1) khuHoi++;
                else motChieu++;

                tongTien += gia;
            } while (cursor.moveToNext());
        }
        cursor.close();

        return "Khứ hồi: " + khuHoi + " | Một chiều: " + motChieu + "\nTổng tiền: " + String.format("%.3f", tongTien);
    }

    public void deleteVeTau(int maVe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("VeTau", "maVe = ?", new String[]{String.valueOf(maVe)});
        db.close();
    }

    // 2. Hàm Cập nhật
    public void updateVeTau(VeTau ve) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("gaDi", ve.getGaDi());
        values.put("gaDen", ve.getGaDen());
        values.put("donGia", ve.getDonGia());
        values.put("loaiVe", ve.isLoaiVe() ? 1 : 0);

        db.update("VeTau", values, "maVe = ?", new String[]{String.valueOf(ve.getMaVe())});
        db.close();
    }
}