package com.example.kutubxona;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.List;


public class FragmentUpdateCategory extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentUpdateCategory() {
        // Required empty public constructor
    }

    public static FragmentUpdateCategory newInstance(String param1, String param2) {
        FragmentUpdateCategory fragment = new FragmentUpdateCategory();
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


    public EditText etname;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Bitmap bitmap;
    private static final int PICK_FILE_REQUEST = 1;
    List<Kategoriya> bookList;
    String turi="not";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_category_update, container, false);
        Button btnSave = (Button) view.findViewById(R.id.btnSaveCategoryUpdate);
        etname = (EditText) view.findViewById(R.id.editTextNameCategoryUpdate);

        DbHelperKategoriya dbHelperKategoriya = new DbHelperKategoriya(requireContext());

        Button btnSelectImage = view.findViewById(R.id.btnSelectImageCatUpdate);
        imageView = view.findViewById(R.id.imageViewCategorySaveUpdate);

        int bookId = getArguments().getInt("categoryId"); // Извлечение данных
        bookList = dbHelperKategoriya.readAllKat(bookId);

        if (bookList == null || bookList.isEmpty()) {
            Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
        }
        else {
            etname.setText(bookList.get(0).kat_name);

            try {
                // Papka yo'lini aniqlaymiz
                File directory = new File(requireContext().getFilesDir(), "categoryPhotos");
                File file = new File(directory, bookList.get(0).image);

                // Rasmni yuklaymiz
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turi="image";
                openFilePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookname = etname.getText().toString().trim();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String userPhoto = "category_";

                if (bookList.get(0).image!=null)
                {
                    long millis = (long) (timestamp.getTime());
                    userPhoto += Long.toString(millis) + ".png";
                    saveImageToInternalStorage(bitmap, userPhoto);
                    deleteFileFromInternalStorage("categoryPhotos",bookList.get(0).image);
                }
                else
                {
                    long millis = (long) (timestamp.getTime());
                    userPhoto += Long.toString(millis) + ".png";
                    saveImageToInternalStorage(bitmap, userPhoto);
                }

                Kategoriya p=new Kategoriya(bookId,bookname,userPhoto);
                if (dbHelperKategoriya.updateKat(p) != -1) {
                    clearBox();
                    Fragment targetFragment = new FragmentSpisokCategory2();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, targetFragment);
                    transaction.addToBackStack(null); // Добавляем в стек для возврата
                    transaction.commit();
                } else {
                    Toast.makeText(requireContext(), "Ma'lumot yangilashda xatolik.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
    public void saveImageToInternalStorage(Bitmap bitmap, String fileName) {
        try {
            String folderPath = new File(requireContext().getFilesDir(), "categoryPhotos").getAbsolutePath();
            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(folderPath, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Невозможно сохранить файл", Toast.LENGTH_SHORT).show();
        }
    }
    public void clearBox(){
        etname.setText("");
        imageView.setImageDrawable(null);
    }
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Укажите MIME-тип для выбора любых файлов
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Выберите файл"), PICK_FILE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri imageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                // Rasmni ImageView da ko'rsatish
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Xatolik yuz berdi", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getPathFromUri(Uri uri) {
        String path = null;
        if (uri != null && "content".equals(uri.getScheme())) {
            Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (columnIndex != -1) {
                    path = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else if (uri != null) {
            path = uri.getPath();
        }
        return path;
    }
    private void deleteFileFromInternalStorage(String folderName, String fileName) {
        try {
            // Создаём путь к папке и файлу
            File folder = new File(requireContext().getFilesDir(), folderName);
            File fileToDelete = new File(folder, fileName);

            // Проверяем, существует ли файл, и удаляем его
            if (fileToDelete.exists()) {
                if (fileToDelete.delete()) {
                } else {
                    Toast.makeText(requireContext(), "Не удалось удалить файл", Toast.LENGTH_SHORT).show();
                }
            } else {
//                Toast.makeText(requireContext(), "Файл не найден", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Ошибка при удалении файла", Toast.LENGTH_SHORT).show();
        }
    }




}