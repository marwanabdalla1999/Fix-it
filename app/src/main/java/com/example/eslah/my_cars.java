package com.example.eslah;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class my_cars extends AppCompatActivity {
   Button add_new_car;
    userdata userdata;
    apis_interface apiService;
    RecyclerView mycars_recycler;
    ArrayList<car_model> car_models;
    mycars_adpt mycars_adpt;
    LottieAnimationView lottieAnimationView;
    ImageView back;
    TextView dont_add_car;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cars);
        mycars_recycler=findViewById(R.id.my_cars);
        add_new_car=findViewById(R.id.add_new_car);
        lottieAnimationView=findViewById(R.id.loading);
        back=findViewById(R.id.back_my_cars);
         dont_add_car=findViewById(R.id.dont_add_car);
        back.setOnClickListener(i->onBackPressed());
        userdata= getuserdata();
        apiService = client.start().create(apis_interface.class);
        car_models=new ArrayList<>();
        mycars_adpt=new mycars_adpt(car_models,this);
        mycars_recycler.setAdapter(mycars_adpt);
        mycars_recycler.setLayoutManager(new LinearLayoutManager(this));
        add_new_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(my_cars.this,new_car.class);
                startActivityForResult(intent,15);

            }
        });
        
        getmycars();
        
    }

    private void getmycars() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        mycars_recycler.setVisibility(View.INVISIBLE);
        dont_add_car.setVisibility(View.GONE);
        mycars_recycler.setEnabled(false);
        Call<ArrayList<car_model>> getcars=apiService.get_user_data(userdata.getId(),userdata.getToken());
        getcars.enqueue(new Callback<ArrayList<car_model>>() {
            @Override
            public void onResponse(Call<ArrayList<car_model>> call, Response<ArrayList<car_model>> response) {
                lottieAnimationView.setVisibility(View.INVISIBLE);


                if (response.isSuccessful()){
                    if(response.body().size()<=0){

                        dont_add_car.setVisibility(View.VISIBLE);
                        mycars_recycler.setVisibility(View.INVISIBLE);

                    }else {
                        dont_add_car.setVisibility(View.GONE);
                        mycars_recycler.setVisibility(View.VISIBLE);
                        mycars_recycler.setEnabled(true);
                        car_models.clear();
                        for (int i = 0; i < response.body().size(); i++) {


                            car_models.add(response.body().get(i));

                        }
                        mycars_adpt.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<car_model>> call, Throwable t) {
                lottieAnimationView.setVisibility(View.INVISIBLE);
                dont_add_car.setVisibility(View.VISIBLE);
                mycars_recycler.setVisibility(View.INVISIBLE);
            }
        });



    }
    private userdata getuserdata() {
        return shared_preference.get_user_data(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==15){
            if (resultCode==RESULT_OK){
                getmycars();

            }

        }
    }
}