package com.example.kutubxona;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class Fragment_Kategoriya extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Kategoriya() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Fragment_Kategoriya newInstance(String param1, String param2) {
        Fragment_Kategoriya fragment = new Fragment_Kategoriya();
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

    private SQLiteDatabase db;
    ListView listView;
    List<Kategoriya> usersList;
    AdapterKategoriya customAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kategoriya, container, false);
        listView = view.findViewById(R.id.lvkategoriya);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        DbHelperKategoriya DbHelperKategoriya = new DbHelperKategoriya(requireContext());
        usersList = DbHelperKategoriya.readAllKat(0);

        if (usersList == null || usersList.isEmpty()) {
            Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
        }
        else {

            customAdapter = new AdapterKategoriya(requireContext(), usersList);
            listView.setAdapter(customAdapter);

            // Обработка нажатий на элементы списка
            listView.setOnItemClickListener((parent, view, position, id) -> {
                int userId = usersList.get(position).id;

                Fragment targetFragment = new Fragment_Glavniy();
                Bundle bundle = new Bundle();
                bundle.putInt("kategoriyaId", userId); // Передача данных
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