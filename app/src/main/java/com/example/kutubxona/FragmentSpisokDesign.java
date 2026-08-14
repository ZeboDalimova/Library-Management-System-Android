package com.example.kutubxona;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class FragmentSpisokDesign extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSpisokDesign() {
        // Required empty public constructor
    }

    public static FragmentSpisokDesign newInstance(String param1, String param2) {
        FragmentSpisokDesign fragment = new FragmentSpisokDesign();
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


    List<Design> usersList;
    ListView listView;
    AdapterDesignListAdmin customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_spisok_design, container, false);
        listView = view.findViewById(R.id.listDesignesAdmin);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DbHelperDesign dbHelperKategoriya = new DbHelperDesign(requireContext());
        usersList = dbHelperKategoriya.readAllKat(0);

        if (usersList == null || usersList.isEmpty()) {
            Toast.makeText(requireContext(), "Нет данных для отображения", Toast.LENGTH_SHORT).show();
        }
        else {
            customAdapter = new AdapterDesignListAdmin(requireContext(), usersList,getParentFragmentManager());
            listView.setAdapter(customAdapter);

        }


    }
}