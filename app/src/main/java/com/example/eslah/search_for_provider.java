package com.example.eslah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class search_for_provider extends AppCompatActivity {
    recyclerviewadapter recyclerviewadapter;
    ArrayList<branchmodel> branchmodels;
    RecyclerView recylerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_provider);
        recylerview=findViewById(R.id.recyclerview);
        branchmodels=new ArrayList<>();


        recyclerviewadapter=new recyclerviewadapter(branchmodels,this);
        recylerview.setAdapter(recyclerviewadapter);
        recylerview.setLayoutManager(new LinearLayoutManager(this));
        recylerview.setNestedScrollingEnabled(false);
    }
}