package com.example.eslah;

public class cards_model {

   private String Token;
   private String masked_pan;
   private String id;
    private String type;
    public cards_model(String id,String token, String masked_pan,String type) {
        this.id=id;
        this.Token = token;
        this.masked_pan = masked_pan;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getMasked_pan() {
        return masked_pan;
    }

    public void setMasked_pan(String masked_pan) {
        this.masked_pan = masked_pan;
    }
}
