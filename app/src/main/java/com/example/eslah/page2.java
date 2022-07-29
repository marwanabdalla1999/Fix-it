package com.example.eslah;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class page2 extends Fragment implements OnMapReadyCallback, LocationListener {
ViewPager2 slider;
Handler sliderhandler;
TabLayout sildertabs;
//CardView findprovider,findprovider1;
ImageView close;
RelativeLayout accedientassist;
TextView textView;
    GoogleMap  mMap;
    LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_page2, container, false);
        SupportMapFragment mapFragment1 = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment1.getMapAsync(page2.this);
     //   findprovider=view.findViewById(R.id.findprovider);
      //  findprovider1=view.findViewById(R.id.findprovider1);
        slider=view.findViewById(R.id.slider);
        sildertabs=view.findViewById(R.id.sildertabs);
      //  findprovider=view.findViewById(R.id.findprovider);
       // findprovider1=view.findViewById(R.id.findprovider1);
        close=view.findViewById(R.id.close);
        textView=view.findViewById(R.id.location);
     /*   textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),change_location.class);
                startActivity(i);
            }
        });*/
        accedientassist=view.findViewById(R.id.accedientassist);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accedientassist.setVisibility(View.GONE);
            }
        });
        sliderhandler=new Handler();
       /* findprovider1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(),search_for_provider.class);
                getContext().startActivity(intent);

            }
        });
        findprovider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        List<slideritem> slideritems=new ArrayList<>();

        sliderAdapter sliderAdapter=new sliderAdapter(slideritems,slider);
        slider.setAdapter(sliderAdapter);
        sliderAdapter.notifyDataSetChanged();
        slider.setClipToPadding(false);
        slider.setClipChildren(false);
        slider.setOffscreenPageLimit(3);
       // slider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
      //  slider.setCurrentItem(1);
        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+ r * 0.15f);
            }
        });
        sildertabs.addTab(sildertabs.newTab());
        sildertabs.addTab(sildertabs.newTab());
        sildertabs.addTab(sildertabs.newTab());
        slider.setPageTransformer(compositePageTransformer);
        slider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderhandler.removeCallbacks(sliderRunnable);
                sliderhandler.postDelayed(sliderRunnable,7000);
            }
        });
        sildertabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                slider.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        slider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            public void onPageSelected(int position) {

                        sildertabs.selectTab(sildertabs.getTabAt(position));

            }
        });


        return view;
    }

   private Runnable sliderRunnable=new Runnable() {
        @Override
        public void run() {
            if (slider.getCurrentItem()==2){
                slider.setCurrentItem(0);
                return;
            }
            slider.setCurrentItem(slider.getCurrentItem()+1);
        }
    };
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                getContext(), R.raw.map));

        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);


        }








    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
       /* current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                change_location.this.location.setText(getaddress(latLng));
            }
        });*/


      /*  if (!onrecieve){
            mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation)).title(getaddress(latLng)));
            this.location.setText(getaddress(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            onrecieve=true;
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {

                    LatLng latlng=mMap.getCameraPosition().target;
                    change_location.this.location.setText(getaddress(latlng));
                    if (!change_location.this.location.getText().equals("لم نتمكن من تحديد الموقع")){
                        location_card.setVisibility(View.VISIBLE);
                        line.setVisibility(View.VISIBLE);
                        choose_location.setVisibility(View.VISIBLE);
                    }
                }
            });
            mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onCameraMove() {
                    location_card.setVisibility(View.INVISIBLE);
                    line.setVisibility(View.INVISIBLE);
                    choose_location.setVisibility(View.GONE);
                    autoCompleteTextView.setText("", false);
                }
            });
        }*/
    }
}