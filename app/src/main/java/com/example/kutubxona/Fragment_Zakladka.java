package com.example.kutubxona;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kutubxona.databinding.ActivityGlavnoeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Zakladka extends Fragment {


    public Fragment_Zakladka() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    Button button1,button2,button3;
    List<Book> usersList;
    List<Book> usersList2;
    List<Zakladka> zakladkaList;
    ListView listView;
    TextView textView;
    SharedPreferences settings;
    FragmentManager fragmentManager;
    private static final String PREFS_FILE = "Account";
    private static final String PREF_NAME = "Status";
    AdapterBook customAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zakladka, container, false);
        listView = view.findViewById(R.id.listBooksZakladka);
        settings = requireContext().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        Boolean LogIn = settings.getBoolean("LogIn", false);
        button1=view.findViewById(R.id.btnChitatt);
        button2=view.findViewById(R.id.btnPrachitana);
        button3=view.findViewById(R.id.btnBuduchitat);
        textView=view.findViewById(R.id.statusknigtxt);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Zakladka newFragment = new Fragment_Zakladka();
                Bundle args = new Bundle();
                args.putString("statusBook", "Читаю");
                newFragment.setArguments(args);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Zakladka newFragment = new Fragment_Zakladka();
                Bundle args = new Bundle();
                args.putString("statusBook", "Прочитано");
                newFragment.setArguments(args);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Zakladka newFragment = new Fragment_Zakladka();
                Bundle args = new Bundle();
                args.putString("statusBook", "В планах");
                newFragment.setArguments(args);

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        int userId = settings.getInt("Id", 0);
        DbHelperBook dbHelperBook = new DbHelperBook(requireContext());
        DbHelperZakladka dbHelperZakladka = new DbHelperZakladka(requireContext());
        zakladkaList=dbHelperZakladka.readAllKatSearch(userId,"user_id");
        usersList2 = new ArrayList<>();
        for(int i=0;i<zakladkaList.size();i++)
        {
            usersList2.add(dbHelperBook.search(zakladkaList.get(i).book_id));
        }


        Bundle args = getArguments();
        if (args != null) {
            String value = args.getString("statusBook");
            if(value.equals("Читаю"))
            {
                textView.setText("Читаю");
            }
            else if(value.equals("Прочитано"))
            {
                textView.setText("Прочитано");
            }
            else if(value.equals("В планах"))
            {
                textView.setText("В планах");
            }
            else
            {
                textView.setText("Все");
            }

            if (usersList2 == null || usersList2.isEmpty()) {
//                Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
            }
            else
            {
                usersList = new ArrayList<>();
                for(int i=0; i<zakladkaList.size();i++)
                {
                    if(zakladkaList.get(i).status.trim().equals(value))
                    {
                        usersList.add(usersList2.get(i));
                    }
                }
            }
        }
        else
        {
            if (usersList2 == null || usersList2.isEmpty()) {
//                Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
            }
            else
            {
                usersList = new ArrayList<>();
                for(int i=0; i<zakladkaList.size();i++) {
                    usersList.add(usersList2.get(i));
                }
            }
        }

        if (usersList == null || usersList.isEmpty()) {
//            Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
        }

        else {

            customAdapter = new AdapterBook(requireContext(), usersList);
            listView.setAdapter(customAdapter);

            // Обработка нажатий на элементы списка
            listView.setOnItemClickListener((parent, view, position, id) -> {
                int userID = usersList.get(position).id;

                Fragment targetFragment = new FragmentAboutBook();
                Bundle bundle = new Bundle();
                bundle.putInt("kitobId", userID); // Передача данных
                targetFragment.setArguments(bundle);

                // Замена текущего фрагмента на целевой
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, targetFragment);
                transaction.addToBackStack(null); // Добавляем в стек для возврата
                transaction.commit();
            });
        }
    }



}