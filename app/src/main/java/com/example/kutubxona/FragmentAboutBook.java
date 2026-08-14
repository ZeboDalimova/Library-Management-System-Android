package com.example.kutubxona;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class FragmentAboutBook extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAboutBook() {
        // Required empty public constructor
    }

    public static FragmentAboutBook newInstance(String param1, String param2) {
        FragmentAboutBook fragment = new FragmentAboutBook();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    List<Book> bookList;
    List<Kategoriya> kategoriyaList;
    ImageView image;
    TextView name,kat,op,av;
    Button add,read,audio;
    List<Zakladka> zakladkiList;
    private static final String PREFS_FILE = "Account";
    private static final String PREF_NAME = "Status";
    int zakladka_id=0;
    SharedPreferences settings;
    int bookId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_book, container, false);
        image=view.findViewById(R.id.oblojka_book_about);
        name=view.findViewById(R.id.name_book_about);
        kat=view.findViewById(R.id.kategoriya_book_about);
        op=view.findViewById(R.id.opisaniya_book_about);
        av=view.findViewById(R.id.avtor_book_about);
        add=view.findViewById(R.id.dobavitVIzbr);
        read=view.findViewById(R.id.chitat);
        audio=view.findViewById(R.id.audioAboutBook);

        settings = requireContext().getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        int userId = settings.getInt("Id", 0);
        Boolean LogIn = settings.getBoolean("LogIn", false);

        if (getArguments() != null) {
            bookId = getArguments().getInt("kitobId"); // Извлечение данных
            DbHelperBook dbHelperBook = new DbHelperBook(requireContext());
            bookList = dbHelperBook.readAllBook(bookId);


            if (bookList == null || bookList.isEmpty()) {
                Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
            }
            else
            {
                DbHelperZakladka dbHelperZakladka=new DbHelperZakladka(requireContext());
                zakladkiList=dbHelperZakladka.readAllKat(userId);
                for (int i=0;i<zakladkiList.size();i++)
                {
                    if(zakladkiList.get(i).book_id==bookId)
                    {
                        if(LogIn==true)
                        {
                            add.setText(zakladkiList.get(i).status);
                        }
                        zakladka_id=zakladkiList.get(i).id;
                    }
                }

                op.setText(bookList.get(0).opesan);
                name.setText(bookList.get(0).kitobname);
                av.setText(bookList.get(0).komment);


                DbHelperKategoriya dbHelperKategoriya = new DbHelperKategoriya(requireContext());
                kategoriyaList = dbHelperKategoriya.readAllKat(bookList.get(0).kateg_id);
                kat.setText(String.valueOf(kategoriyaList.get(0).kat_name));

                try {
                    // Papka yo'lini aniqlaymiz
                    File directory = new File(requireContext().getFilesDir(), "myPhotos");
                    File file = new File(directory, bookList.get(0).rasm_id);

                    // Rasmni yuklaymiz
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LogIn==true)
                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
                    builder.setTitle("Выберите цвет");
                    String[] colors;
                    if(add.getText().toString().trim().equals("Добавить"))
                    {
                        colors = new String[]{"Читаю", "В планах", "Прочитано"};
                    }
                    else
                    {
                        colors = new String[]{"Читаю", "В планах", "Прочитано","Удалить"};
                    }
//                String[] colors = {"Читаю", "Буду читать", "Прочитано","Удалить"};
                    int checkedItem = 0; // Индекс выбранного элемента
                    DbHelperZakladka dbHelperZakladka=new DbHelperZakladka(requireContext());
                    builder.setSingleChoiceItems(colors, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Обработка выбора элемента
                            if(add.getText().toString().trim().equals("Добавить"))
                            {
                                Zakladka zakladka=new Zakladka(bookId,userId,colors[which]);
                                if(dbHelperZakladka.inserKat(zakladka) != -1){
//                                Toast.makeText(requireContext(), "Qo'shildi!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(requireContext(), "Qo'shishda xatolik.", Toast.LENGTH_SHORT).show();
                                }
                                add.setText(colors[which]);
                            }
                            else
                            {
                                if (which==3)
                                {
                                    if(dbHelperZakladka.deleteBook(zakladka_id) != -1)
                                    {
                                        Toast.makeText(requireContext(), "Тайтл удалено из списка!", Toast.LENGTH_SHORT).show();
                                        add.setText("Добавить");
                                    }
                                }
                                else
                                {
                                    if(zakladka_id!=0)
                                    {
                                        if(dbHelperZakladka.updateKat(new Zakladka(zakladka_id, bookId, userId,colors[which])) != -1){
                                        }
                                        else{
//                                    Toast.makeText(requireContext(), "Qo'shishda xatolik.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    add.setText(colors[which]);
                                }
                            }

//                        Toast.makeText(requireContext(), "Выбрано: " + colors[which], Toast.LENGTH_SHORT).show();
                            dialog.dismiss(); // Закрыть диалоговое окно после выбора
                        }
                    });
                    builder.create().show();
                }
                else

                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
                    builder.setTitle("Регистрация отсутствует!")
                            .setMessage("Для этого требуется регистрация. Хотите войти в свой аккаунт?")
                            .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Обработка нажатия кнопки "ОК"
                                    Intent intent = new Intent(requireContext(), Activitylogin.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Обработка нажатия кнопки "Отмена"
                                }
                            });

                    android.app.AlertDialog dialog = builder.create();

                    dialog.setOnShowListener(dialogInterface -> {
                        // Устанавливаем цвет сообщения
                        TextView message = dialog.findViewById(android.R.id.message);
                        if (message != null) {
                            message.setTextColor(requireContext().getResources().getColor(android.R.color.black)); // Черный текст сообщения
                        }

                        // Заголовок диалога
                        int titleId = requireContext().getResources().getIdentifier("alertTitle", "id", "android");
                        TextView title = dialog.findViewById(titleId);
                        if (title != null) {
                            title.setTextColor(requireContext().getResources().getColor(android.R.color.black)); // Черный текст заголовка
                        }
                    });

                    if (dialog.getWindow() != null) {
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                    }

                    dialog.show();
                }
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(requireContext(), ActivityReadPdf.class);
                intent.putExtra("kitobIdRead",bookList.get(0).fayl_id);
                startActivity(intent);
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bookList.get(0).audio_id==null)
                {
                    Toast.makeText(requireContext(), "У этой книги нет аудио файла :(", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent=new Intent(requireContext(), ActivityListenAudio.class);
                    intent.putExtra("kitobIdListen",bookList.get(0).audio_id);
                    intent.putExtra("kitobName",bookList.get(0).kitobname);
                    intent.putExtra("kitobImage",bookList.get(0).rasm_id);
                    startActivity(intent);
                }
            }
        });


        return view;
    }

}