package com.example.eslah;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class quick_order extends Fragment {

   recyclerviewadapter recyclerviewadapter;
    ArrayList<branchmodel> branchmodels;
RecyclerView recylerview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_quick_order, container, false);
        recylerview=view.findViewById(R.id.recylerview);
        branchmodels=new ArrayList<>();
        recyclerviewadapter=new recyclerviewadapter(branchmodels,getContext());
        recylerview.setAdapter(recyclerviewadapter);
        recylerview.setLayoutManager(new GridLayoutManager(getContext(),2));
        recylerview.setNestedScrollingEnabled(false);
    return view;
    }
}