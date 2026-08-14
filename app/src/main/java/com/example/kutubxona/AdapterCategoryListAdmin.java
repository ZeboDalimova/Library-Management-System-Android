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
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import androidx.fragment.app.FragmentManager;

public class AdapterCategoryListAdmin extends BaseAdapter {
    List<Kategoriya> bookList;
    LayoutInflater layoutInflater;
    Context context;
    DbHelperKategoriya dbHelper;
    FragmentManager fragmentManager;

    public AdapterCategoryListAdmin(Context context, List<Kategoriya> bookList, FragmentManager fragmentManager) {
        this.context = context;
        this.bookList = bookList;
        this.fragmentManager = fragmentManager;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelper = new DbHelperKategoriya(this.context);
    }
    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.for_category_list_admin, null);
        }
        Kategoriya book = bookList.get(position);
        TextView textFirstname = (TextView) convertView.findViewById(R.id.kategoriya_nomi_for_listAdmin);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.kategoriya_image_for_listAdmin);
        Button updateButton = convertView.findViewById(R.id.spisok_category_admin_updateAdmin);
        Button deleteButton = convertView.findViewById(R.id.spisok_category_admin_deleteAdmin);
        textFirstname.setText(book.kat_name);


        try {
            // Papka yo'lini aniqlaymiz
            File directory = new File(context.getFilesDir(), "categoryPhotos");
            File file = new File(directory, book.image);

            // Rasmni yuklaymiz
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        updateButton.setOnClickListener(v -> {
            Fragment targetFragment = new FragmentUpdateCategory();
            Bundle bundle = new Bundle();
            bundle.putInt("categoryId", book.id); // Передача данных
            targetFragment.setArguments(bundle);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_layout, targetFragment);
            transaction.addToBackStack(null); // Добавляем в стек для возврата
            transaction.commit();
        });

        deleteButton.setOnClickListener(v -> {
            dbHelper.deleteKat(book.id);
            if(book.image !=null)
            {
                deleteFileFromInternalStorage("categoryPhotos",book.image);
            }
            bookList.remove(position); // Удаляем элемент из списка
            notifyDataSetChanged();
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
