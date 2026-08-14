package com.example.kutubxona;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAddDesign#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddDesign extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAddDesign() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDesignFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddDesign newInstance(String param1, String param2) {
        FragmentAddDesign fragment = new FragmentAddDesign();
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



    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Bitmap bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_design, container, false);

        Button btnSave = (Button) view.findViewById(R.id.btnSaveDesign);

        DbHelperDesign dbHelperKategoriya = new DbHelperDesign(requireContext());

        Button btnSelectImage = view.findViewById(R.id.btnSelectImageDesign);
        imageView = view.findViewById(R.id.imageViewDesignSave);
        btnSelectImage.setOnClickListener(v -> openGallery());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //rasm nomini generatsiya qilish
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String userPhoto = "design_";
                long millis = (long) (timestamp.getTime());
                userPhoto += Long.toString(millis) + ".png";

                saveImageToInternalStorage(bitmap, userPhoto); // rasmni saqlash

                Design p = new Design( userPhoto);
                if(dbHelperKategoriya.inserKat(p) != -1){
                    Toast.makeText(requireContext(), "Ma'lumot qo'shildi.", Toast.LENGTH_SHORT).show();
                    clearBox();
                }else{
                    Toast.makeText(requireContext(), "Ma'lumot qo'shishda xatolik.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }



    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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

//            Toast.makeText(requireContext(), "Rasm saqlandi: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Saqlashda xatolik yuz berdi", Toast.LENGTH_SHORT).show();
        }
    }
    public void clearBox(){
        imageView.setImageDrawable(null);
    }
}
