package com.example.eslah;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.eslah.car_data.car_brands;
import com.example.eslah.car_data.car_type;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class new_car extends AppCompatActivity {
Spinner cartype,carbrand,carmodel,caryear,carcolor;
    ArrayAdapter Adapter_type,Adapter_brand,Adapter_model,Adapter_year,Adapter_color;
    ArrayList<car_brands> car_brands;
    ArrayList<String> type,brands,models,years,colors;
        Button add_car;
        ProgressBar add_car_progress;
    ArrayList<car_type> cars;
    apis_interface apiService;
    userdata userdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardialog);
        cartype=findViewById(R.id.cartype);
        carbrand=findViewById(R.id.carbrand);
        carmodel=findViewById(R.id.carmodel);
        caryear=findViewById(R.id.caryear);
        carcolor=findViewById(R.id.carcolor);
        add_car=findViewById(R.id.add_car);
        add_car_progress=findViewById(R.id.add_car_progress);
        apiService = client.start().create(apis_interface.class);
        userdata= getuserdata();
       // ArrayAdapter brands_adpt = new ArrayAdapter(this,android.R.layout.simple_spinner_item,brands);
      //  carbrands.setAdapter(brands_adpt);
        cars= shared_preference.get_cars(this);
       type=new ArrayList<>();
       models=new ArrayList<>();
       brands=new ArrayList<>();
       colors=new ArrayList<>();
       years=new ArrayList<>();

       for (int y=1990;y<2023;y++){

       years.add(Integer.toString(y));
       }
       addcolors();


        for (int i=0;i<cars.size();i++){

            type.add(cars.get(i).getType());

        }
        Adapter_type = new ArrayAdapter<String>(this, R.layout.spinner_row, type);
        Adapter_type.setDropDownViewResource(android.R.layout.simple_list_item_1);
        cartype.setAdapter(Adapter_type);

        cartype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                brands.clear();
                car_brands=cars.get(i).getCar_brands();
                for (int j=0;j<cars.get(i).getCar_brands().size();j++){

                    brands.add(cars.get(i).getCar_brands().get(j).getBrand());

                }



                Adapter_brand = new ArrayAdapter<String>(new_car.this, R.layout.spinner_row, brands);
                Adapter_brand.setDropDownViewResource(android.R.layout.simple_list_item_1);
                carbrand.setAdapter(Adapter_brand);
                Adapter_brand.notifyDataSetChanged();




                carbrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    models.clear();
                        for (int k=0;k<car_brands.get(i).getCar_models().size();k++){

                            models.add(car_brands.get(i).getCar_models().get(k).getModel());

                        }

                        Adapter_model = new ArrayAdapter<String>(new_car.this, R.layout.spinner_row, models);
                        Adapter_model.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        Adapter_model.notifyDataSetChanged();
                        carmodel.setAdapter(Adapter_model);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Adapter_year = new ArrayAdapter<String>(new_car.this, R.layout.spinner_row, years);
        Adapter_year.setDropDownViewResource(android.R.layout.simple_list_item_1);
        caryear.setAdapter(Adapter_year);

        Adapter_color = new ArrayAdapter<String>(new_car.this, R.layout.spinner_row, colors);
        Adapter_color.setDropDownViewResource(android.R.layout.simple_list_item_1);
        Adapter_color.notifyDataSetChanged();
        carcolor.setAdapter(Adapter_color);


        add_car.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                add_car_progress.setVisibility(View.VISIBLE);
                add_car.setVisibility(View.GONE);
                Call<String> addcar=apiService.add_user_car(userdata.getId(),userdata.getToken(),cartype.getSelectedItem().toString()
                        ,carbrand.getSelectedItem().toString()
                        ,carmodel.getSelectedItem().toString()
                        ,caryear.getSelectedItem().toString(),carcolor.getSelectedItem().toString());
            addcar.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        add_car_progress.setVisibility(View.GONE);
                        add_car.setVisibility(View.VISIBLE);
                        setResult(RESULT_OK);
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


            }
        });

    }

    private void addcolors() {
        colors.add("Red");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Black");
        colors.add("White");
        colors.add("yellow");
        colors.add("Purple");
        colors.add("Cyan");
        colors.add("Sliver");
        colors.add("Gray");
        colors.add("Orange");
        colors.add("Gold");
        colors.add("Brown");

    }
    private userdata getuserdata() {
        return shared_preference.get_user_data(this);


    }
}