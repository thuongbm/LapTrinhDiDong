package com.example.songapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SongManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SONGS = "songs";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SINGER = "singer";
    private static final String KEY_RATING = "rating";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_SINGER + " TEXT,"
                + KEY_RATING + " REAL" + ")";
        db.execSQL(CREATE_SONGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        onCreate(db);
    }

    // Thêm bài hát mới
    public void addSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, song.getTen());
        values.put(KEY_SINGER, song.getTenCaSi());
        values.put(KEY_RATING, song.getDiemDanhGia());

        db.insert(TABLE_SONGS, null, values);
        db.close();
    }

    // Lấy tất cả bài hát
    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SONGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getFloat(3),
                        cursor.getString(2)
                );
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songList;
    }

    // Thêm phương thức này vào lớp DatabaseHandler của bạn
    public int updateSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, song.getTen());
        values.put(KEY_SINGER, song.getTenCaSi());
        values.put(KEY_RATING, song.getDiemDanhGia());

        // Cập nhật dòng có ID tương ứng
        return db.update(TABLE_SONGS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(song.getMaBaiHat())});
    }
}
