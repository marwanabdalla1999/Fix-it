package com.example.eslah;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class order_model {

    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("issue")
    @Expose
    String issue;

    @SerializedName("amount")
    @Expose
    String amount;

    @SerializedName("created_at")
    @Expose
    String created_at;

    @SerializedName("car_id")
    @Expose
    String car_id;

    @SerializedName("address")
    @Expose
    String address;
    @SerializedName("state")
    @Expose
    String state;
    @SerializedName("payment_way")
    @Expose
    String payment_way;
    @SerializedName("location_lat_lng")
    @Expose
    String location_lat_lng;
    public order_model(int id, String issue, String amount, String created_at, String car_id, String address, String state,String location_lat_lng,String payment_way) {
        this.id = id;
        this.issue = issue;
        this.amount = amount;
        this.created_at = created_at;
        this.car_id = car_id;
        this.address = address;
        this.state = state;
        this.location_lat_lng=location_lat_lng;
        this.payment_way=payment_way;
    }

    public String getPayment_way() {
        return payment_way;
    }

    public void setPayment_way(String payment_way) {
        this.payment_way = payment_way;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocation_lat_lng() {
        return location_lat_lng;
    }

    public void setLocation_lat_lng(String location_lat_lng) {
        this.location_lat_lng = location_lat_lng;
    }
}
