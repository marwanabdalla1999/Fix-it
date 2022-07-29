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

public class chat_adpt extends RecyclerView.Adapter<chat_adpt.Holder> {
    ArrayList<String> messages;
    Context context;
    apis_interface apiService;
    userdata userdata;


    public chat_adpt(ArrayList<String> messages, Context context) {
        this.messages = messages;
        this.context = context;
        apiService = client.start().create(apis_interface.class);
        userdata= getuserdata();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = LayoutInflater.from(context).inflate(R.layout.massage_model_user, parent, false);
        }
    else {
            view = LayoutInflater.from(context).inflate(R.layout.massage_model_tech, parent, false);

        }
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.message.setText(messages.get(position).split("&&&@@@:")[1]);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).startsWith("sender_user"+userdata.getName()+"&&&@@@:")){
            return 1;

        }else {
            return 2;

        }
    }

    

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView message;

        public Holder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message);



        }
    }

    private userdata getuserdata() {
        return shared_preference.get_user_data(context);


    }
}
