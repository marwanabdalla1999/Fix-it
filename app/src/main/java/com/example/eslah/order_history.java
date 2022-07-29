package com.example.eslah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class order_history extends AppCompatActivity {
    userdata userdata;
    apis_interface apiService;
    RecyclerView my_orders_recycler;
    ArrayList<order_model> order_models;
    myorder_adpter myorder_adpter;
    LottieAnimationView lottieAnimationView;
    ImageView back;
    TextView number_orders;
    TextView dont__make_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        my_orders_recycler=findViewById(R.id.my_orders);
        lottieAnimationView=findViewById(R.id.loading);
        back=findViewById(R.id.back_my_orders);
        dont__make_request=findViewById(R.id.dont__make_request);
        number_orders=findViewById(R.id.number_orders);
        back.setOnClickListener(i->onBackPressed());
        userdata= getuserdata();
        apiService = client.start().create(apis_interface.class);
        order_models=new ArrayList<>();
        myorder_adpter=new myorder_adpter(order_models,this);
        my_orders_recycler.setAdapter(myorder_adpter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
        linearLayoutManager.setStackFromEnd(true);
        my_orders_recycler.setLayoutManager(linearLayoutManager);


        getmyOrders();
    }

    private void getmyOrders() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        my_orders_recycler.setVisibility(View.INVISIBLE);
        dont__make_request.setVisibility(View.GONE);
        my_orders_recycler.setEnabled(false);
        Call<ArrayList<order_model>> getcars=apiService.get_user_history(userdata.getId(),userdata.getToken());
        getcars.enqueue(new Callback<ArrayList<order_model>>() {
            @Override
            public void onResponse(Call<ArrayList<order_model>> call, Response<ArrayList<order_model>> response) {
                lottieAnimationView.setVisibility(View.INVISIBLE);


                if (response.isSuccessful()){
                    if(response.body().size()<=0){
                        dont__make_request.setVisibility(View.VISIBLE);
                        my_orders_recycler.setVisibility(View.INVISIBLE);
                    }
                    else {
                        dont__make_request.setVisibility(View.GONE);
                        my_orders_recycler.setVisibility(View.VISIBLE);
                        my_orders_recycler.setEnabled(true);
                        order_models.clear();
                        for (int i = 0; i < response.body().size(); i++) {


                            order_models.add(response.body().get(i));

                        }
                        myorder_adpter.notifyDataSetChanged();

                    }
getOrders_today(response.body());

                }
            }

            @Override
            public void onFailure(Call<ArrayList<order_model>> call, Throwable t) {
                lottieAnimationView.setVisibility(View.INVISIBLE);
                dont__make_request.setVisibility(View.VISIBLE);
                my_orders_recycler.setVisibility(View.INVISIBLE);
            }
        });



    }
    private userdata getuserdata() {
        return shared_preference.get_user_data(this);


    }
    private void getOrders_today(ArrayList<order_model> body) {
        int number_of_orders=0;
        for (int i=0 ;i<body.size();i++){
            if (body.get(i).getCreated_at().split("T")[0].equals(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()))){
                number_of_orders++;

            }


        }
        number_orders.setText(Integer.toString(number_of_orders));

    }
}