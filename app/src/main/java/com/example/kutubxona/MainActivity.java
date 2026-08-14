package com.example.kutubxona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    private SQLiteDatabase db;
    DbHelperPerson dbHelperPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        SharedPreferences preferences = getSharedPreferences("Account", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Status", "none"); // или "user"
        editor.putBoolean("LogIn", false);
        editor.putInt("Id", 0); // Пример ID пользователя
        editor.apply();

//        db = getBaseContext().openOrCreateDatabase("myDatabase.db", MODE_PRIVATE, null);
//        dbHelperPerson=new DbHelperPerson(this);
//        dbHelperPerson.onCreate(db);



        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this, Activityglavnoe.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}