package com.example.eslah.car_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class car_type {

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("brands")
    @Expose
    ArrayList<car_brands> car_brands;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<com.example.eslah.car_data.car_brands> getCar_brands() {
        return car_brands;
    }

    public void setCar_brands(ArrayList<com.example.eslah.car_data.car_brands> car_brands) {
        this.car_brands = car_brands;
    }
}
