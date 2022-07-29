package com.example.eslah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.eslah.custom_map.WrapperView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class
change_location extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    boolean onrecieve;
    TextView location;
    CardView current_location, location_card;
    Button choose_location;
    //RelativeLayout line;
    ImageView back;
    AutoCompleteTextView autoCompleteTextView;
    PlacesClient placesClient;
    ArrayList<String> resultList;
    ArrayList<String> place_id;
    ArrayAdapter<String> arrayAdapter;
    String location_lat_lng;
    Marker marker;
    Boolean getted = false;
    LottieAnimationView lottieAnimationView;
    Handler handler;
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(change_location.this);
        mapFragment.onLowMemory();
        mapFragment.onStop();
        mapFragment.onResume();
        handler=new Handler();
        Places.initialize(getApplicationContext(), "AIzaSyDdoZKqqKdQXhmepwtlyYfvqMN2lRO-nBU");
        placesClient = Places.createClient(this);
        onrecieve = false;
        location = findViewById(R.id.location);
        current_location = findViewById(R.id.current_location);
        choose_location = findViewById(R.id.choose_location);
       // line = findViewById(R.id.line);
        location_card = findViewById(R.id.location_card);
        back = findViewById(R.id.back);
        back.setOnClickListener(i -> onBackPressed());
        resultList = new ArrayList<>();
        place_id = new ArrayList<>();
        autoCompleteTextView = findViewById(R.id.location2);
        lottieAnimationView=findViewById(R.id.loading);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getlocation(place_id.get(position));

                resultList.clear();
                place_id.clear();
                hidesoftkeyboard();
                autoCompleteTextView.clearFocus();
                autoCompleteTextView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autoCompleteTextView.dismissDropDown();
                    }
                }, 1000);
            }
        });

        choose_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location.getVisibility()!=View.INVISIBLE){
                Intent data = new Intent();
                data.putExtra("location", mMap.getCameraPosition().target.toString());
                data.putExtra("address", location.getText());
                setResult(RESULT_OK, data);
                finish();
            }}
        });
    }

    private void hidesoftkeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getlocation(String placeId) {

        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);


        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));
            change_location.this.location.setText(getaddress(place.getLatLng()));
            location.setVisibility(View.VISIBLE);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;

                final int statusCode = apiException.getStatusCode();
                // TODO: Handle error with given status code.
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map));
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1)));

      //  mMap.setLatLngBoundsForCameraTarget(adelaideBounds);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setTrafficEnabled(false);
        mMap.setBuildingsEnabled(false);
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        else {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }
        String latlng=getIntent().getStringExtra("location");
        String latlangstr=  latlng.substring(latlng.indexOf('(')+1,latlng.indexOf(')'));
        String[] latlang=latlangstr.split(",");
        LatLng latLng=new LatLng(Double.parseDouble(latlang[0]),Double.parseDouble(latlang[1]));
        marker=  mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder1)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getPredictions(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(s, 16));
        onrecieve=true;
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                lottieAnimationView.setVisibility(View.VISIBLE);
                location.setVisibility(View.INVISIBLE);
            if (handler!=null){
                handler.removeCallbacks(runnable);

            }
         handler.postDelayed(runnable,2000);



            }
        });


    }

    Runnable runnable= new Runnable() {
        @Override
        public void run() {
            LatLng latlng=mMap.getCameraPosition().target;
            new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {


                    return getaddress(latlng);

                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    if (!o.toString().equals("مصر") && !o.toString().equals("لم نتمكن من تحديد الموقع")){
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        location.setVisibility(View.VISIBLE);
                        location.setText(o.toString());
                        //line.setVisibility(View.VISIBLE);
                        //choose_location.setVisibility(View.VISIBLE);

                    }

                }
            }.execute();

        }
    };

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


         //marker.setPosition(latLng);
   current_location.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
              mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
              change_location.this.location.setText(getaddress(latLng));
          change_location.this.location.setVisibility(View.VISIBLE);
     }
});


        if (!onrecieve){
          //  mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation)).title(getaddress(latLng)));

        }

    }


    private String getaddress(LatLng location) {
        Geocoder geocoder;
        List<Address> addresses;
        Locale loc = new Locale("ar");
        geocoder = new Geocoder(this, loc);
        String[] address=null;

        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses!=null && addresses.size()>0){
             address = addresses.get(0).getAddressLine(0).split("،"); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();}
        } catch (IOException e) {
            e.printStackTrace();
        }
if (address!=null ){
    if (address.length>1){
 return address[0];

    }
    else if(address.length>0){
        return address[0];
    }
else return "لم نتمكن من تحديد الموقع";
}
else return "لم نتمكن من تحديد الموقع";



    }
    public void getPredictions(CharSequence constraint) {

        RectangularBounds bounds=new RectangularBounds() {
            @NonNull
            @Override
            public LatLng getSouthwest() {
                return new LatLng(22.000174727939385, 25.000625731336637);
            }

            @NonNull
            @Override
            public LatLng getNortheast() {
                return new LatLng(31.31505229938542, 34.22372181580895);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();


        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)

                .setCountry("EGY")
                //   .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build();

        Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onSuccess(FindAutocompletePredictionsResponse findAutocompletePredictionsResponse) {
                if (findAutocompletePredictionsResponse != null)
                    for (AutocompletePrediction prediction : findAutocompletePredictionsResponse.getAutocompletePredictions()) {


                        resultList.add(prediction.getPrimaryText(null).toString());
                        place_id.add(prediction.getPlaceId());

                        Collections.reverse(resultList);
                        Collections.reverse(place_id);

                         arrayAdapter=new ArrayAdapter<>(change_location.this, android.R.layout.simple_dropdown_item_1line,resultList);
                        autoCompleteTextView.setAdapter(arrayAdapter);
                        autoCompleteTextView.showDropDown();

                    }


            }
        });

    }


}