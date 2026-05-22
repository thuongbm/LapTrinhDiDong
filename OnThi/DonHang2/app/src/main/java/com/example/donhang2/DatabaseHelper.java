package com.example.donhang2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuanLyDonHang.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "DonHang";
    public static final String COLUMN_MA = "maDonHang";
    public static final String COLUMN_TEN = "tenHang";
    public static final String COLUMN_NGAY = "ngayDat";
    public static final String COLUMN_GIA = "giaHang";
    public static final String COLUMN_GIAO_NHANH = "loaiGiaoHang";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_MA + " TEXT PRIMARY KEY, "
                + COLUMN_TEN + " TEXT, "
                + COLUMN_NGAY + " TEXT, "
                + COLUMN_GIA + " REAL, "
                + COLUMN_GIAO_NHANH + " INTEGER)"; // 1: giao nhanh, 0: giao thường
        db.execSQL(createTableStatement);

        // Câu 2.2: Nhập dữ liệu mẫu tự động ngay khi tạo database lần đầu
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Hàm phụ trợ chèn nhanh dữ liệu mẫu (Sử dụng công thức ID: 17, 22, 27, 32...)
    private void insertSampleData(SQLiteDatabase db) {
        insertRow(db, "DH17", "Nước giặt", "15/01/2023", 1000000f, 0);
        insertRow(db, "DH22", "Quạt", "18/01/2023", 1500000f, 1);
        insertRow(db, "DH27", "Áo thun", "22/01/2023", 800000f, 1);
        insertRow(db, "DH32", "Bình hoa", "17/01/2023", 300000f, 0);
    }

    private void insertRow(SQLiteDatabase db, String ma, String ten, String ngay, float gia, int giaoNhanh) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MA, ma);
        cv.put(COLUMN_TEN, ten);
        cv.put(COLUMN_NGAY, ngay);
        cv.put(COLUMN_GIA, gia);
        cv.put(COLUMN_GIAO_NHANH, giaoNhanh);
        db.insert(TABLE_NAME, null, cv);
    }

    // Hàm cập nhật lại giá hàng mới vào SQLite sau khi giảm giá
    public void capNhatGiaVaoCSDL(String maDonHang, float giaMoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GIA, giaMoi);

        // Cập nhật dựa theo Mã đơn hàng (Khóa chính)
        db.update(TABLE_NAME, cv, COLUMN_MA + " = ?", new String[]{maDonHang});
    }
}
