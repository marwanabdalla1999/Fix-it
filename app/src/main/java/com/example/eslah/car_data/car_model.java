package com.example.eslah.car_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class car_model {


    @SerializedName("model")
    @Expose
    String model;


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
