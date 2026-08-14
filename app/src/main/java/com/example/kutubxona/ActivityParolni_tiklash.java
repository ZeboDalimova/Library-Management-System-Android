package com.example.kutubxona;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;


public class ActivityParolni_tiklash extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    EditText email,phone,parol;
    Button otpravit, kirish;
    List<Person> personList;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parolni_tiklash);

        phone=findViewById(R.id.tiklashPhone);
        otpravit=findViewById(R.id.tiklashBtnOtpravit);

        DbHelperPerson dbHelperPerson = new DbHelperPerson(this);
        personList = dbHelperPerson.readAllPerson();

        if (personList == null || personList.isEmpty()) {
            Toast.makeText(this, "Нет данных для отображения", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (int i=0;i<personList.size();i++)
            {
                if(personList.get(i).phone.equals(phone.getText().toString().trim()));
                {
                    person=personList.get(i);
                }
            }
        }


        otpravit.setOnClickListener( v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
            } else {
                sendSms(person.phone, "Ваш парол"+person.password);
            }
            Intent intent =new Intent(ActivityParolni_tiklash.this,Activitylogin.class);
            startActivity(intent);
        });
    }


    private void sendSms(String phoneNumber, String message) {
        if (!phoneNumber.isEmpty() && !message.isEmpty()) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } else {
            Toast.makeText(this, "Raqam va xabarni kiriting.", Toast.LENGTH_SHORT).show();
        }
    }


}