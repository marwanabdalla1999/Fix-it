package com.example.eslah.car_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class car_brands {

    @SerializedName("brand")
    @Expose
    String brand;

    @SerializedName("models")
    @Expose
    ArrayList<car_model> car_models;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ArrayList<car_model> getCar_models() {
        return car_models;
    }

    public void setCar_models(ArrayList<car_model> car_models) {
        this.car_models = car_models;
    }
}
