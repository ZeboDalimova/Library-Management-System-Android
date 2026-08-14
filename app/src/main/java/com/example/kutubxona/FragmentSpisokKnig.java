package com.example.kutubxona;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FragmentSpisokKnig extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public FragmentSpisokKnig() {
        // Required empty public constructor
    }

    public static FragmentSpisokKnig newInstance(String param1, String param2) {
        FragmentSpisokKnig fragment = new FragmentSpisokKnig();
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


    List<Book> usersList;
    List<Book> usersList2;
    ListView listView;
    AdapterBookListAdmin customAdapter;
    Button updateButton, deleteButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spisok_knig, container, false);

        listView = view.findViewById(R.id.listBooksAdminn);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            int katId = getArguments().getInt("kategoriyaId"); // Извлечение данных
            DbHelperBook dbHelperBook = new DbHelperBook(requireContext());
            usersList2 = dbHelperBook.readAllBook(0);

            if (usersList2 == null || usersList2.isEmpty()) {
                Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
            }
            else
            {
                usersList = new ArrayList<>();
                for(int i=0; i<usersList2.size();i++)
                {
                    if(usersList2.get(i).kateg_id==katId)
                    {
                        usersList.add(usersList2.get(i));
                    }
                }
            }
        }
        else
        {
            DbHelperBook dbHelperBook = new DbHelperBook(requireContext());
            usersList = dbHelperBook.readAllBook(0);
        }

        if (usersList == null || usersList.isEmpty()) {
            Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
        }
        else {
            customAdapter = new AdapterBookListAdmin(requireContext(), usersList,getParentFragmentManager());
            listView.setAdapter(customAdapter);

        }


    }
}