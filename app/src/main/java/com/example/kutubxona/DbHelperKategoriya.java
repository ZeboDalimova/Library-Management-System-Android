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

public class DbHelperKategoriya extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final String TABLE_NAME = "kategoriya";

    private static final String K_ID = "id_kat";
    private static final String K_NAME = "kat_name";
    private static final String K_IMAGE = "kat_image";


    public DbHelperKategoriya(@Nullable Context context) {
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
        String query = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +"("+ K_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ K_NAME +" VARCHAR(100),"+ K_IMAGE +" VARCHAR(200))";
        db.execSQL(query);
    }

    public long inserKat(Kategoriya book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(K_NAME, book.kat_name);
        values.put(K_IMAGE, book.image);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public List<Kategoriya> readAllKat(int id){
        List<Kategoriya> listBook = new ArrayList<Kategoriya>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        if(id != 0){
            query += " WHERE "+K_ID+"=" + id;
        }
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                listBook.add(new Kategoriya(cursor.getInt(cursor.getColumnIndex(K_ID)),
                        cursor.getString(cursor.getColumnIndex(K_NAME)),
                        cursor.getString(cursor.getColumnIndex(K_IMAGE))
                ));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listBook;
    }

    @SuppressLint("Range")
    public Kategoriya Search(String id,String ustun) {
        Kategoriya listPerson=null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME+" WHERE "+ustun+ "=?";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(id) });

        if (cursor.moveToFirst()) {
            listPerson=new Kategoriya(cursor.getInt(cursor.getColumnIndex(K_ID)),
                    cursor.getString(cursor.getColumnIndex(K_NAME)),
                    cursor.getString(cursor.getColumnIndex(K_IMAGE)));
        }
        cursor.close();
        db.close();
        return listPerson;
    }


    public int updateKat(Kategoriya b){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(K_NAME, b.kat_name);
        values.put(K_IMAGE, b.image);
        int d =  db.update(TABLE_NAME, values, K_ID + "=?", new String[]{String.valueOf(b.id)});
        db.close();
        return d;
    }
    public int deleteKat(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int d = db.delete(TABLE_NAME, K_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return d;
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
