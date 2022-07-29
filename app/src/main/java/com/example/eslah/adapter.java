package com.example.eslah;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class adapter extends FragmentStateAdapter {

    public adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);


    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 1:
               return new page2();
           case 2:
               return new tracking();


       }
        return new quick_order();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
