package com.example.databasehowto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    // 1
    private static final String DBName = "MyDB.db";
    private static final int DBVersion = 1;

    private static final String TableName = "MyTable";
    private static final String Col1 = "id";
    private static final String Col2 = "name";
    private static final String Col3 = "yearob";

    private SQLiteDatabase db;


    // 2. Rút gọn Constructor cho dễ dùng
    public MyDBHelper(@Nullable Context context) {
        super(context, DBName, null, DBVersion);
    }

    // 3. Bắt buộc phải có onCreate để tạo bảng
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TableName + " (" +
                Col1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Col2 + " TEXT, " +
                Col3 + " INTEGER)";
        db.execSQL(query);
    }

    // 4. Bắt buộc phải có onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }
    public void openDB() {
        db = this.getWritableDatabase();
    }
    public void closeDB() {
        if (db != null && db.isOpen()) {
            this.close();
        }
    }
    public long addData(String name, int yearob){
        openDB();

        ContentValues values = new ContentValues();
        values.put(Col2, name);
        values.put(Col3, yearob);

        long result = db.insert(TableName, null, values);

        closeDB();
        return result;
    }
    public int uppdateData(int id, String name, int yearob){
        openDB();

        ContentValues values = new ContentValues();
        values.put(Col2, name);
        values.put(Col3, yearob);

        String whereClause = Col1 + " = ?";
        String[] whereArgs = {String.valueOf(id)};

        int rowsAffected = db.update(TableName, values, whereClause, whereArgs);

        closeDB();

        return rowsAffected;
    }
    public Cursor disPlayData(){
        // Với hàm đọc dữ liệu, chúng ta nên dùng getReadableDatabase()
        SQLiteDatabase dbRead = this.getReadableDatabase();

        String query = "SELECT * FROM " + TableName;

        // Thực hiện truy vấn và trả về Cursor
        // Lưu ý: KHÔNG đóng DB ngay tại đây vì Cursor cần DB mở để đọc dữ liệu
        return dbRead.rawQuery(query, null);
    }
    public int deleteData(int id){
        openDB(); // Mở kết nối

        String whereClause = Col1 + " = ?";
        String[] whereArgs = {String.valueOf(id)};

        // Trả về số dòng đã xóa (thường là 1 nếu xóa thành công)
        int result = db.delete(TableName, whereClause, whereArgs);

        closeDB(); // Đóng kết nối
        return result;
    }
    public static String getTableName() { return TableName; }
    public static String getCol1() { return Col1; }
    public static String getCol2() { return Col2; }
    public static String getCol3() { return Col3; }
}