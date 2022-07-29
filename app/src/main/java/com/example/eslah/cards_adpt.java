package com.example.eslah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class cards_adpt extends RecyclerView.Adapter<cards_adpt.Holder> {
    ArrayList<cards_model> cards_models;
    Context context;
    apis_interface apiService;
    userdata userdata;
    Boolean delete_option;
    public cards_adpt(ArrayList<cards_model> cards_models, Context context,Boolean delete_option) {
        this.cards_models = cards_models;
        this.context = context;
        this.delete_option=delete_option;
        apiService = client.start().create(apis_interface.class);
        userdata= getuserdata();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.card_model,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.cardnumber.setText(cards_models.get(position).getMasked_pan());
                if(cards_models.get(position).getType().equals("MasterCard")){
                    holder.type.setImageResource(R.drawable.mastercard);

                }
                else if(cards_models.get(position).getType().equals("Visa")) {
                    holder.type.setImageResource(R.drawable.visa);
                }
        holder.delete_option_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<String> delete_card=apiService.delete_card(cards_models.get(position).getId());
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

                cards_models.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cards_models.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView cardnumber;
        ImageView delete_option_btn,type;
        public Holder(@NonNull View itemView) {
            super(itemView);
            cardnumber=itemView.findViewById(R.id.cardnumber);
            delete_option_btn=itemView.findViewById(R.id.delete_option);
            type=itemView.findViewById(R.id.type);
            if (!delete_option){
                delete_option_btn.setVisibility(View.GONE);

            }
        }
    }

    private userdata getuserdata() {
        return shared_preference.get_user_data(context);


    }
}
