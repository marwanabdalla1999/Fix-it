package com.example.eslah;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.eslah.car_data.car_type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class shared_preference {

     Context context;
    static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    public static void getinstance(Context context) {



    }
static void save_user_data(Context context,String id, String name, String phone, String token){
    if (sharedPreferences==null){
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }
    editor.putString("id",id);
    editor.putString("name",name);
    editor.putString("phone",phone);
    editor.putString("token",token);
    editor.apply();

}
static userdata get_user_data(Context context){
    if (sharedPreferences==null){
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }
   String id= sharedPreferences.getString("id","");
    String name= sharedPreferences.getString("name","");
    String phone= sharedPreferences.getString("phone","");
    String token= sharedPreferences.getString("token","");
    userdata userdata=new userdata(phone,name,id,token);

    return userdata;

}
static void set_cars(Context context,ArrayList<car_type> car_types){
    if (sharedPreferences==null){
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }
    Gson gson=new Gson();
    String cars=gson.toJson(car_types);
    editor.putString("cars",cars);
    editor.apply();
}

static  ArrayList<car_type> get_cars(Context context){
    if (sharedPreferences==null){
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }
    Type type = new TypeToken<ArrayList<car_type>>() {}.getType();
    Gson gson=new Gson();
    String cars= sharedPreferences.getString("cars","");
ArrayList<car_type> car_types=gson.fromJson(cars,type);

return car_types;

}
    static void remove_user_data(Context context){
        if (sharedPreferences==null){
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
        }
        editor.putString("id","");
        editor.putString("name","");
        editor.putString("phone","");
        editor.putString("token","");
        editor.apply();
    }
    static void set_car_data_version(Context context,String Version){
        if (sharedPreferences==null){
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
        }

        editor.putString("Version",Version);
        editor.apply();
    }

    static String get_car_data_version(Context context){
        if (sharedPreferences==null){
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
        }


        return  sharedPreferences.getString("Version","");
    }

    static void setstate(Context context,String state){
        if (sharedPreferences==null){
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
        }

        editor.putString("state",state);
        editor.apply();
    }
    static String getstate(Context context){
        if (sharedPreferences==null){
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();
        }


        return  sharedPreferences.getString("state","");
    }
}
