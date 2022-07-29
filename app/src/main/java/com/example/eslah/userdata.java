package com.example.eslah;

public class userdata {

  private String phonenumber;
  private String token;
  private String id;
  private String name;


  public userdata(String phonenumber, String name, String id, String token) {
    this.phonenumber = phonenumber;
    this.name = name;
    this.token=token;
    this.id=id;

  }





  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }



  public String getPhonenumber() {
    return phonenumber;
  }

  public void setPhonenumber(String phonenumber) {
    this.phonenumber = phonenumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
