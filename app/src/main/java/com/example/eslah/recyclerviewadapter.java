package com.example.eslah;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.eslah.MainActivity2.isEmpty;

public class recyclerviewadapter extends RecyclerView.Adapter<recyclerviewadapter.Holder> {
    ArrayList<branchmodel> branchmodels;
    Context context;
    int progress=20;
    Handler handler;
    Runnable runnable;
    apis_interface apiService;
    userdata userdata;
    public recyclerviewadapter(ArrayList<branchmodel> branchmodels, Context context) {
        this.branchmodels = branchmodels;
        this.context = context;
        apiService = client.start().create(apis_interface.class);
        userdata= getuserdata();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.branchmodel,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.time.setText(branchmodels.get(position).gettime());
        holder.distance.setText(Double.parseDouble(branchmodels.get(position).getDistance())/1000+" KM");
        holder.price.setText(branchmodels.get(position).getAmount()+" EGP");


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.Accept_progress.setVisibility(View.VISIBLE);
                holder.accept.setVisibility(View.GONE);
                Call<String> update_order_data=apiService.update_order_data(userdata.getId(),userdata.getToken(),Integer.toString(branchmodels.get(position).getId()),"in progress",branchmodels.get(position).getProvider_id(),branchmodels.get(position).getAmount(),branchmodels.get(position).gettime(),branchmodels.get(position).getDistance(),branchmodels.get(position).getTechnican_location());
               update_order_data.enqueue(new Callback<String>() {
                   @Override
                   public void onResponse(Call<String> call, Response<String> response) {
                       holder.Accept_progress.setVisibility(View.GONE);
                       holder.accept.setVisibility(View.VISIBLE);

                       if (response.isSuccessful()){
                           if (response.body().toString().equals("order assigned")){
                               if (branchmodels!=null && branchmodels.size()>0 && branchmodels.get(position)!=null) {
                                   new FCMMessages().sendMessageSingle(context, branchmodels.get(position).getDevice_token(), userdata.getName()+" has been accept your offer", "order has been started go to help him now.", null);
                               }
                        Call<String> reserve_tech=apiService.reserve_tech(branchmodels.get(position).getProvider_id());
                        reserve_tech.enqueue(new Callback<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()){
                              if (response.body().equals("deleted")){
                                    shared_preference.setstate(context,"");
                                  Intent i=new Intent(context,summery.class);

                                context.startActivity(i);
                                  ((Activity)context).finish();


                              }
                            }}

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                          }
                           else {

                               Toast.makeText(context, "Cant be assigned to this technician", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }

                   @Override
                   public void onFailure(Call<String> call, Throwable t) {
                       holder.Accept_progress.setVisibility(View.GONE);
                       holder.accept.setVisibility(View.VISIBLE);

                   }
               });


            }
        });

    }

    @Override
    public int getItemCount() {
        return branchmodels.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView time,distance,price;
        Button accept;
        ProgressBar Accept_progress;
        public Holder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.time);
            distance=itemView.findViewById(R.id.distance);
            price=itemView.findViewById(R.id.price);
            accept=itemView.findViewById(R.id.accept);
            Accept_progress=itemView.findViewById(R.id.Accept_progress);

        }
    }

    private userdata getuserdata() {
        return shared_preference.get_user_data(context);


    }
}
