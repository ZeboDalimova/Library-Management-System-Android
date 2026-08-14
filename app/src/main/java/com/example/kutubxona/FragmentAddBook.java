package com.example.kutubxona;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

public class FragmentAddBook extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public FragmentAddBook() {
        // Required empty public constructor
    }
    public static FragmentAddBook newInstance(String param1, String param2) {
        FragmentAddBook fragment = new FragmentAddBook();
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




    private static final int PICK_FILE_REQUEST = 1;
    private static final int PERMISSION_STORAGE = 100;
    public EditText etname, etOp,avtor;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Bitmap bitmap;
    Button btnImage,btnAudio,btnFile,btnSave;
    TextView txtAudio,txtFile;
    RadioGroup rgroup;
    DbHelperKategoriya dbHelperKategoriya;
    List<Kategoriya> kategoriyalist;
    Uri uriG,uriA,uriF;
    String uniqueFileName,uniqueAudioName;
    String turi1="not",turi2="not",turi3="not";

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        btnSave = (Button) view.findViewById(R.id.btnSaveBook);

        etname=(EditText) view.findViewById(R.id.editTextBookname);
        avtor=(EditText) view.findViewById(R.id.editTextBookavtor);
        etOp = (EditText) view.findViewById(R.id.editTextOpisaniya);
        imageView = view.findViewById(R.id.imageViewBookSave);
        btnImage=(Button) view.findViewById(R.id.btnSelectImage);
        btnAudio=(Button) view.findViewById(R.id.btnSelectAudio);
        btnFile=(Button) view.findViewById(R.id.btnSelectFile);
        txtFile=(TextView) view.findViewById(R.id.textViewSelectFile);
        txtAudio=(TextView) view.findViewById(R.id.textViewSelectAudio);
        rgroup=(RadioGroup) view.findViewById(R.id.RadioGroupBookKateg);

        rgroup.getCheckedRadioButtonId();
        dbHelperKategoriya=new DbHelperKategoriya(requireContext());
        kategoriyalist=dbHelperKategoriya.readAllKat(0);

        for(int i = 0; i < kategoriyalist.size(); i++){
            RadioButton rb = new RadioButton(requireContext());
            rb.setText(kategoriyalist.get(i).kat_name);
            rb.setId(kategoriyalist.get(i).id);
            rb.setTextColor(R.color.black);
            rgroup.addView(rb);
        }
        DbHelperBook dbHelperBook = new DbHelperBook(requireContext());


        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turi1="file";
                uniqueFileName = "book_file_" + System.currentTimeMillis() + ".pdf";
                openFilePicker();

            }
        });
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turi2="audio";
                uniqueAudioName = "book_audio_" + System.currentTimeMillis() + ".mp3";
                openFilePicker();
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turi3="image";
                openFilePicker();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookname = etname.getText().toString().trim();
                String bookop = etOp.getText().toString().trim();
                String bookavtor = avtor.getText().toString().trim();
                int bookkat = rgroup.getCheckedRadioButtonId();


                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String userPhoto = "PhotoBook_";
                long millis = (long) (timestamp.getTime());
                userPhoto += Long.toString(millis) + ".png";
                saveImageToInternalStorage(bitmap, userPhoto);



                Book p = new Book(bookname, bookkat, userPhoto, bookop, uniqueFileName, uniqueAudioName, bookavtor);
                if (dbHelperBook.insertBook(p) != -1) {
                    clearBox();
                } else {
                    Toast.makeText(requireContext(), "Ma'lumot qo'shishda xatolik.", Toast.LENGTH_SHORT).show();
                }
            }



        });
        return view;
    }

    private void saveMusicFileToInternalStorage(Uri fileUri, String folderName, String fileName) {
        try {
            // Создаем папку для хранения файлов, если её нет
            File folder = new File(requireContext().getFilesDir(), folderName);
            if (!folder.exists()) {
                folder.mkdir();
            }

            // Генерируем уникальное имя для файла
            String uniqueFileName = fileName; // или ".wav", в зависимости от типа

            File outputFile = new File(folder, uniqueFileName);

            // Получаем входной поток из URI
            InputStream inputStream = requireContext().getContentResolver().openInputStream(fileUri);

            // Запись в файл
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Закрытие потоков
            inputStream.close();
            outputStream.close();

            Toast.makeText(requireContext(), "Файл сохранен: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Ошибка сохранения файла", Toast.LENGTH_LONG).show();
        }
    }
    public void saveImageToInternalStorage(Bitmap bitmap, String fileName) {
        try {
            String folderPath = new File(requireContext().getFilesDir(), "myPhotos").getAbsolutePath();
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
        etOp.setText("");
        imageView.setImageDrawable(null);
        txtFile.setText("exemple.pdf");
        txtAudio.setText("exemple.mp3");
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

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData(); // Получение URI выбранного файла
            String filePath = getPathFromUri(uri); // Преобразование URI в путь
            String mimeType = requireContext().getContentResolver().getType(uri);
            if(turi1.equals("file"))
            {
                if (filePath != null && filePath.contains(".pdf")==true) {
                    txtFile.setText(filePath);
                    saveFileToInternalStorage(uri,"myFiles", uniqueFileName);
                }
                else {
                    Toast.makeText(requireContext(), "Невозможно получить путь к файлу", Toast.LENGTH_LONG).show();
                }
                turi1="not";
            }
            if(turi2.equals("audio"))
            {
                if ("audio/mpeg".equals(mimeType) || "audio/wav".equals(mimeType)) {
                    txtAudio.setText(filePath);
                    saveFileToInternalStorage(uri,"myAudios", uniqueAudioName);
                }
                else {
                    Toast.makeText(requireContext(), "Невозможно получить путь к файлу", Toast.LENGTH_LONG).show();
                }
                turi2="not";
            }
            if(turi3.equals("image"))
            {
                if (filePath != null && ((filePath.contains(".png"))||filePath.contains(".jpg")||filePath.contains(".jpeg"))) {
                    try {
//                    Uri imageUri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                        // Rasmni ImageView da ko'rsatish
                        uriG=uri;
                        imageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(requireContext(), "Невозможно получить путь к файлу", Toast.LENGTH_LONG).show();
                }
                turi3="not";
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


    private void saveFileToInternalStorage(Uri fileUri, String folderName, String fileName) {
        try {
            // Создаём папку myfiles внутри files

            File folder = new File(requireContext().getFilesDir(), folderName);
            if (!folder.exists()) {
                folder.mkdir(); // Создаём папку, если её ещё нет
            }

            File outputFile = new File(folder, fileName);

            // Получение входного потока из URI
            InputStream inputStream = requireContext().getContentResolver().openInputStream(fileUri);

            // Запись файла
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Невозможно сохранить файл", Toast.LENGTH_SHORT).show();
        }
    }


}
