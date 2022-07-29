package com.example.eslah;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.zip.Inflater;

public class sliderAdapter  extends RecyclerView.Adapter<sliderAdapter.sliderAdapterHolder> {
        List<slideritem> slideritems;
        ViewPager2 viewPager2;

    public sliderAdapter(List<slideritem> slideritems, ViewPager2 viewPager2) {
        this.slideritems = slideritems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public sliderAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new sliderAdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_container,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull sliderAdapterHolder holder, int position) {
holder.setimg(slideritems.get(position));


    }

    @Override
    public int getItemCount() {
        return slideritems.size();
    }

    public  class sliderAdapterHolder extends RecyclerView.ViewHolder{
    private RoundedImageView imageView;

    public sliderAdapterHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = itemView.findViewById(R.id.roundedimg);
        viewPager2.post(new Runnable()
        {
            @Override
            public void run() {
                notifyDataSetChanged();

            }
        });
    }
    void setimg(slideritem slideritem){
        imageView.setImageResource(slideritem.getimg());
    }
}


}
