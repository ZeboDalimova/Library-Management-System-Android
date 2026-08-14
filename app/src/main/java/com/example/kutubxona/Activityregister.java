package com.example.kutubxona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activityregister extends AppCompatActivity {
    DbHelperPerson dbHelperPerson;
    EditText Uname, Email, Phone, Pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Uname = findViewById(R.id.RegUserName);
        Email = findViewById(R.id.RegEmail);
        Phone = findViewById(R.id.RegPhone);
        Pass = findViewById(R.id.RegPassword);

        Button btnInsert = findViewById(R.id.register);
        dbHelperPerson = new DbHelperPerson(this);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String U = Uname.getText().toString().trim();
                String E = Email.getText().toString().trim();
                String Ph = Phone.getText().toString().trim();
                String P = Pass.getText().toString().trim();

                if (U.isEmpty() || E.isEmpty() || P.isEmpty()) {
                    Toast.makeText(Activityregister.this, "Iltimos, barcha maydonlarni to'ldiring", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Статус по умолчанию 'user', аватар '1'
                Person p = new Person(U, E, Ph, P, "admin", "1");

                long result = dbHelperPerson.insertPerson(p);
                
                if (result != -1) {
                    Log.d("Register", "User inserted with ID: " + result);
                    Toast.makeText(getApplicationContext(), "Ro'yhatdan o'tdingiz!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Activityregister.this, Activitylogin.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("Register", "Insert failed for user: " + U);
                    Toast.makeText(getApplicationContext(), "Ro'yhatdan o'tishda xatolik. Bazaga yozilmadi.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
