package com.example.eslah;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class client  {


    static final String BASE_URL = "https://fix--it-car-repair.herokuapp.com";

    public static Retrofit start(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)

                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;
    }



}
