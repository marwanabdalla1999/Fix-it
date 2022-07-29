package com.example.eslah;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class transactions_adpt extends RecyclerView.Adapter<transactions_adpt.Holder> {
    ArrayList<transactions_model> transactions_models;
    Context context;
    apis_interface apiService;
    userdata userdata;
    public transactions_adpt(ArrayList<transactions_model> transactions_models, Context context) {
        this.transactions_models = transactions_models;
        this.context = context;
        apiService = client.start().create(apis_interface.class);
        userdata= getuserdata();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.transactions,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.name.setText(transactions_models.get(position).getName());
            if (Double.parseDouble(transactions_models.get(position).getAmount())<0){
                holder.amount.setTextColor(Color.RED);
                holder.amount.setText(transactions_models.get(position).getAmount()+" EGP");

            }
            else {
                holder.amount.setTextColor(Color.rgb(0,142,6));
                holder.amount.setText("+"+transactions_models.get(position).getAmount()+" EGP");

            }
        holder.date.setText(transactions_models.get(position).getDate().split("T")[0]);

    }

    @Override
    public int getItemCount() {
        return transactions_models.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView amount,name,date;

        public Holder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            amount=itemView.findViewById(R.id.amount);
            date=itemView.findViewById(R.id.date);


        }
    }

    private userdata getuserdata() {
        return shared_preference.get_user_data(context);


    }
}
