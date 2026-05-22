package com.example.monhoc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuanLyMonHoc.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và các cột theo đúng thuộc tính đối tượng MonThi
    public static final String TABLE_NAME = "tbl_mon_thi";
    public static final String COLUMN_MA = "maMon";
    public static final String COLUMN_TEN = "tenMon";
    public static final String COLUMN_TIN_CHI = "soTinChi";
    public static final String COLUMN_DIEM_QT = "diemQuaTrinh";
    public static final String COLUMN_DIEM_THI = "diemThi";
    public static final String COLUMN_LOAI = "loai";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Câu 2.1: Viết lệnh tạo CSDL SQLite
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_MA + " INTEGER PRIMARY KEY, " // Mã tự quản lý nhảy số +11 theo logic ứng dụng
                + COLUMN_TEN + " TEXT, "
                + COLUMN_TIN_CHI + " INTEGER, "
                + COLUMN_DIEM_QT + " REAL, "
                + COLUMN_DIEM_THI + " REAL, "
                + COLUMN_LOAI + " INTEGER)";

        db.execSQL(createTableStatement);
        Log.d("SQLITE_LOG", "Đã tạo bảng CSDL thành công bằng lệnh: " + createTableStatement);

        // Câu 2.2: Chèn dữ liệu mẫu vào database khi khởi tạo lần đầu
        chenDuLieuMau(db, 4, "Lập trình TBDD", 3, 5.0f, 6.0f, 1);
        chenDuLieuMau(db, 15, "Lập trình web", 3, 2.0f, 5.0f, 1);
        chenDuLieuMau(db, 26, "Hệ quản trị CSDL", 2, 3.5f, 2.5f, 1);
        chenDuLieuMau(db, 37, "Mạng máy tính", 3, 8.5f, 7.0f, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Hàm phụ trợ chèn dữ liệu mẫu
    private void chenDuLieuMau(SQLiteDatabase db, int ma, String ten, int tc, float qt, float thi, int loai) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MA, ma);
        cv.put(COLUMN_TEN, ten);
        cv.put(COLUMN_TIN_CHI, tc);
        cv.put(COLUMN_DIEM_QT, qt);
        cv.put(COLUMN_DIEM_THI, thi);
        cv.put(COLUMN_LOAI, loai);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result != -1) {
            Log.d("SQLITE_LOG", "Minh chứng chèn thành công môn: " + ten + " (Mã: " + ma + ")");
        }
    }

    // Hàm thêm mới một môn học (Dùng cho nút FAB Add)
    public boolean addMonThi(MonThi monThi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MA, monThi.getMa());
        cv.put(COLUMN_TEN, monThi.getTenMon());
        cv.put(COLUMN_TIN_CHI, monThi.getSoTinChi());
        cv.put(COLUMN_DIEM_QT, monThi.getDiemQuaTrinh());
        cv.put(COLUMN_DIEM_THI, monThi.getDiemThi());
        cv.put(COLUMN_LOAI, monThi.getLoai());

        long insert = db.insert(TABLE_NAME, null, cv);
        db.close();
        return insert != -1;
    }

    // Hàm đọc toàn bộ danh sách môn học từ SQLite ra ứng dụng
    public List<MonThi> getAllMonThi() {
        List<MonThi> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int ma = cursor.getInt(0);
                String ten = cursor.getString(1);
                int tc = cursor.getInt(2);
                float qt = cursor.getFloat(3);
                float thi = cursor.getFloat(4);
                int loai = cursor.getInt(5);

                MonThi monThi = new MonThi(ma, ten, tc, qt, thi, loai);
                returnList.add(monThi);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }
}