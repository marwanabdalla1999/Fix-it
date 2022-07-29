package com.example.eslah;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class car_model {

    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("type")
            @Expose
    String type;

    @SerializedName("brand")
    @Expose
   String brand;

    @SerializedName("model")
    @Expose
   String model;

    @SerializedName("year")
    @Expose
   String year;

    @SerializedName("color")
    @Expose
   String color;

    public car_model(int id,String type, String brand, String model, String year, String color) {
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
