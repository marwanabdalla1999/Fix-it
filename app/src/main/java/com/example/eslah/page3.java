package com.example.eslah;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class page3 extends Fragment {

    recyclerviewadapter recyclerviewadapter;
    RecyclerView recyclerView;
    ArrayList<branchmodel> branchmodels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_page3, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);



        return view;

    }
}