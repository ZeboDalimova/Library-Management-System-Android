package com.example.kutubxona;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

public class Fragment_update_book extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Fragment_update_book() {
    }
    public static Fragment_update_book newInstance(String param1, String param2) {
        Fragment_update_book fragment = new Fragment_update_book();
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
    List<Book> bookList;
    Uri uriG,uriA,uriF;
    String uniqueFileName,uniqueAudioName;
    String turi="not";
    String turi1="not",turi2="not",turi3="not";

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_update_book, container, false);
        btnSave = (Button) view.findViewById(R.id.btnSaveBookUpdate);

        etname=(EditText) view.findViewById(R.id.editTextBooknameUpdate);
        avtor=(EditText) view.findViewById(R.id.editTextBookavtorUpdate);
        etOp = (EditText) view.findViewById(R.id.editTextOpisaniyaUpdate);
        imageView = view.findViewById(R.id.imageViewBookSaveUpdate);
        btnImage=(Button) view.findViewById(R.id.btnSelectImageUpdate);
        btnAudio=(Button) view.findViewById(R.id.btnSelectAudioUpdate);
        btnFile=(Button) view.findViewById(R.id.btnSelectFileUpdate);
        txtFile=(TextView) view.findViewById(R.id.textViewSelectFileUpdate);
        txtAudio=(TextView) view.findViewById(R.id.textViewSelectAudioUpdate);
        rgroup=(RadioGroup) view.findViewById(R.id.RadioGroupBookKategUpdate);

        rgroup.getCheckedRadioButtonId();
        dbHelperKategoriya=new DbHelperKategoriya(requireContext());
        kategoriyalist=dbHelperKategoriya.readAllKat(0);

        DbHelperBook dbHelperBook = new DbHelperBook(requireContext());
        int bookId = getArguments().getInt("kitobId"); // Извлечение данных
        bookList = dbHelperBook.readAllBook(bookId);

        for(int i = 0; i < kategoriyalist.size(); i++){
            RadioButton rb = new RadioButton(requireContext());
            if(kategoriyalist.get(i).id==bookList.get(0).kateg_id)
            {
                rb.setChecked(true);
            }
            rb.setText(kategoriyalist.get(i).kat_name);
            rb.setId(kategoriyalist.get(i).id);
            rb.setTextColor(R.color.black);
            rgroup.addView(rb);
        }


        if (bookList == null || bookList.isEmpty()) {
            Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
        }
        else {
            etname.setText(bookList.get(0).kitobname);
            avtor.setText(bookList.get(0).komment);
            etOp.setText(bookList.get(0).opesan);

            try {
                // Papka yo'lini aniqlaymiz
                File directory = new File(requireContext().getFilesDir(), "myPhotos");
                File file = new File(directory, bookList.get(0).rasm_id);

                // Rasmni yuklaymiz
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

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

//                if (txtFile.getText().toString().trim().equals("exemple.pdf"))
//                {
//                    uniqueFileName=bookList.get(0).fayl_id;
//                }
//                else
//                {
//                    if (bookList.get(0).fayl_id!=null)
//                    {
//                        uniqueFileName = "book_file_" + System.currentTimeMillis() + ".pdf";
//                        saveFileToInternalStorage(uriF, "myFiles", uniqueFileName);
//                        deleteFileFromInternalStorage("myFiles",bookList.get(0).fayl_id);
//                    }
//                    else
//                    {
//                        uniqueFileName = "book_file_" + System.currentTimeMillis() + ".pdf";
//                        saveFileToInternalStorage(uriF, "myFiles", uniqueFileName);
//                    }
//
//                }
//
//
//                if (txtAudio.getText().toString().trim().equals("exemple.mp3"))
//                {
//                    uniqueAudioName=bookList.get(0).audio_id;
//                }
//                else
//                {
//                    if (bookList.get(0).audio_id!=null)
//                    {
//                        uniqueAudioName = "book_audio_" + System.currentTimeMillis() + ".mp3";
//                        saveMusicFileToInternalStorage(uriF, "myAudios", uniqueAudioName);
//                        deleteFileFromInternalStorage("myAudios",bookList.get(0).audio_id);
//                    }
//                    else
//                    {
//                        uniqueAudioName = "book_audio_" + System.currentTimeMillis() + ".mp3";
//                        saveMusicFileToInternalStorage(uriF, "myAudios", uniqueAudioName);
//                    }
//
//                }

                if(bookList.get(0).fayl_id!=null)
                {
                    deleteFileFromInternalStorage("myFiles",bookList.get(0).fayl_id);
                }
                if(bookList.get(0).audio_id!=null)
                {
                    deleteFileFromInternalStorage("myAudios",bookList.get(0).audio_id);
                }

                if(txtAudio.getText().toString().trim().equals("exemple.mp3"))
                {
                    uniqueAudioName=bookList.get(0).audio_id;
                }
                if(txtFile.getText().toString().trim().equals("exemple.pdf"))
                {
                    uniqueAudioName=bookList.get(0).fayl_id;
                }
                Book p = new Book(bookId,bookname, bookkat, userPhoto, bookop, uniqueFileName, uniqueAudioName, bookavtor);
                if (dbHelperBook.updateBook(p) != -1) {
                    clearBox();
                    Fragment targetFragment = new FragmentSpisokKnig();
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

//            Toast.makeText(requireContext(), "Файл сохранен: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Ошибка сохранения файла", Toast.LENGTH_LONG).show();
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