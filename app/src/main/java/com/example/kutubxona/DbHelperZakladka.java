package com.example.kutubxona;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelperZakladka extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final String TABLE_NAME = "zakladka";

    private static final String K_ID = "id_zakladka";
    private static final String B_NAME = "kitob_id";
    private static final String K_USER = "user_id";
    private static final String K_STATUS = "status";

    public DbHelperZakladka(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + K_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + B_NAME + " VARCHAR(100)," + K_USER + " VARCHAR(200), " + K_STATUS + " VARCHAR(100))";
        db.execSQL(query);
    }

    @SuppressLint("Range")
    public List<Zakladka> readAllKat(int id) {
        List<Zakladka> listBook = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        if (id != 0) {
            query += " WHERE " + K_USER + "=" + id;
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                listBook.add(new Zakladka(cursor.getInt(cursor.getColumnIndex(K_ID)),
                        cursor.getInt(cursor.getColumnIndex(B_NAME)),
                        cursor.getInt(cursor.getColumnIndex(K_USER)),
                        cursor.getString(cursor.getColumnIndex(K_STATUS))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listBook;
    }

    @SuppressLint("Range")
    public List<Zakladka> readAllKatSearch(int id, String ustun) {
        List<Zakladka> listBook = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        if (id != 0) {
            query += " WHERE " + ustun + "=" + id;
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                listBook.add(new Zakladka(cursor.getInt(cursor.getColumnIndex(K_ID)),
                        cursor.getInt(cursor.getColumnIndex(B_NAME)),
                        cursor.getInt(cursor.getColumnIndex(K_USER)),
                        cursor.getString(cursor.getColumnIndex(K_STATUS))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listBook;
    }

    public int updateKat(Zakladka b) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(B_NAME, b.book_id);
        values.put(K_USER, b.user_id);
        values.put(K_STATUS, b.status);
        int d = db.update(TABLE_NAME, values, K_ID + "=?", new String[]{String.valueOf(b.id)});
        db.close();
        return d;
    }

    public long inserKat(Zakladka book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(B_NAME, book.book_id);
        values.put(K_USER, book.user_id);
        values.put(K_STATUS, book.status);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public Zakladka Search(String id, String ustun) {
        Zakladka zakladka = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ustun + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            zakladka = new Zakladka(cursor.getInt(cursor.getColumnIndex(K_ID)),
                    cursor.getInt(cursor.getColumnIndex(B_NAME)),
                    cursor.getInt(cursor.getColumnIndex(K_USER)),
                    cursor.getString(cursor.getColumnIndex(K_STATUS)));
        }
        cursor.close();
        db.close();
        return zakladka;
    }

    public int deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int d = db.delete(TABLE_NAME, K_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return d;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
