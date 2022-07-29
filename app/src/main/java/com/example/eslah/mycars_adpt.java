package com.example.eslah;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class mycars_adpt extends RecyclerView.Adapter<mycars_adpt.Holder> {
        ArrayList<car_model> car_models;
        Context context;
    private Resources res = null;
    apis_interface apiService;


    public mycars_adpt(ArrayList<car_model> car_models, Context context) {
        this.car_models = car_models;
        this.context = context;
    res=context.getResources();
        apiService = client.start().create(apis_interface.class);
        }

@NonNull
@Override
public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.car_model,parent,false);
        return new Holder(view);
        }

@Override
public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.type.setText(car_models.get(position).getType());
        holder.brand.setText(car_models.get(position).getBrand());
        holder.model.setText(car_models.get(position).getModel());
    holder.year.setText(car_models.get(position).getYear());
    holder.color.setText(car_models.get(position).getColor());

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


    holder.delete_car.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Call<String> delete_card=apiService.delete_car(Integer.toString(car_models.get(position).getId()));
            delete_card.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        if(response.body().equals("deleted")){


                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d(TAG, "onResponse: "+t.getMessage());

                }
            });

            car_models.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyDataSetChanged();
        }
    });
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
    TextView type,brand,model,year,color;
    ImageView brandicon,delete_car;
    public Holder(@NonNull View itemView) {
        super(itemView);
        type=itemView.findViewById(R.id.type);
        brand=itemView.findViewById(R.id.Brand);
        model=itemView.findViewById(R.id.model);
        year=itemView.findViewById(R.id.year);
        color=itemView.findViewById(R.id.color);
        brandicon=itemView.findViewById(R.id.brand_icon);
        delete_car=itemView.findViewById(R.id.delete_car);
    }
}


}
