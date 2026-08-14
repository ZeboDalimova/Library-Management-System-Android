package com.example.kutubxona;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelperPerson extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final String TABLE_NAME = "users";
    private static final String P_ID = "user_id";
    private static final String P_USERNAME = "username";
    private static final String P_EMAIL = "email";
    private static final String P_PHONE = "phone";
    private static final String P_PASSWORD = "password";
    private static final String P_STATUS = "status";
    private static final String P_IMAGE = "image";

    public DbHelperPerson(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTable(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Принудительно создаем таблицу при каждом открытии, если её нет
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" 
                + P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
                + P_USERNAME + " VARCHAR(100), " 
                + P_EMAIL + " VARCHAR(100), "
                + P_PHONE + " VARCHAR(100), " 
                + P_PASSWORD + " VARCHAR(100), " 
                + P_STATUS + " VARCHAR(100), " 
                + P_IMAGE + " VARCHAR(100))";
        db.execSQL(query);
        Log.d("DbHelperPerson", "Table users checked/created");
    }

    public long insertPerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(P_USERNAME, person.username);
        values.put(P_EMAIL, person.email);
        values.put(P_PHONE, person.phone);
        values.put(P_PASSWORD, person.password);
        values.put(P_STATUS, person.status);
        values.put(P_IMAGE, person.image);

        long result = -1;
        try {
            // Используем insertOrThrow для получения детальной ошибки в catch
            result = db.insertOrThrow(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("DbHelperPerson", "Insert failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public List<Person> readAllPerson() {
        List<Person> listPerson = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                listPerson.add(new Person(cursor.getInt(cursor.getColumnIndex(P_ID)),
                        cursor.getString(cursor.getColumnIndex(P_USERNAME)),
                        cursor.getString(cursor.getColumnIndex(P_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(P_PHONE)),
                        cursor.getString(cursor.getColumnIndex(P_PASSWORD)),
                        cursor.getString(cursor.getColumnIndex(P_STATUS)),
                        cursor.getString(cursor.getColumnIndex(P_IMAGE))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listPerson;
    }

    public int updatePerson(Person p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(P_USERNAME, p.username);
        values.put(P_EMAIL, p.email);
        values.put(P_PHONE, p.phone);
        values.put(P_PASSWORD, p.password);
        values.put(P_STATUS, p.status);
        values.put(P_IMAGE, p.image);

        int d = db.update(TABLE_NAME, values, P_ID + "=?", new String[]{String.valueOf(p.id)});
        db.close();
        return d;
    }

    public int deletePerson(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int d = db.delete(TABLE_NAME, P_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return d;
    }

    @SuppressLint("Range")
    public Person Search(String id, String ustun) {
        Person person = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ustun + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            person = new Person(cursor.getInt(cursor.getColumnIndex(P_ID)),
                    cursor.getString(cursor.getColumnIndex(P_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(P_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(P_PHONE)),
                    cursor.getString(cursor.getColumnIndex(P_PASSWORD)),
                    cursor.getString(cursor.getColumnIndex(P_STATUS)),
                    cursor.getString(cursor.getColumnIndex(P_IMAGE)));
        }
        cursor.close();
        db.close();
        return person;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}