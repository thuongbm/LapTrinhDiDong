package com.example.donhang;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Định nghĩa các hằng số tên CSDL và Bảng
    private static final String DATABASE_NAME = "QuanLyDonHang.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "DonHang";
    public static final String COLUMN_MA = "ma";
    public static final String COLUMN_TEN = "tenHang";
    public static final String COLUMN_NGAY = "ngayDat";
    public static final String COLUMN_GIA = "giaHang";
    public static final String COLUMN_LOAI = "loaiGiaoHang"; // 1: Nhanh, 0: Thường

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // 2.1 Viết lệnh tạo cơ sở dữ liệu SQLite
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_MA + " TEXT PRIMARY KEY, " +
                COLUMN_TEN + " TEXT, " +
                COLUMN_NGAY + " TEXT, " +
                COLUMN_GIA + " REAL, " +
                COLUMN_LOAI + " INTEGER)";
        db.execSQL(createTableQuery);

        // 2.2 Nhập dữ liệu mẫu tự chọn trực tiếp khi khởi tạo bảng lần đầu
        chenDuLieuMau(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Hàm phụ trợ chèn dữ liệu mẫu tương tự danh sách trên UI của bạn
    private void chenDuLieuMau(SQLiteDatabase db) {
        // Khởi tạo các biến mã SV như các câu trước (A=35, B=17, C=5)
        int startId = 17;
        int step = 5;

        insertRow(db, "DH" + startId, "Nước giặt", "15/01/2023", 1000000f, 0);

        startId += step; // DH22
        insertRow(db, "DH" + startId, "Quạt", "18/01/2023", 1500000f, 1);

        startId += step; // DH27
        insertRow(db, "DH" + startId, "Áo thun", "22/01/2023", 800000f, 1);

        startId += step; // DH32
        insertRow(db, "DH" + startId, "Bình hoa", "17/01/2023", 300000f, 0);
    }

    private void insertRow(SQLiteDatabase db, String ma, String ten, String ngay, float gia, int loai) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MA, ma);
        values.put(COLUMN_TEN, ten);
        values.put(COLUMN_NGAY, ngay);
        values.put(COLUMN_GIA, gia);
        values.put(COLUMN_LOAI, loai);
        db.insert(TABLE_NAME, null, values);
    }
}