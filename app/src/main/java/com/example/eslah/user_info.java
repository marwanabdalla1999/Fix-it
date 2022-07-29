package com.example.eslah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class user_info extends Fragment {
Button next;
EditText name;
String phone;
ProgressBar progress_name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_user_info, container, false);
        next=view.findViewById(R.id.go_to_phone_fragment);
        name=view.findViewById(R.id.name);
        progress_name=view.findViewById(R.id.progress_name);
        apis_interface apiService = client.start().create(apis_interface.class);
        Bundle arguments = getArguments();
        phone = arguments.getString("phone");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name=name.getText().toString();
                if (Name!=null && !Name.equals("")){
                    progress_name.setVisibility(View.VISIBLE);
                    next.setVisibility(View.GONE);
                        Call<String> changename=apiService.change_name(phone,Name);
                        changename.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                progress_name.setVisibility(View.GONE);
                                next.setVisibility(View.VISIBLE);
                                if (response.body().equals("name changed")){
                                Varifyingcode fragment = new Varifyingcode();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                Bundle arguments = new Bundle();
                                arguments.putString("phone" ,phone);
                                    arguments.putString("location" ,getActivity().getIntent().getStringExtra("location"));
                                    fragment.setArguments(arguments);
                                transaction.setCustomAnimations(R.anim.enter_right, R.anim.exit_left, R.anim.enter_left, R.anim.exit_right);
                                transaction.replace(R.id.fragment,fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                progress_name.setVisibility(View.GONE);
                                next.setVisibility(View.VISIBLE);
                            }
                        });



                }



            }
        });
        return view;
    }
}