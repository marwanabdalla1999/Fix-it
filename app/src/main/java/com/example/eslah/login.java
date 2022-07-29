package com.example.eslah;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class login extends Fragment {
    Button send_code;
    EditText phone;
    String name;
    String number="";
    apis_interface apiService;
    ProgressBar progress;
    String location;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        apiService = client.start().create(apis_interface.class);
        send_code=view.findViewById(R.id.send_code);
        phone=view.findViewById(R.id.phone);
        progress=view.findViewById(R.id.progress);
        location=getArguments().getString("location");
        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                send_code.setVisibility(View.GONE);
        send_code();
            }
        });

        return view;
    }

    private void send_code() {

        if (!phone.getText().toString().equals("")&& phone.getText()!=null){

            if (phone.getText().toString().length()<10){
                progress.setVisibility(View.GONE);
                send_code.setVisibility(View.VISIBLE);
                phone.setError("please enter a valid number");
                phone.requestFocus();

            }
            else {
                phoneformat();
                Call<String> send_otp = apiService.check_phone(number);
                send_otp.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        progress.setVisibility(View.GONE);
                        send_code.setVisibility(View.VISIBLE);
                        if (response.isSuccessful()) {
                            if (response.body().equals("otp has been sent") || response.body().equals("registertion not completed")) {
                                user_info fragment = new user_info();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                Bundle arguments = new Bundle();
                                arguments.putString("phone", number);
                                arguments.putString("location" ,location);

                                fragment.setArguments(arguments);
                                transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right);
                                transaction.replace(R.id.fragment, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();


                            } else if (response.body().equals("registered before")) {
                                Varifyingcode fragment = new Varifyingcode();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                Bundle arguments = new Bundle();
                                arguments.putString("phone", number);
                                arguments.putString("location" ,location);
                                fragment.setArguments(arguments);
                                transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right);
                                transaction.replace(R.id.fragment, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            }
                            else {
                                phone.setError(response.body());
                                phone.requestFocus();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                        send_code.setVisibility(View.VISIBLE);
                        phone.setError("please enter a valid number");
                        phone.requestFocus();
                    }
                });

            }
        }
        else {
            progress.setVisibility(View.GONE);
            send_code.setVisibility(View.VISIBLE);
            phone.setError("please enter your number");
            phone.requestFocus();
        }
    }

    private void phoneformat() {
        number=phone.getText().toString();
        if (number.startsWith("0")){

            number="2"+number;
        }
        else {

            number="20"+number;
        }
    }
}