package com.example.eslah;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eslah.car_data.car_data;
import com.example.eslah.car_data.car_type;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static int SPLASH_TIME_OUT = 2000;
   LinearLayout img;
    LinearLayout logo;
    userdata userdata;
    apis_interface apiService;
    Boolean getted=false;
    Boolean ready=false;
    String returned_response="";
    LocationManager locationManager;
    CardView reload;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         apiService = client.start().create(apis_interface.class);
        userdata= getuserdata();
        statusCheck();
        img=findViewById(R.id.logoimg);
        reload=findViewById(R.id.reload);
        reload.setVisibility(View.GONE);
        reload.setOnClickListener(i->{
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
                finish();
        });
        logo=findViewById(R.id.logoimg);
        locationManager  = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        get_car_data();




        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }

        else {
            Location location=locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER);
            if (location!=null){
                startapp(location);

            }
            else {
                locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 0, 0, MainActivity.this);
            }

        }
      //  Intent i = new Intent(MainActivity.this, MainActivity2.class);
        //    ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,img, ViewCompat.getTransitionName(img));
       // startActivity(i);

        logo.setTranslationX(-950);
            logo.animate().translationXBy(1050).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    logo.animate().translationXBy(-100).setDuration(500).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {


                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();

    }

    private void get_car_data() {

            Call<car_data> car_data = apiService.get_cars_data(shared_preference.get_car_data_version(this));
            car_data.enqueue(new Callback<car_data>() {
                @Override
                public void onResponse(Call<car_data> call, Response<car_data> response) {
                    if (response.isSuccessful()){

                        if (response.body().getVersion().equals("already_downloaded")){
                            get_device_token();

                        }
                        else {
                            shared_preference.set_car_data_version(MainActivity.this,response.body().getVersion());
                            shared_preference.set_cars(MainActivity.this, response.body().getData());
                            get_device_token();                        }
                    }
                    else {
                        reload.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "error in connection", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<car_data> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "error in connection", Toast.LENGTH_SHORT).show();
                    reload.setVisibility(View.VISIBLE);
                }
            });



    }

    private void get_device_token() {

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
                           getsession(token);

                    }
                });
    }


    private void getsession(String token) {
        if (userdata==null || userdata.getToken().isEmpty()){
            ready=true;
            returned_response="logout";
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {


                Location location=locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER);
                if (location!=null){
                    startapp(location);

                }


                else {
                    locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 0, 0, MainActivity.this);
                }

            }
        }
        else {
            Call<String> session = apiService.session(userdata.getId(), userdata.getToken(),token);
            session.enqueue(new Callback<String>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        ready=true;
                        returned_response=response.body();
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {


                                Location location=locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER);
                            if (location!=null){
                                startapp(location);

                            }
                            else {
                                locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 0, 0, MainActivity.this);
                            }

                        }
               }
                    else {
                        reload.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "error in connection please check your network", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "error in connection please check your network", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void startapp(Location location) {
        if (ready) {
            ready=false;
            getted = true;
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        if (returned_response.equals("login/request_order")) {
            Intent i = new Intent(MainActivity.this, summery.class);
            i.putExtra("location", latLng.toString());
            startActivity(i);
            finish();
        }
        else if (returned_response.contains("login/searching for technician")) {
            Intent i = new Intent(MainActivity.this, MainActivity2.class);
            i.putExtra("location", latLng.toString());
            shared_preference.setstate(MainActivity.this,returned_response);
            startActivity(i);
            finish();
        }
        else if (returned_response.equals("login")) {
            Intent i = new Intent(MainActivity.this, MainActivity2.class);
            i.putExtra("location", latLng.toString());

            startActivity(i);
            finish();
        }
        else if (returned_response.equals("logout")) {
            Intent i = new Intent(MainActivity.this, sign_up.class);
            i.putExtra("location", latLng.toString());
            startActivity(i);
            finish();
        }}
    }

    private userdata getuserdata() {
        return shared_preference.get_user_data(this);


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (!getted) {
                    startapp(location);

        }
        }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void    buildAlertMessageNoGps() {
        Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.locationenable);
        builder.setCancelable(false);
        builder.getWindow().setBackgroundDrawableResource(R.color.translucent);
        builder.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        Button openGPS=builder.findViewById(R.id.enable_gps);
        openGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        });

        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults!=null && grantResults.length>0){
                if (grantResults[0]==PackageManager.PERMISSION_DENIED){
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {

                   dialog.setContentView(R.layout.location_denied);
                    dialog.setCancelable(false);
                    dialog.getWindow().setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawableResource(R.color.translucent);
                    if (!dialog.isShowing()){
                    dialog.show();

            }}}
                else {
                dialog.cancel();
                Location location = locationManager.getLastKnownLocation(LocationManager.FUSED_PROVIDER);
                if (location != null) {
                    startapp(location);
                } else {
                    locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 0, 0, MainActivity.this);
                }


            }
        }}
    }


}
