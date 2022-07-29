package com.example.eslah;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Choose_car_adpt extends RecyclerView.Adapter<Choose_car_adpt.Holder> {
        ArrayList<car_model> car_models;
        Context context;
    private Resources res = null;


public Choose_car_adpt(ArrayList<car_model> car_models, Context context) {
        this.car_models = car_models;
        this.context = context;
    res=context.getResources();
        }

@NonNull
@Override
public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.choosen_car_model,parent,false);
        return new Holder(view);
        }

@Override
public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.car.setText(car_models.get(position).getBrand()+" "+car_models.get(position).getModel());


    if (car_models.get(position).getBrand().toLowerCase().equals("citroën")){
        int resourceId = res.getIdentifier("citroen", "drawable",
                context.getPackageName());//initialize res and context in adapter's contructor
        holder.brandicon.setImageResource(resourceId);
    }
    else if (car_models.get(position).getBrand().toLowerCase().equals("rolls-royce")){
        int resourceId = res.getIdentifier("rolls_royce", "drawable",
                context.getPackageName());//initialize res and context in adapter's contructor
        holder.brandicon.setImageResource(resourceId);
    }
    else if (car_models.get(position).getBrand().toLowerCase().equals("mercedes benz")){
        int resourceId = res.getIdentifier("mercedes_benz", "drawable",
                context.getPackageName());//initialize res and context in adapter's contructor
        holder.brandicon.setImageResource(resourceId);
    }
    else if (car_models.get(position).getBrand().toLowerCase().equals("ssang yong")){
        int resourceId = res.getIdentifier("ssang_yong", "drawable",
                context.getPackageName());//initialize res and context in adapter's contructor
        holder.brandicon.setImageResource(resourceId);
    }
    else if (car_models.get(position).getBrand().toLowerCase().equals("land rover")){
        int resourceId = res.getIdentifier("land_rover", "drawable",
                context.getPackageName());//initialize res and context in adapter's contructor
        holder.brandicon.setImageResource(resourceId);
    }
else {
        int resourceId = res.getIdentifier(car_models.get(position).getBrand().toLowerCase(), "drawable",
                context.getPackageName());//initialize res and context in adapter's contructor
        holder.brandicon.setImageResource(resourceId);
    }


        }

@Override
public int getItemCount() {
        return car_models.size();
        }


@Override
public long getItemId(int position) {
        return super.getItemId(position);
        }

public class Holder extends RecyclerView.ViewHolder {
    TextView car;
    ImageView brandicon;
    public Holder(@NonNull View itemView) {
        super(itemView);
        car=itemView.findViewById(R.id.carname);

        brandicon=itemView.findViewById(R.id.brand_icon);
    }
}


}
