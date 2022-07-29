package com.example.eslah;

public class branchmodel {
   String photo;
   String name;
   String rate;
   String time;
    int id;
    String distance;
    String amount;
    Boolean start;
    String provider_id;
    String technican_location;
    String device_token;

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getTechnican_location() {
        return technican_location;
    }

    public void setTechnican_location(String technican_location) {
        this.technican_location = technican_location;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public branchmodel(String photo, String name, String rate, String time, int id, String distance, String amount, String provider_id,String technican_location,Boolean start,String device_token) {
        this.photo = photo;
        this.name = name;
        this.rate = rate;
        this.time = time;
        this.id = id;
        this.distance = distance;
        this.amount = amount;
        this.start=start;
        this.provider_id=provider_id;
        this.technican_location=technican_location;
        this.device_token=device_token;
    }



    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String gettime() {
        return time;
    }

    public void settime(String time) {
        time = time;
    }


}
