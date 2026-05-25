package com.example.thi;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import kotlin.text.UStringsKt;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuanLyBanBe.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "BanBe";
    public static final String COLUMN_MA = "ma";
    public static final String COLUMN_TEN = "hoVaTen";
    public static final String COLUMN_YEUTHICH = "yeuThich";
    public static final String COLUMN_NGAYSINH = "ngaySinh";
    public static final String COLUMN_DANHGIA = "danhGia";
    public static final String COLUMN_TRONGSO = "trongSo";
    public static final String COLUMN_LOAI = "loai";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_MA + " TEXT PRIMARY KEY, "
                + COLUMN_TEN + " TEXT, "
                + COLUMN_YEUTHICH + " FLOAT, "
                + COLUMN_NGAYSINH + " DATE, "
                + COLUMN_DANHGIA + " FLOAT,"
                + COLUMN_TRONGSO + " INTEGER,"
                + COLUMN_LOAI + "REAL)";

        db.execSQL(createTableStatement);

        // Câu 2.2: Nhập dữ liệu mẫu tự động ngay khi tạo database lần đầu
        insertSampleData(db);
    }
    private void insertSampleData(SQLiteDatabase db) {
        insertRow(db, "DH30", "BUITHUONG", "5.5", "30/12/2005", "9.0", "2", "1");
        insertRow(db, "DH42", "BUITHUONG", "5.5", "30/12/2005", "9.0", "2", "1");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    private void insertRow(SQLiteDatabase db, String ma, String ten, String yeuThich, String ngaySinh, String danhGia, String trongSo, String loai) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MA, ma);
        cv.put(COLUMN_TEN, ten);
        cv.put(COLUMN_YEUTHICH, yeuThich);
        cv.put(COLUMN_NGAYSINH, ngaySinh);
        cv.put(COLUMN_DANHGIA, danhGia);
        cv.put(COLUMN_TRONGSO, trongSo);
        cv.put(COLUMN_LOAI, loai);
        db.insert(TABLE_NAME, null, cv);
    }
}
