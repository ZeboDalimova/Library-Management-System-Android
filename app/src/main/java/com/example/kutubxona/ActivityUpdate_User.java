package com.example.kutubxona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kutubxona.databinding.ActivityGlavnoeBinding;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class ActivityUpdate_User extends AppCompatActivity {

    EditText Name, Email, Phone, Password;
    Button Save;
    ImageView Icon;
    RecyclerView listView;
    SharedPreferences settings;
    String id_rasm="0";
    int userId=0;
    private static final String PREFS_FILE = "Account";
    private static final String PREF_NAME = "Status";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Завершение активности
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Name = findViewById(R.id.updateUserNameEdt);
        Email = findViewById(R.id.updateEmailEdt);
        Phone = findViewById(R.id.updatePhoneEdt);
        Password = findViewById(R.id.updatePasswordEdt);
        Save = findViewById(R.id.updateBtn);
        Icon = findViewById(R.id.updateUserIconImage);
        listView = findViewById(R.id.updateUserIconList);

        Toolbar toolbar = findViewById(R.id.toolbarActivityUpdateUser);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listView.setLayoutManager(layoutManager);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelperPerson dbHelperPerson = new DbHelperPerson(ActivityUpdate_User.this);
                settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
                int id_u = settings.getInt("Id", 0);
                String stat_u = settings.getString("Status", "user");
                Person p=new Person(
                        id_u,
                        Name.getText().toString().trim(),
                        Email.getText().toString().trim(),
                        Phone.getText().toString().trim(),
                        Password.getText().toString().trim(),
                        stat_u,
                        String.valueOf(userId));
                if(dbHelperPerson.updatePerson(p)!=-1)
                {
                    Toast.makeText(getApplicationContext(), "Данные успешно обновились!", Toast.LENGTH_SHORT).show();

//                    Intent intent = getIntent(); // Получаем текущий Intent
//                    finish(); // Завершаем текущую Activity
//                    startActivity(intent);
                    Intent intent = new Intent(ActivityUpdate_User.this, Activityglavnoe.class);
                    intent.putExtra("obnavitGlavniy", true);
                    finish();
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Не получается изменить данные!", Toast.LENGTH_SHORT).show();
                }
//                dbHelperPerson.updatePerson(p);

            }
        });

    }

    private SQLiteDatabase db;
    List<Design> usersList;
    AdapterDesign customAdapter;

    @Override
    public void onResume() {
        super.onResume();

        DbHelperDesign dbHelperKategoriya = new DbHelperDesign(this);
        usersList = dbHelperKategoriya.readAllKat(0);

        if (usersList == null || usersList.isEmpty()) {

        }
        else {
            customAdapter = new AdapterDesign(usersList, design -> {
                userId = design.id;
                id_rasm=design.image;

                try {
                    File directory = new File(this.getFilesDir(), "categoryPhotos");
                    File file = new File(directory, design.image);

                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    Icon.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

            listView.setAdapter(customAdapter);
        }


    }


}