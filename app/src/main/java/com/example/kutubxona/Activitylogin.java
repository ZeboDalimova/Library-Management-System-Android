package com.example.kutubxona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Activitylogin extends AppCompatActivity {

    private SQLiteDatabase db;
    DbHelperPerson dbHelperPerson;
    DbHelperKategoriya dbHelperKategoriya;
    DbHelperDesign dbHelperDesign;
    DbHelperBook dbHelperBook;
    EditText Email, Pass;
    TextView sms;
    private static final String PREFS_FILE = "Account";
    private static final String PREF_NAME = "Status";
    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        dbHelperPerson = new DbHelperPerson(this);
        dbHelperKategoriya = new DbHelperKategoriya(this);
        dbHelperPerson = new DbHelperPerson(this);
        dbHelperDesign = new DbHelperDesign(this);
        dbHelperBook = new DbHelperBook(this);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        Button btnlog = (Button) findViewById(R.id.login);
        sms = (TextView) findViewById(R.id.smsgaotish);

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Activitylogin.this, ActivityParolni_tiklash.class);
                startActivity(intent);
            }
        });


        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = findViewById(R.id.LoginUsername);
                Pass = findViewById(R.id.LoginPassword);
                String E, P;
                E = Email.getText().toString().trim();
                P = Pass.getText().toString().trim();
                List<Person> people = (List<Person>) dbHelperPerson.readAllPerson();
                String txtE = "";
                String txtP = "";
                String txtS = "";
                int id = -1;
                for (Person p : people) {
                    if (p.email.equals(E)) {
                        txtE = p.email;
                        txtP = p.password;
                        id = p.id;
                        txtS = p.status;
                    }
//                    txt += p.id + " " + p.email + " " + p.password;
                }
                if (txtE != "") {
                    if (txtE.equals(E) && txtP.equals(P)) {
                        SharedPreferences.Editor prefEditor = settings.edit();
                        prefEditor.putString("Status", txtS);
                        prefEditor.putBoolean("LogIn", true);
                        prefEditor.putInt("Id", id);
                        prefEditor.apply();

                        Intent intent = new Intent(Activitylogin.this, Activityglavnoe.class);
                        intent.putExtra("id_u", id);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email yoki parol noto'g'ri!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Bunday email topilmadi!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(this, Activityregister.class);
        startActivity(intent);
    }
}