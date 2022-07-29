package com.example.eslah;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paymob.acceptsdk.IntentConstants;
import com.paymob.acceptsdk.PayActivity;
import com.paymob.acceptsdk.PayActivityIntentKeys;
import com.paymob.acceptsdk.PayResponseKeys;
import com.paymob.acceptsdk.SaveCardResponseKeys;
import com.paymob.acceptsdk.ThreeDSecureWebViewActivty;
import com.paymob.acceptsdk.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class summery extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap1;
    TextView name,time,distance,cost,rate;
    apis_interface apiService;
    userdata userdata;
    Marker tech_marker;
    CardView phone_layout;
    CardView getPhone_layout,my_location;
    TextView phone;
    DrawerLayout drawerLayout;
    CardView menu;
    Handler handler;
    Runnable runnable;
    LatLng tech_location;
    Dialog paymentway_dialog;
    String mask_pan="",token="";
    String amount="",wallet="",order_id="";
    LatLng mylocation;
    String total_cost="";
    ProgressBar progresspayment;
    Button pay;
    CardView chat;
    CardView notification_chat;
    FirebaseDatabase database;
    DatabaseReference myRef;
     Polyline polyline;
        TextView phone_number;
        Double max_discount=0.0,discount=0.0;
        TextView voucher;
        CardView voucher_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summery);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(summery.this);
        apiService = client.start().create(apis_interface.class);
        insit();
        clicklistner();
        displaydrawerviews();


    }

    private void clicklistner() {
        getPhone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phone_layout.getVisibility()==View.VISIBLE){

                    phone_layout.setVisibility(View.GONE);
                }
                else {

                    phone_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        phone_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent=new Intent();
                newIntent.setAction(Intent.ACTION_DIAL);
                newIntent.setData(Uri.parse("tel: "+phone_number.getText().toString()));
                startActivity(newIntent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(Gravity.RIGHT);

            }
        });

    }

    private void insit() {
        userdata= getuserdata();

        name=findViewById(R.id.name);
        time=findViewById(R.id.time);
        distance=findViewById(R.id.distance);
        cost=findViewById(R.id.price);
        rate=findViewById(R.id.rate);
        phone_layout=findViewById(R.id.phone_number_layout);
        getPhone_layout=findViewById(R.id.getphone);
        phone=findViewById(R.id.phone_number);
        my_location=findViewById(R.id.my_location);
        menu=findViewById(R.id.menu_summery);
        drawerLayout=findViewById(R.id.drawer_layout);
        chat=findViewById(R.id.chat);
        phone_number=findViewById(R.id.phone_number);
        notification_chat=findViewById(R.id.notification_chat);
        voucher=findViewById(R.id.voucher);
        voucher_layout=findViewById(R.id.voucher_layout);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chat");
        getnotifaction_chat();
        paymentway_dialog = new Dialog(summery.this);
    }


    private void getnotifaction_chat() {
        myRef.child("seen").child(order_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot snap:dataSnapshot.getChildren()){
                        if (snap.child(userdata.getName()).exists()){

                    if (snap.child(userdata.getName()).getValue().equals("seen")) {
                        notification_chat.setVisibility(View.INVISIBLE);

                    } else {
                        notification_chat.setVisibility(View.VISIBLE);

                    }}
                }}

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


    void update_order_data(){
    Call<String> update_Order_data=apiService.update_Order_data(order_id);
    update_Order_data.enqueue(new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            handler.postDelayed(runnable,1000);
            if (response.isSuccessful()){
                try {
                    JSONObject data=new JSONObject(response.body());
                    if (data!=null) {
                      amount= data.optString("amount");
                        name.setText(data.optString("name"));
                        time.setText(data.optString("time"));
                        distance.setText(Double.toString(Double.parseDouble(data.optString("distance"))/1000)+" km");
                        Double Amount=Double.parseDouble(amount);
                        Double discount_amount=(Amount*discount)/100;
                        if(discount_amount<=max_discount){

                            Amount-=discount_amount;

                        }
                        else{
                            Amount-=max_discount;
                        }
                        amount=Double.toString(Amount);
                        cost.setText(amount+" EGP");
                        rate.setText(data.optString("rate"));
                        phone.setText("+2"+data.optString("phone"));
                         tech_location=getlatlng(data.optString("technican_location"));
                        tech_marker.setPosition(tech_location);

                polyline.remove();
                        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(10), new Gap(20));

                        polyline= mMap1.addPolyline(new PolylineOptions().add(mylocation, tech_location).
                                // below line is use to specify the width of poly line.
                                        width(5)
                                // below line is use to add color to our poly line.
                                .color(Color.BLACK)
                                .pattern(pattern)
                                // below line is to make our poly line geodesic.
                                .geodesic(false));

                        if (data.optString("state").equals("wait for paying")){


                if (!data.optString("card_used").equals("null")&&!paymentway_dialog.isShowing()){

                            get_paymnet_way(data.optString("card_used"));
                }
                        }

                        else if (data.optString("state").equals("finished")){
                            if (handler!=null){
                                handler.removeCallbacks(runnable);
                            }
                            Intent i=new Intent(summery.this,MainActivity2.class);
                            i.putExtra("location",mylocation.toString());
                            shared_preference.setstate(summery.this,"finished");
                            startActivity(i);
                            finish();
                        }
                        else if (data.optString("state").equals("cancelled")){
                            if (handler!=null){
                                handler.removeCallbacks(runnable);
                            }
                            Intent i=new Intent(summery.this,MainActivity2.class);
                            i.putExtra("location",mylocation.toString());
                            shared_preference.setstate(summery.this,"cancelled");
                            startActivity(i);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            handler.postDelayed(runnable,1000);
        }
    });
}
    private void getprovider_data() {
        ProgressBar tech_data_progress=findViewById(R.id.tech_data_progress);
        LinearLayout tech_data=findViewById(R.id.tech_data);
        tech_data_progress.setVisibility(View.VISIBLE);
        tech_data.setVisibility(View.INVISIBLE);
        Call<String> getOrder_data=apiService.getOrder_data(getuserdata().getId(),getuserdata().getToken());
        getOrder_data.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject data=new JSONObject(response.body());
                        if (data!=null) {
                            tech_data_progress.setVisibility(View.INVISIBLE);
                            tech_data.setVisibility(View.VISIBLE);
                            order_id=data.optString("order_id");
                             amount= data.optString("amount");
                            if (!data.optString("voucher").equals("0")){
                                voucher_layout.setVisibility(View.VISIBLE);
                                max_discount=Double.parseDouble(data.optString("voucher").split("/")[1]);
                            discount=Double.parseDouble(data.optString("voucher").split("/")[0]);
                                voucher.setText(discount+"% up to "+max_discount+" L.E");

                            }else{
                                voucher_layout.setVisibility(View.GONE);

                            }

                            wallet=data.optString("wallet");
                            name.setText(data.optString("name"));
                            time.setText(data.optString("time"));
                            distance.setText(Double.toString(Integer.parseInt(data.optString("distance"))/1000)+" km");
                            Double Amount=Double.parseDouble(amount);
                            Double discount_amount=(Amount*discount)/100.0;
                            if(discount_amount<=max_discount){

                                Amount-=discount_amount;

                            }
                            else{
                                Amount-=max_discount;
                            }
                            amount=Double.toString(Amount);
                            cost.setText(amount+" EGP");
                            rate.setText(data.optString("rate"));
                            phone.setText("+2"+data.optString("phone"));
                             mylocation=getlatlng(data.optString("location_lat_lng"));
                             tech_location=getlatlng(data.optString("technican_location"));
                              mMap1.addMarker(new MarkerOptions().position(mylocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder1)));
                            tech_marker=  mMap1.addMarker(new MarkerOptions().position(tech_location).icon(BitmapDescriptorFactory.fromResource(R.drawable.tech_location)));
                            mMap1.animateCamera(CameraUpdateFactory.newLatLngZoom(midPoint(mylocation.latitude,mylocation.longitude,tech_location.latitude,tech_location.longitude), 12));
                            List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(10), new Gap(20));
                         polyline= mMap1.addPolyline(new PolylineOptions().add(mylocation, tech_location).
                                    // below line is use to specify the width of poly line.
                                            width(5)
                                    // below line is use to add color to our poly line.
                                    .color(Color.BLACK)
                                    .pattern(pattern)
                                    // below line is to make our poly line geodesic.
                                    .geodesic(false));

                            my_location.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mMap1.animateCamera(CameraUpdateFactory.newLatLngZoom(midPoint(mylocation.latitude,mylocation.longitude,tech_location.latitude,tech_location.longitude), 12));
                                }
                            });
                            chat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i=new Intent(summery.this,Chat_Activity.class);
                                    i.putExtra("order_id",order_id);
                                    i.putExtra("tech_name",data.optString("name"));
                                    startActivity(i);
                                }
                            });


                            if (data.optString("state").equals("wait for paying")){

                                if (!data.optString("card_used").equals("null")&&!paymentway_dialog.isShowing()){

                                    get_paymnet_way(data.optString("card_used"));
                                }


                            }
                            else if (data.optString("state").equals("finished")){
                                if (handler!=null){
                                    handler.removeCallbacks(runnable);
                                }
                                Intent i=new Intent(summery.this,MainActivity2.class);
                                i.putExtra("location",mylocation.toString());
                                shared_preference.setstate(summery.this,"finished");
                                startActivity(i);
                                finish();
                            }
                            else if (data.optString("state").equals("cancelled")){
                                if (handler!=null){
                                    handler.removeCallbacks(runnable);
                                }
                                Intent i=new Intent(summery.this,MainActivity2.class);
                                i.putExtra("location",mylocation.toString());
                                shared_preference.setstate(summery.this,"cancelled");

                                startActivity(i);
                                finish();
                            }

                            handler=new Handler();
                            runnable=new Runnable() {
                                @Override
                                public void run() {
                                    update_order_data();

                                }
                            };
                            handler.postDelayed(runnable,1000);
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

    private void get_paymnet_way(String card_id) {
        paymentway_dialog.setContentView(R.layout.payment_way);
        paymentway_dialog.setCancelable(false);
        paymentway_dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        paymentway_dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_White);
        TextView cardnumber=paymentway_dialog.findViewById(R.id.cardnumber);
        LinearLayout card_data=paymentway_dialog.findViewById(R.id.order_payment);
        TextView user_wallet=paymentway_dialog.findViewById(R.id.user_wallet);
        TextView order_cost=paymentway_dialog.findViewById(R.id.order_cost);
        TextView total=paymentway_dialog.findViewById(R.id.total_cost);
        TextView voucher_text=paymentway_dialog.findViewById(R.id.voucher_text);
        LinearLayout voucher_layout=paymentway_dialog.findViewById(R.id.voucher_linear_layout);
       progresspayment=paymentway_dialog.findViewById(R.id.progress_payment);
        voucher_text.setText(discount+"% up to "+max_discount+" L.E");
        order_cost.setText(amount+" EGP");
        user_wallet.setText(wallet+" EGP");
         total_cost=Double.toString(Double.parseDouble(wallet)-Double.parseDouble(amount));
        total.setText(total_cost+" EGP");
        if(discount<=0.0){
            voucher_layout.setVisibility(View.GONE);
        }
        else{
            voucher_layout.setVisibility(View.VISIBLE);
        }
       pay =paymentway_dialog.findViewById(R.id.pay);
        LottieAnimationView card_loading=paymentway_dialog.findViewById(R.id.card_loading);
        card_loading.setVisibility(View.VISIBLE);
        card_data.setVisibility(View.INVISIBLE);
Call<String> get_card_data=apiService.get_card_data(card_id);
get_card_data.enqueue(new Callback<String>() {
    @Override
    public void onResponse(Call<String> call, Response<String> response) {

        if (response.isSuccessful()) {
            try {
                JSONObject data = new JSONObject(response.body());
                if (data != null) {
                                card_loading.setVisibility(View.INVISIBLE);
                                card_data.setVisibility(View.VISIBLE);
                                cardnumber.setText(data.optString("mask_pan"));
                                mask_pan=data.optString("mask_pan");
                                token=data.optString("token");

                                pay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        progresspayment.setVisibility(View.VISIBLE);
                                        pay.setVisibility(View.GONE);
                                        getpayment_key(Double.toString(Math.abs(Double.parseDouble(total_cost))),token,mask_pan);


                                    }
                                });
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
if (Double.parseDouble(total_cost)<0 && !paymentway_dialog.isShowing()) {
    paymentway_dialog.show();
}

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap1 = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map));
        getprovider_data();





    }



    private userdata getuserdata() {
        return shared_preference.get_user_data(this);


    }


    LatLng getlatlng(String location){
        String latlangstr=  location.substring(location.indexOf('(')+1,location.indexOf(')'));
        String[] latlang=latlangstr.split(",");
        LatLng latLng=new LatLng(Double.parseDouble(latlang[0]),Double.parseDouble(latlang[1]));
        return  latLng;
    }

    public static LatLng midPoint(double lat1,double lon1,double lat2,double lon2){

        double midlat = (lat1 + lat2)/2;
        double midlng = (lon1 + lon2)/2;


        return new LatLng(midlat,midlng);
    }

    private void displaydrawerviews() {
        NavigationView navigationView = findViewById(R.id.summery_nav);
        View headerView = navigationView.getHeaderView(0);
        TextView name =headerView.findViewById(R.id.name);
        TextView phone =headerView.findViewById(R.id.phone);
        name.setText(userdata.getName());
        phone.setText(userdata.getPhonenumber());


        Menu menu=navigationView.getMenu();
        MenuItem wallet=menu.findItem(R.id.wallet);
        wallet.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Intent intent=new Intent(summery.this,wallet.class);
                startActivity(intent);
                return true;
            }
        });
        menu.findItem(R.id.cars).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent=new Intent(summery.this,my_cars.class);
                startActivity(intent);
                return true;
            }
        });
        menu.findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                shared_preference.remove_user_data(summery.this);
                Intent intent=new Intent(summery.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });

    }
    private void putNormalExtras(Intent intent,String payment_token) {
        intent.putExtra(PayActivityIntentKeys.PAYMENT_KEY, payment_token);
        intent.putExtra(PayActivityIntentKeys.THREE_D_SECURE_ACTIVITY_TITLE, "Verification");
    }

    private void startPayActivityToken(String token,String mask_pan,String paymentkey) {
        Intent pay_intent = new Intent(this, PayActivity.class);

        putNormalExtras(pay_intent,paymentkey);
        // replace this with your actual card token
        pay_intent.putExtra("language","en");
        pay_intent.putExtra(PayActivityIntentKeys.TOKEN, token);
        pay_intent.putExtra(PayActivityIntentKeys.MASKED_PAN_NUMBER, mask_pan);
        pay_intent.putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, true);
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_SAVE_CARD, false);
        pay_intent.putExtra("ActionBar",false);
        pay_intent.putExtra(PayActivityIntentKeys.THEME_COLOR,getResources().getColor(R.color.black));

        pay_intent.putExtra( PayActivityIntentKeys.FIRST_NAME, "marwan");
        pay_intent.putExtra(PayActivityIntentKeys.LAST_NAME, "Abdalla");
        pay_intent.putExtra(PayActivityIntentKeys.BUILDING,"24");
        pay_intent.putExtra(PayActivityIntentKeys.FLOOR, "7");
        pay_intent.putExtra(PayActivityIntentKeys.APARTMENT, "705");
        pay_intent.putExtra(PayActivityIntentKeys.CITY, "Alexandria");
        pay_intent.putExtra(PayActivityIntentKeys.STATE, "Utah");
        pay_intent.putExtra(PayActivityIntentKeys.COUNTRY,"EG" );
        pay_intent.putExtra(PayActivityIntentKeys.EMAIL, "marwanabdalla1999@gmail.com");
        pay_intent.putExtra(PayActivityIntentKeys.PHONE_NUMBER, "+201011218307");
        pay_intent.putExtra(PayActivityIntentKeys.POSTAL_CODE,  "21532");
        startActivityForResult(pay_intent, 101);
        Intent secure_intent = new Intent(summery.this, ThreeDSecureWebViewActivty.class);
        secure_intent.putExtra("ActionBar",false);
    }

    private void getpayment_key(String cost,String token, String mask_pan) {

        Call<String> generate_payment_key = apiService.generate_payment_key(Double.toString(Double.parseDouble(cost)*100));

        generate_payment_key.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progresspayment.setVisibility(View.GONE);
                pay.setVisibility(View.VISIBLE);
                if (response.isSuccessful()){
                    startPayActivityToken(token,mask_pan,response.body());

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progresspayment.setVisibility(View.GONE);
                pay.setVisibility(View.VISIBLE);
                Log.d(L.TAG, "onResponse:"+t.getMessage());

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 101) {
            Bundle extras = data.getExtras();
            Log.d(L.TAG, "onActivityResult: successfully");
            if (resultCode == IntentConstants.USER_CANCELED) {
                // User canceled and did no payment request was fired
                ToastMaker.displayShortToast(this, "User canceled!!");
            } else if (resultCode == IntentConstants.MISSING_ARGUMENT) {
                // You forgot to pass an important key-value pair in the intent's extras
                ToastMaker.displayShortToast(this, "Missing Argument == " + extras.getString(IntentConstants.MISSING_ARGUMENT_VALUE));
            } else if (resultCode == IntentConstants.TRANSACTION_ERROR) {
                // An error occurred while handling an API's response
                ToastMaker.displayShortToast(this, "Reason == " + extras.getString(IntentConstants.TRANSACTION_ERROR_REASON));
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED) {
                // User attempted to pay but their transaction was rejected

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras.getString(PayResponseKeys.DATA_MESSAGE));
            } else if (resultCode == IntentConstants.TRANSACTION_REJECTED_PARSING_ISSUE) {
                // User attempted to pay but their transaction was rejected. An error occured while reading the returned JSON
                ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL) {
                // User finished their payment successfully
                payed_amount();
                // Use the static keys declared in PayResponseKeys to extract the fields you want

            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_PARSING_ISSUE) {
                // User finished their payment successfully. An error occured while reading the returned JSON.
                payed_amount();
                // ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_CARD_SAVED) {
                // User finished their payment successfully and card was saved.

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                // Use the static keys declared in SaveCardResponseKeys to extract the fields you want

                //   ToastMaker.displayLongToast(this, "Token == " + extras.getString(SaveCardResponseKeys.TOKEN));
             //   savecard(extras.getString(SaveCardResponseKeys.TOKEN),extras.getString(SaveCardResponseKeys.MASKED_PAN));

            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION) {
                ToastMaker.displayShortToast(this, "User canceled 3-d scure verification!!");

                // Note that a payment process was attempted. You can extract the original returned values
                // Use the static keys declared in PayResponseKeys to extract the fields you want
                ToastMaker.displayShortToast(this, extras.getString(PayResponseKeys.PENDING));
            } else if (resultCode == IntentConstants.USER_CANCELED_3D_SECURE_VERIFICATION_PARSING_ISSUE) {
                ToastMaker.displayShortToast(this, "User canceled 3-d scure verification - Parsing Issue!!");

                // Note that a payment process was attempted.
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            }
        }}


        void payed_amount(){
            String payed,payed_wallet,remain_in_wallet;
            if(Double.parseDouble(total_cost)>0){
                payed="0";
                payed_wallet="-"+amount;
                remain_in_wallet=total_cost;
            }
            else {
                payed_wallet="-"+wallet;
                payed=total_cost;
                remain_in_wallet="0";
            }
            Call<String> payed_amount=apiService.payed_amount(order_id,payed,payed_wallet,remain_in_wallet,voucher.getText().toString(),"Credit/Debit Card");
            payed_amount.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        if (response.body().equals("payment successfully")){

                                Intent i=new Intent(summery.this,MainActivity2.class);
                                i.putExtra("location",mylocation.toString());
                            i.putExtra("state","finish");
                            startActivity(i);

                        }

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });

        }
}