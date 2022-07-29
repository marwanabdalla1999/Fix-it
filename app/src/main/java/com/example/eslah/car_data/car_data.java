package com.example.eslah.car_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class car_data {

    @SerializedName("version")
    @Expose
    String version;

    @SerializedName("data")
    @Expose
    ArrayList<car_type> data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<car_type> getData() {
        return data;
    }

    public void setData(ArrayList<car_type> data) {
        this.data = data;
    }
}
