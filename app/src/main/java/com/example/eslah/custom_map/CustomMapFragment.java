package com.example.eslah.custom_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.eslah.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class CustomMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    public View mapView = null;

    public WrapperView wrapperView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mapView = super.onCreateView(inflater, parent, savedInstanceState);
        wrapperView = new WrapperView(getActivity());
        wrapperView.addView(mapView);
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        return wrapperView;
    }

    @Override
    public View getView() {
        return mapView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        wrapperView.setGoogleMap(googleMap);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);    }

}