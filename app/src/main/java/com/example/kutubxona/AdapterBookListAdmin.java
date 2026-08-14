package com.example.kutubxona;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class AdapterBookListAdmin extends BaseAdapter {
    private Context context;
    private List<Book> books;
    LayoutInflater layoutInflater;
    DbHelperBook dbHelper;
    List<Kategoriya> kategoriyaList;
    private FragmentManager fragmentManager;

    public AdapterBookListAdmin(Context context, List<Book> books, FragmentManager fragmentManager) {
        this.context = context;
        this.books = books;
        this.fragmentManager = fragmentManager;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelper = new DbHelperBook(this.context);
    }

    @Override
    public int getCount() {
        return books.size();
    }
    @Override
    public Object getItem(int position) {
        return books.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.for_kitob_list_admin, parent, false);
        }



        // Получение данных текущей книги
        Book book = books.get(position);

        // Настройка элементов UI
        TextView nameTextView = convertView.findViewById(R.id.k_a_name);
        TextView authorTextView = convertView.findViewById(R.id.k_a_avtor);
        TextView textKat = convertView.findViewById(R.id.k_a_kat);
        Button updateButton = convertView.findViewById(R.id.spisok_knig_admin_update);
        Button deleteButton = convertView.findViewById(R.id.spisok_knig_admin_delete);
        ImageView imageUser = (ImageView) convertView.findViewById(R.id.k_a_rasm);

        nameTextView.setText(book.kitobname);
        authorTextView.setText(book.komment);

        DbHelperKategoriya dbHelperKategoriya = new DbHelperKategoriya(context);
        kategoriyaList = dbHelperKategoriya.readAllKat(book.kateg_id);
        textKat.setText(String.valueOf(kategoriyaList.get(0).kat_name));

        try {
            // Papka yo'lini aniqlaymiz
            File directory = new File(context.getFilesDir(), "myPhotos");
            File file = new File(directory, book.rasm_id);

            // Rasmni yuklaymiz
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            imageUser.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // Обработка нажатий на кнопки
        updateButton.setOnClickListener(v -> {
            Fragment targetFragment = new Fragment_update_book();
            Bundle bundle = new Bundle();
            bundle.putInt("kitobId", book.id); // Передача данных
            targetFragment.setArguments(bundle);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, targetFragment);
            transaction.addToBackStack(null); // Добавляем в стек для возврата
            transaction.commit();
        });

        deleteButton.setOnClickListener(v -> {
            Toast.makeText(context, "Удалить: " + book.id, Toast.LENGTH_SHORT).show();
            dbHelper.deleteBook(book.id);
            if(book.rasm_id !=null)
            {
                deleteFileFromInternalStorage("myPhotos",book.rasm_id);
            }
            if(book.fayl_id !=null)
            {
                deleteFileFromInternalStorage("myFiles",book.fayl_id);
            }
            if(book.audio_id !=null)
            {
                deleteFileFromInternalStorage("myAudios",book.audio_id);
            }

            FragmentManager fragmentManager = this.fragmentManager;
            fragmentManager.beginTransaction()
                    .detach(fragmentManager.findFragmentById(R.id.frame_layout)) // Отсоединяем текущий фрагмент
                    .attach(fragmentManager.findFragmentById(R.id.frame_layout)) // Заново прикрепляем его
                    .commit();
        });

        return convertView;
    }

    private void deleteFileFromInternalStorage(String folderName, String fileName) {
        try {
            // Создаём путь к папке и файлу
            File folder = new File(context.getFilesDir(), folderName);
            File fileToDelete = new File(folder, fileName);

            // Проверяем, существует ли файл, и удаляем его
            if (fileToDelete.exists()) {
                if (fileToDelete.delete()) {
                } else {
                    Toast.makeText(context, "Не удалось удалить файл", Toast.LENGTH_SHORT).show();
                }
            } else {
//                Toast.makeText(requireContext(), "Файл не найден", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Ошибка при удалении файла", Toast.LENGTH_SHORT).show();
        }
    }

}
