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

public class AdapterDesignListAdmin extends BaseAdapter {
    List<Design> bookList;
    LayoutInflater layoutInflater;
    Context context;
    DbHelperDesign dbHelper;
    FragmentManager fragmentManager;

    public AdapterDesignListAdmin(Context context, List<Design> bookList, FragmentManager fragmentManager) {
        this.context = context;
        this.bookList = bookList;
        this.fragmentManager = fragmentManager;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dbHelper = new DbHelperDesign(this.context);
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
            convertView = layoutInflater.inflate(R.layout.for_design_list_admin, null);
        }
        Design book = bookList.get(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.deleteDesignImageAdmin);
        Button deleteButton = convertView.findViewById(R.id.deleteDesignButtonAdmin);


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


        deleteButton.setOnClickListener(v -> {
            dbHelper.deleteKat(book.id);
            if (book.image != null) {
                deleteFileFromInternalStorage("categoryPhotos", book.image);
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
