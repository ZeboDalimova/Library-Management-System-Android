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

public class DbHelperBook extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final String TABLE_NAME = "kitoblar";

    private static final String K_ID = "id_kitob";
    private static final String K_NAME = "kitobname";
    private static final String K_KAT = "kategoriya_id";
    private static final String K_RASM = "rasm";
    private static final String K_OP = "opesaniya";
    private static final String K_FAYL = "fayl";
    private static final String K_AUDIO = "audio";
    private static final String K_KOMM = "komment";

    public DbHelperBook(@Nullable Context context) {
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
        String query = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +"("+ K_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ K_NAME +" VARCHAR(100), "+ K_KAT +" INTEGER, "+ K_RASM +" VARCHAR(200),"+ K_OP +" VARCHAR(200),"+ K_FAYL +" VARCHAR(200),"+ K_AUDIO +" VARCHAR(200),"+ K_KOMM +" VARCHAR(200))";
        db.execSQL(query);
    }

    public long insertBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(K_NAME, book.kitobname);
        values.put(K_KAT, book.kateg_id);
        values.put(K_RASM, book.rasm_id);
        values.put(K_OP, book.opesan);
        values.put(K_FAYL, book.fayl_id);
        values.put(K_AUDIO, book.audio_id);
        values.put(K_KOMM, book.komment);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public List<Book> readAllBook(int id){
        List<Book> listBook = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        if(id != 0){
            query += " WHERE id_kitob=" + id;
        }
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                listBook.add(new Book(cursor.getInt(cursor.getColumnIndex(K_ID)),
                        cursor.getString(cursor.getColumnIndex(K_NAME)),
                        cursor.getInt(cursor.getColumnIndex(K_KAT)),
                        cursor.getString(cursor.getColumnIndex(K_RASM)),
                        cursor.getString(cursor.getColumnIndex(K_OP)),
                        cursor.getString(cursor.getColumnIndex(K_FAYL)),
                        cursor.getString(cursor.getColumnIndex(K_AUDIO)),
                        cursor.getString(cursor.getColumnIndex(K_KOMM))
                ));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listBook;
    }

    @SuppressLint("Range")
    public Book search(int id){
        Book book = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id_kitob=" + id;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            book = new Book(cursor.getInt(cursor.getColumnIndex(K_ID)),
                    cursor.getString(cursor.getColumnIndex(K_NAME)),
                    cursor.getInt(cursor.getColumnIndex(K_KAT)),
                    cursor.getString(cursor.getColumnIndex(K_RASM)),
                    cursor.getString(cursor.getColumnIndex(K_OP)),
                    cursor.getString(cursor.getColumnIndex(K_FAYL)),
                    cursor.getString(cursor.getColumnIndex(K_AUDIO)),
                    cursor.getString(cursor.getColumnIndex(K_KOMM))
            );
        }
        cursor.close();
        db.close();
        return book;
    }

    public int updateBook(Book b){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(K_NAME, b.kitobname);
        values.put(K_KAT, b.kateg_id);
        values.put(K_RASM, b.rasm_id);
        values.put(K_OP, b.opesan);
        values.put(K_FAYL, b.fayl_id);
        values.put(K_AUDIO, b.audio_id);
        values.put(K_KOMM, b.komment);
        int d =  db.update(TABLE_NAME, values, K_ID + "=?", new String[]{String.valueOf(b.id)});
        db.close();
        return d;
    }

    public int deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int d = db.delete(TABLE_NAME, K_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return d;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }
}
