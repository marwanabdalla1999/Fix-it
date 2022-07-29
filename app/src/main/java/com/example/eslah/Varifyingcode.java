package com.example.eslah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Varifyingcode extends Fragment {
    PinEntryEditText txt_pin_entry;
    String phone;
    TextView phone_txt;
    apis_interface apiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_varifyingcode, container, false);
         apiService = client.start().create(apis_interface.class);
        phone_txt=view.findViewById(R.id.phone_txt);
        Bundle arguments = getArguments();
        phone = arguments.getString("phone");
        phone_txt.setText("+"+phone);
        txt_pin_entry=view.findViewById(R.id.txt_pin_entry);
        txt_pin_entry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                get_device_token(str);


            }
        });


        return view;
    }
    private void get_device_token(CharSequence str) {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        checkotp(token,str);

                    }
                });
    }

    private void checkotp(String token, CharSequence str) {

        Call<String> send_otp=apiService.checkotp(phone,str.toString(),token);
        send_otp.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject retval=new JSONObject(response.body());
                        if (retval.optString("massage").equals("Wrong otp")){
                            txt_pin_entry.setError("incorrect code, please check the code again or resend it");
                            txt_pin_entry.requestFocus();
                        }
                        else {

                            JSONObject data=retval.optJSONObject("data");
                            if (data!=null){
                                String id=Integer.toString(data.optInt("id"));
                                String name=data.optString("name");
                                String phone=data.optString("phone");
                                String token=data.optString("token");
                                shared_preference.save_user_data(getContext(),id,name,phone,token);
                                Intent i=new Intent(getContext(),MainActivity2.class);
                                i.putExtra("location" ,getArguments().getString("location"));
                                startActivity(i);
                                ((Activity) getContext()).finish();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}