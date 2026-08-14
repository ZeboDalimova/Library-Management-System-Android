package com.example.kutubxona;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Glavniy extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public Fragment_Glavniy() {
        // Required empty public constructor
    }
    public static Fragment_Glavniy newInstance(String param1, String param2) {
        Fragment_Glavniy fragment = new Fragment_Glavniy();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void onClick(View view) {
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
    AdapterBook customAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_glavniy, container, false);
        listView = view.findViewById(R.id.lvUsers);

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

            customAdapter = new AdapterBook(requireContext(), usersList);
            listView.setAdapter(customAdapter);

            // Обработка нажатий на элементы списка
            listView.setOnItemClickListener((parent, view, position, id) -> {
                int userId = usersList.get(position).id;

                Fragment targetFragment = new FragmentAboutBook();
                Bundle bundle = new Bundle();
                bundle.putInt("kitobId", userId); // Передача данных
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