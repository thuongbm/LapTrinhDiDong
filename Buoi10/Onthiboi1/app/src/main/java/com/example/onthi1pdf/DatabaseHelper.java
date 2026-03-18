package com.example.onthi1pdf;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "StudentManager.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Students (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "math INTEGER, " +
                "chemistry INTEGER, " +
                "physics INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Students");
        onCreate(db);
    }

    public void addSampleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Example: Student "Ha" with scores totaling 23
        values.put("name", "Ha");
        values.put("math", 8);
        values.put("chemistry", 7);
        values.put("physics", 8);

        db.insert("Students", null, values);

        values.put("name", "Noname");
        values.put("math", 3);
        values.put("chemistry", 10);
        values.put("physics", 11);

        db.insert("Students", null, values);
        db.close();
    }
}
