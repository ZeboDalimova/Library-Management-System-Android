package com.example.kutubxona;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kutubxona.databinding.ActivityGlavnoeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Activityglavnoe extends AppCompatActivity {

    DbHelperPerson dbHelperPerson;
    DbHelperDesign dbHelperDesign;
    ActivityGlavnoeBinding binding;
    BottomNavigationView bottomNavigationView2;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    TextView uName, Email0;
    SharedPreferences settings;
    private static final String PREFS_FILE = "Account";
    private static final String PREF_NAME = "Status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGlavnoeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelperPerson = new DbHelperPerson(this);
        dbHelperDesign = new DbHelperDesign(this);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        String status = settings.getString(PREF_NAME, "none");
        boolean LogIn = settings.getBoolean("LogIn", false);
        int UserId = settings.getInt("Id", 0);

        bottomNavigationView2 = findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView2.getMenu();

        loadFragment(new Fragment_Glavniy());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            String title = item.getTitle().toString();
            if (title.equals("Главная")) {
                fragment = new Fragment_Glavniy();
                menu.getItem(0).setIcon(R.drawable.home_on);
                menu.getItem(1).setIcon(R.drawable.kategoriya_of);
                menu.getItem(2).setIcon(R.drawable.zakladki_of);
            } else if (title.equals("Категория")) {
                fragment = new Fragment_Kategoriya();
                menu.getItem(0).setIcon(R.drawable.home_of);
                menu.getItem(1).setIcon(R.drawable.kategoriya_on);
                menu.getItem(2).setIcon(R.drawable.zakladki_of);
            } else if (title.equals("Закладки")) {
                if (LogIn) {
                    fragment = new Fragment_Zakladka();
                    menu.getItem(0).setIcon(R.drawable.home_of);
                    menu.getItem(1).setIcon(R.drawable.kategoriya_of);
                    menu.getItem(2).setIcon(R.drawable.zakladki_on);
                } else {
                    showLoginDialog();
                }
            }
            if (fragment != null) loadFragment(fragment);
            return true;
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        if (status.equals("admin")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_menu_admin);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            String t = item.getTitle().toString();
            switch (t) {
                case "Мой профиль":
                    startActivity(new Intent(this, LogIn ? ActivityUpdate_User.class : Activitylogin.class));
                    break;
                case "Добавить книгу": fragment = new FragmentAddBook(); break;
                case "Добавить категории": fragment = new FragmentAddCategory(); break;
                case "Добавить дизайн": fragment = new FragmentAddDesign(); break;
                case "Список всех книг": fragment = new FragmentSpisokKnig(); break;
                case "Список всех категории": fragment = new FragmentSpisokCategory2(); break;
                case "Cписок всех дизайнов": fragment = new FragmentSpisokDesign(); break;
                case "Список всех пользователей": fragment = new FragmentSpisokUser(); break;
                case "Выход": logout(); return true;
            }
            if (fragment != null) displayFragment(fragment);
            drawerLayout.closeDrawers();
            return true;
        });

        if (LogIn) {
            updateHeader(UserId);
        }
    }

    private void updateHeader(int userId) {
        try {
            View headerView = navigationView.getHeaderView(0);
            uName = headerView.findViewById(R.id.dr_head_name);
            Email0 = headerView.findViewById(R.id.dr_head_email);
            ImageView iconka = headerView.findViewById(R.id.dr_head_icon);

            Person user = dbHelperPerson.Search(String.valueOf(userId), "user_id");
            if (user != null) {
                uName.setText(user.username);
                Email0.setText(user.email);

                // Защищаем вызов дизайна от ошибок базы данных
                try {
                    Design design = dbHelperDesign.Search(String.valueOf(user.image), "id_kat");
                    if (design != null && design.image != null) {
                        File file = new File(new File(getFilesDir(), "categoryPhotos"), design.image);
                        if (file.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                            iconka.setImageBitmap(bitmap);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Glavnoe", "Design table error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("Glavnoe", "Header update error: " + e.getMessage());
        }
    }

    private void showLoginDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Нужна регистрация")
                .setMessage("Хотите войти в аккаунт?")
                .setPositiveButton("Да", (d, i) -> startActivity(new Intent(this, Activitylogin.class)))
                .setNegativeButton("Нет", null)
                .show();
    }

    private void logout() {
        settings.edit().clear().apply();
        startActivity(new Intent(this, Activitylogin.class));
        finish();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }

    private void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).addToBackStack(null).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }
}
