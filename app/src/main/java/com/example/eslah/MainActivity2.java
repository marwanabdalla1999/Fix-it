package com.example.eslah;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonArray;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.airbnb.lottie.L.TAG;

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback ,LocationListener {
    ViewPager2 slider;
    TabLayout sildertabs;
    TextView textView;
    GoogleMap mMap;
    DrawerLayout drawerLayout;
    CardView menu;
    Button search;
    CardView tire,changebattery,fuel,cant_detect_issue,motor,changeoil;
     Dialog searchfortech_dialog;
     LinearLayout choosen_car;
     TextView paymentway;
     AutoCompleteTextView location;
     TextView tiretxt,changebatterytxt,fueltxt,cant_detect_issuetxt,motortxt,changeoiltxt;
    Boolean retval;
    CardView currentlocation;
    apis_interface apiService;
    String car_id="",issue="",address="",location_lat_lng="";
    userdata userdata;
    Marker marker;
    ProgressBar create_order_loading;
    recyclerviewadapter recyclerviewadapter;
    ArrayList<branchmodel> branchmodels;
    RecyclerView recylerview;
     Handler handler;
     Runnable run;
     LinearLayout cancelling;
     LinearLayout wait;
     TextView choose_car;
    TextView carname;
    ImageView carImage;
    CardView setcar;
   LinearLayout car_info_layout;
   String card_id="";
   String state="";
   BottomSheetDialog state_dialog;
   CardView voucher_layout;
   TextView voucher;
   public static Boolean isEmpty=false;
     String paymentway_str="Cash";
    private ArrayList<Marker> tech_marker;
    LocationManager locationManager;
    @Override
    protected void onResume() {
        super.onResume();
        retval=true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main2);
        currentlocation=findViewById(R.id.current_location);
        SupportMapFragment mapFragment1 = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment1.getMapAsync(MainActivity2.this);
        mapFragment1.onLowMemory();
        mapFragment1.onStop();
        mapFragment1.onResume();
        insit();
        setstate();
        clickListners();
        getvoucher();
        displaydrawerviews();
        close_bottomdrawer();
        locationManager  = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else {
         locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 0, 0, MainActivity2.this);
        }
    }

    private void getvoucher() {
        Call<String> getvouter=apiService.getvouter(userdata.getId());
        getvouter.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if(response.body().equals("0")){
                        voucher_layout.setVisibility(View.GONE);
                    }
                    else{
                        voucher_layout.setVisibility(View.VISIBLE);
                        voucher.setText(response.body().split("/")[0]+"% up to "+response.body().split("/")[1]+" L.E");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void setlocation(String latlng) {
        String latlangstr=  latlng.substring(latlng.indexOf('(')+1,latlng.indexOf(')'));
        String[] latlang=latlangstr.split(",");
        LatLng latLng=new LatLng(Double.parseDouble(latlang[0]),Double.parseDouble(latlang[1]));
        if (marker==null) {
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("choosing location").icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder1)));
        }
        else {

            marker.setPosition(latLng);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        address=getaddress(latLng);
        location.setText(address);
        location_lat_lng=latLng.toString();

    }

    private void displaydrawerviews() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.home_nav);
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

                Intent intent=new Intent(MainActivity2.this,wallet.class);
                startActivity(intent);
                return true;
            }
        });
        menu.findItem(R.id.cars).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent=new Intent(MainActivity2.this,my_cars.class);
                startActivity(intent);
                return true;
            }
        });
        menu.findItem(R.id.history).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent=new Intent(MainActivity2.this,order_history.class);
                startActivity(intent);
                return true;
            }
        });
        menu.findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                shared_preference.remove_user_data(MainActivity2.this);
                Intent intent=new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startchoose_location() {
        retval=false;
        Intent intent = new Intent(MainActivity2.this, change_location.class);
        intent.putExtra("location",location_lat_lng);
        Pair[] pairs = new Pair[3];
        pairs[0] = Pair.create(location, "location");
        pairs[1] = Pair.create(findViewById(R.id.map2), "map");
        pairs[2] = Pair.create(findViewById(R.id.current_location), "current_location");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity2.this, pairs);
        startActivityForResult(intent,100,options.toBundle());
    }

    private userdata getuserdata() {
        return shared_preference.get_user_data(this);


    }

    private boolean isbottomdrawer_open() {
        if (location.getVisibility()==View.VISIBLE){

            return false;
        }
        else {

            return true;
        }
    }

    private void insit() {
        apiService = client.start().create(apis_interface.class);
        userdata= getuserdata();
        handler=new Handler();
        choosen_car=findViewById(R.id.choosencar);
        paymentway=findViewById(R.id.payment_way);
        location=findViewById(R.id.location);
        drawerLayout=findViewById(R.id.drawer_layout);
        search=findViewById(R.id.search_for_providers);
        menu=findViewById(R.id.menu);
        slider=findViewById(R.id.slider);
        sildertabs=findViewById(R.id.sildertabs);
        textView=findViewById(R.id.location);
        motor=findViewById(R.id.motor_issue);
        cant_detect_issue=findViewById(R.id.cant_detect_issue);
        changebattery=findViewById(R.id.carbattary);
        changeoil=findViewById(R.id.changeoil);
        tire=findViewById(R.id.tire);
        fuel=findViewById(R.id.fuel);
        cant_detect_issuetxt=findViewById(R.id.cant_detect_issue_txt);
        changebatterytxt=findViewById(R.id.battery_txt);
        changeoiltxt=findViewById(R.id.oil_txt);
        changeoiltxt=findViewById(R.id.oil_txt);
        tiretxt=findViewById(R.id.tire_txt);
        fueltxt=findViewById(R.id.fuel_txt);
        choose_car=findViewById(R.id.choose_car);
        create_order_loading=findViewById(R.id.create_order_loading);
        carname=findViewById(R.id.carname);
        carImage=findViewById(R.id.carimage);
        setcar=findViewById(R.id.setcar);
        car_info_layout=findViewById(R.id.car_info_layout);
        state=shared_preference.getstate(this);
        tech_marker=new ArrayList<Marker>();
       location_lat_lng= getIntent().getStringExtra("location");
       voucher=findViewById(R.id.voucher_value);
       voucher_layout=findViewById(R.id.voucher_box);
    }

    private void clickListners() {
        paymentway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosepayment_way();
            }
        });
        setcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_car();
            }
        });
        choose_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_car();
            }
        });
        motor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issue="motor";
                motor.setCardBackgroundColor(Color.rgb(35,124,204));
                changebattery.setCardBackgroundColor(Color.WHITE);
                changeoil.setCardBackgroundColor(Color.WHITE);
                cant_detect_issue.setCardBackgroundColor(Color.WHITE);
                tire.setCardBackgroundColor(Color.WHITE);
                fuel.setCardBackgroundColor(Color.WHITE);
                if (isbottomdrawer_open()){
                    open_bottomdrawer();}
            }
        });
        tire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issue="tire";
                motor.setCardBackgroundColor(Color.WHITE);
                changebattery.setCardBackgroundColor(Color.WHITE);
                changeoil.setCardBackgroundColor(Color.WHITE);
                cant_detect_issue.setCardBackgroundColor(Color.WHITE);
                tire.setCardBackgroundColor(Color.rgb(35,124,204));
                fuel.setCardBackgroundColor(Color.WHITE);
                if (isbottomdrawer_open()){
                    open_bottomdrawer();}
            }
        });
        cant_detect_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issue="can't detect issue";
                motor.setCardBackgroundColor(Color.WHITE);
                changebattery.setCardBackgroundColor(Color.WHITE);
                changeoil.setCardBackgroundColor(Color.WHITE);
                cant_detect_issue.setCardBackgroundColor(Color.rgb(35,124,204));
                tire.setCardBackgroundColor(Color.WHITE);
                fuel.setCardBackgroundColor(Color.WHITE);
                if (isbottomdrawer_open()){
                    open_bottomdrawer();}
            }
        });
        changebattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issue="change or charge battery";
                motor.setCardBackgroundColor(Color.WHITE);
                changebattery.setCardBackgroundColor(Color.rgb(35,124,204));
                changeoil.setCardBackgroundColor(Color.WHITE);
                cant_detect_issue.setCardBackgroundColor(Color.WHITE);
                tire.setCardBackgroundColor(Color.WHITE);
                fuel.setCardBackgroundColor(Color.WHITE);
                if (isbottomdrawer_open()){
                    open_bottomdrawer();}
            }
        });
        changeoil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issue="change oil";
                motor.setCardBackgroundColor(Color.WHITE);
                changebattery.setCardBackgroundColor(Color.WHITE);
                changeoil.setCardBackgroundColor(Color.rgb(35,124,204));
                cant_detect_issue.setCardBackgroundColor(Color.WHITE);
                tire.setCardBackgroundColor(Color.WHITE);
                fuel.setCardBackgroundColor(Color.WHITE);
                if (isbottomdrawer_open()){
                    open_bottomdrawer();}
            }
        });
        fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issue="fuel";
                motor.setCardBackgroundColor(Color.WHITE);
                changebattery.setCardBackgroundColor(Color.WHITE);
                changeoil.setCardBackgroundColor(Color.WHITE);
                cant_detect_issue.setCardBackgroundColor(Color.WHITE);
                tire.setCardBackgroundColor(Color.WHITE);
                fuel.setCardBackgroundColor(Color.rgb(35,124,204));
                if (isbottomdrawer_open()){
                    open_bottomdrawer();}
            }
        });

        location.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (retval) {
                    startchoose_location();
                }
                return true;
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(Gravity.RIGHT);

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();



            }
        });
    }

    private void setstate() {

        if (state.equals("finished")){
            state_dialog=new BottomSheetDialog(this);
            state_dialog.setContentView(R.layout.activity_rate_screen);
            state_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            state_dialog.setCancelable(false);
            state_dialog.show();
            ImageView close=state_dialog.findViewById(R.id.close_rating);
            Button submit_rating=state_dialog.findViewById(R.id.submit_rating);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    state_dialog.cancel();
                    shared_preference.setstate(MainActivity2.this,"");
                }
            });
            submit_rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    state_dialog.cancel();
                    shared_preference.setstate(MainActivity2.this,"");
                }
            });


        }
        else if (state.contains("searching for technician")){

            startsearchScreen(state.split("-")[1]);

        }

       else if (state.equals("cancelled")){
            state_dialog=new BottomSheetDialog(this);
            state_dialog.setContentView(R.layout.cancel_request_screen);

            state_dialog.show();
            ImageView close=state_dialog.findViewById(R.id.close_rating);
            shared_preference.setstate(MainActivity2.this,"");
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    state_dialog.cancel();
                }
            });


        }
    }

    private void choose_car() {
        BottomSheetDialog     bottomSheetDialog = new BottomSheetDialog(MainActivity2.this);
        bottomSheetDialog.setContentView(R.layout.choose_car);
        RecyclerView recyclerView=bottomSheetDialog.findViewById(R.id.mycars);
        TextView dont_add_car=bottomSheetDialog.findViewById(R.id.dont_add_car2);
        LottieAnimationView lottieAnimationView=bottomSheetDialog.findViewById(R.id.loading_choose_car);
        ArrayList<car_model> car_models=new ArrayList<>();
        lottieAnimationView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        dont_add_car.setVisibility(View.GONE);
        recyclerView.setEnabled(false);
        Choose_car_adpt Choose_car_adpt=new Choose_car_adpt(car_models,this);
        recyclerView.setAdapter(Choose_car_adpt);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        setcar.setVisibility(View.GONE);
                        choose_car.setVisibility(View.VISIBLE);
                        car_info_layout.setVisibility(View.VISIBLE);
                        carname.setText(car_models.get(position).getBrand()+" "+car_models.get(position).getModel());
                        car_id=Integer.toString(car_models.get(position).getId());
                        if (car_models.get(position).getBrand().toLowerCase().equals("citroën")){
                            int resourceId = getResources().getIdentifier("citroen", "drawable",
                                    getPackageName());//initialize res and context in adapter's contructor
                            carImage.setImageResource(resourceId);
                        }
                        else if (car_models.get(position).getBrand().toLowerCase().equals("rolls-royce")){
                            int resourceId = getResources().getIdentifier("rolls_royce", "drawable",
                                    getPackageName());//initialize res and context in adapter's contructor
                            carImage.setImageResource(resourceId);
                        }
                        else if (car_models.get(position).getBrand().toLowerCase().equals("mercedes benz")){
                            int resourceId = getResources().getIdentifier("mercedes_benz", "drawable",
                                    getPackageName());//initialize res and context in adapter's contructor
                            carImage.setImageResource(resourceId);
                        }
                        else if (car_models.get(position).getBrand().toLowerCase().equals("ssang yong")){
                            int resourceId = getResources().getIdentifier("ssang_yong", "drawable",
                                    getPackageName());//initialize res and context in adapter's contructor
                            carImage.setImageResource(resourceId);
                        }
                        else if (car_models.get(position).getBrand().toLowerCase().equals("land rover")){
                            int resourceId = getResources().getIdentifier("land_rover", "drawable",
                                    getPackageName());//initialize res and context in adapter's contructor
                            carImage.setImageResource(resourceId);
                        }
                        else {
                            int resourceId = getResources().getIdentifier(car_models.get(position).getBrand().toLowerCase(), "drawable",
                                    getPackageName());//initialize res and context in adapter's contructor
                            carImage.setImageResource(resourceId);
                        }
                        bottomSheetDialog.cancel();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        Call<ArrayList<car_model>> getcars=apiService.get_user_data(userdata.getId(),userdata.getToken());
        getcars.enqueue(new Callback<ArrayList<car_model>>() {
            @Override
            public void onResponse(Call<ArrayList<car_model>> call, Response<ArrayList<car_model>> response) {

                lottieAnimationView.setVisibility(View.INVISIBLE);


                if (response.isSuccessful()){
                    if(response.body().size()<=0){

                        dont_add_car.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                        Intent i=new Intent(MainActivity2.this,my_cars.class);
                        startActivity(i);
                        bottomSheetDialog.cancel();
                    }else {
                        dont_add_car.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setEnabled(true);
                        car_models.clear();
                        for (int i = 0; i < response.body().size(); i++) {


                            car_models.add(response.body().get(i));

                        }
                        Choose_car_adpt.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<car_model>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                dont_add_car.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                lottieAnimationView.setVisibility(View.INVISIBLE);

            }
        });
bottomSheetDialog.show();

    }

    private void choosepayment_way() {

   BottomSheetDialog     bottomSheetDialog = new BottomSheetDialog(MainActivity2.this);
        bottomSheetDialog.setContentView(R.layout.choose_pqyment);
        TextView cash=bottomSheetDialog.findViewById(R.id.cash);
        TextView visa=bottomSheetDialog.findViewById(R.id.visa);
        LinearLayout visalayout=bottomSheetDialog.findViewById(R.id.viewcardslayout);
        ProgressBar getcards=bottomSheetDialog.findViewById(R.id.getcards);
        ImageView viewcards=bottomSheetDialog.findViewById(R.id.viewcards);
        RecyclerView recyclerView =bottomSheetDialog.findViewById(R.id.cards);
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                paymentway.setText("Cash");
                Drawable cash = getResources().getDrawable( R.drawable.money );
                cash.setBounds(0, 0, 60, 60);
                Drawable arrow = getResources().getDrawable( R.drawable.ic_baseline_keyboard_arrow_right_24 );
                arrow.setBounds(0, 0, 60, 60);
                paymentway.setCompoundDrawablesRelative(cash,null,arrow,null);
                paymentway_str="Cash";
            }
        });
        visalayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewcards.setVisibility(View.INVISIBLE);
                getcards.setVisibility(View.VISIBLE);
                Call<String> getusercards=apiService.get_user_cards(userdata.getId());
                    getusercards.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            viewcards.setVisibility(View.VISIBLE);
                            getcards.setVisibility(View.INVISIBLE);

                            if (response.isSuccessful())
                            {
                                if (response.body().equals("Empty") || response.body().equals("[]")){

                                    Intent i=new Intent(MainActivity2.this,wallet.class);
                                        startActivity(i);

                                }
                                else {
                                    viewcards.setVisibility(View.VISIBLE);
                                    getcards.setVisibility(View.INVISIBLE);

                                   ArrayList<cards_model> cards_models=new ArrayList<>();
                                    cards_adpt cards_adpt=new cards_adpt(cards_models,MainActivity2.this,false);
                                    recyclerView.setAdapter(cards_adpt);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
                                    recyclerView.addOnItemTouchListener(
                                            new RecyclerItemClickListener(MainActivity2.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                                                @Override public void onItemClick(View view, int position) {
                                                    paymentway.setText(cards_models.get(position).getMasked_pan());
                                                    Drawable visa;
                                                    if(cards_models.get(position).getType().equals("MasterCard")) {
                                                        visa  = getResources().getDrawable(R.drawable.mastercard);
                                                    }
                                                   else if(cards_models.get(position).getType().equals("Visa")) {
                                                        visa  = getResources().getDrawable(R.drawable.visa);
                                                    }
                                                    else{
                                                        visa=getResources().getDrawable(R.drawable.creditcard);
                                                    }
                                                    visa.setBounds(0, 0, 60, 60);
                                                    Drawable arrow = getResources().getDrawable( R.drawable.ic_baseline_keyboard_arrow_right_24 );
                                                    arrow.setBounds(0, 0, 60, 60);
                                                    paymentway.setCompoundDrawablesRelative(visa,null,arrow,null);
                                                    bottomSheetDialog.cancel();
                                                    card_id=cards_models.get(position).getId();
                                                    paymentway_str="Credit/Debit Card";
                                                }

                                                @Override public void onLongItemClick(View view, int position) {
                                                    // do whatever
                                                }
                                            })
                                    );
                                    try {
                                        JSONArray data= new JSONArray(response.body());
                                        if (data!=null){
                                            cards_models.clear();
                                            for (int i=0;i<data.length();i++){

                                                cards_model cards_model=new cards_model(Integer.toString(data.optJSONObject(i).optInt("id")),data.optJSONObject(i).optString("token"),
                                                        data.optJSONObject(i).optString("mask_pan"),data.optJSONObject(i).optString("type"));
                                                cards_models.add(cards_model);

                                            }
                                            cards_adpt.notifyDataSetChanged();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            viewcards.setVisibility(View.VISIBLE);
                            getcards.setVisibility(View.INVISIBLE);

                        }
                    });
            }
        });
        bottomSheetDialog.show();

    }

    private void open_bottomdrawer() {
        paymentway.setVisibility(View.VISIBLE);
        paymentway.setVisibility(View.VISIBLE);
        choosen_car.setVisibility(View.VISIBLE);
        location.setVisibility(View.VISIBLE);
      //  open_close.animate().rotationBy(-180).setDuration(300).start();
    }

    private void close_bottomdrawer() {
        paymentway.setVisibility(View.GONE);
        paymentway.setVisibility(View.GONE);
        choosen_car.setVisibility(View.GONE);
        location.setVisibility(View.GONE);
    //    open_close.animate().rotationBy(180).setDuration(300).start();
    }

    private void showBottomSheetDialog() {
        if (issue==""){
                Toast.makeText(this, "please choose the issue do you face", Toast.LENGTH_SHORT).show();
        }
        if (car_id.equals("")){
            Toast.makeText(this, "please set your car", Toast.LENGTH_SHORT).show();
        }
        else {
            create_order_loading.setVisibility(View.VISIBLE);
            search.setVisibility(View.GONE);
        Call<String> create_order=apiService.create_order(location_lat_lng,userdata.getId(),car_id,address,paymentway_str,issue,userdata.getToken(),card_id);
        create_order.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                create_order_loading.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                if (response.isSuccessful()){

                    if (!response.body().equals("logout")) {

                        try {
                            JSONObject data = new JSONObject(response.body());
                            if (data != null) {
                            //    new FCMMessages().sendMessageMulti(MainActivity2.this, "New requests", "there is New requests check it now.", null,issue);

                                startsearchScreen(Integer.toString(data.optInt("id")));

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }








            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                create_order_loading.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
            }
        });

       }

    }

    private void startsearchScreen(String order_id) {

        searchfortech_dialog = new Dialog(MainActivity2.this);
        searchfortech_dialog.setContentView(R.layout.activity_search_for_provider);
        searchfortech_dialog.setCancelable(false);
        searchfortech_dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        searchfortech_dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_White);
        recylerview=searchfortech_dialog.findViewById(R.id.recyclerview);
        cancelling=searchfortech_dialog.findViewById(R.id.cancelling);
        TextView cancel=searchfortech_dialog.findViewById(R.id.cancel);
        wait=searchfortech_dialog.findViewById(R.id.wait);
        //      progress=searchfortech_dialog.findViewById(R.id.wait_for_tech_progress);



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation_dialog(order_id);

            }
        });
        branchmodels=new ArrayList<>();
        recyclerviewadapter=new recyclerviewadapter(branchmodels,MainActivity2.this);
        recylerview.setAdapter(recyclerviewadapter);
        recylerview.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
        recylerview.setNestedScrollingEnabled(false);
        recylerview.setItemViewCacheSize(150000);
        recyclerviewadapter.notifyDataSetChanged();
        if (!searchfortech_dialog.isShowing()){
        searchfortech_dialog.show();
        }
        if (handler!=null){
            handler.removeCallbacks(run);
        }
        run=new Runnable() {
            @Override
            public void run() {

                search_for_offer(order_id);
                handler.postDelayed(run,1000);

            }
        };
        handler.postDelayed(run,1000);

    }

    private void confirmation_dialog(String order_id) {
        Dialog cancel = new Dialog(MainActivity2.this);
        cancel.setContentView(R.layout.cancel_dialog);
        cancel.setCancelable(true);
        cancel.getWindow().setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        cancel.getWindow().setBackgroundDrawableResource(R.color.translucent);
            Button ok=cancel.findViewById(R.id.cancel_searching);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancel.cancel();
                    recylerview.setVisibility(View.INVISIBLE);
                    cancelling.setVisibility(View.VISIBLE);
                    wait.setVisibility(View.GONE);
                    recylerview.setEnabled(false);
                    Call<String> cancel_order=apiService.cancel_order(userdata.getId(),userdata.getToken(),Integer.parseInt(order_id));
                    cancel_order.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.isSuccessful()){
                                if (response.body().equals("cancelled")){
                                    searchfortech_dialog.cancel();

                                    if (handler!=null){
                                        handler.removeCallbacks(run);
                                    }
                                }

                            }

                            recylerview.setVisibility(View.VISIBLE);
                            cancelling.setVisibility(View.GONE);

                            recylerview.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            recylerview.setVisibility(View.VISIBLE);
                            cancelling.setVisibility(View.GONE);
                            recylerview.setEnabled(true);
                        }
                    });

                }
            });

            Button Continue=cancel.findViewById(R.id.Continue);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel.cancel();
            }
        });

cancel.show();
    }

    private void search_for_offer(String id ) {
        Call<String> create_order=apiService.getoffers(id,userdata.getId(),userdata.getToken());
        create_order.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()&& response!=null){


                if (response.body().equals("Empty")){
                    branchmodels.clear();
                    recyclerviewadapter.notifyDataSetChanged();
                    if (cancelling.getVisibility()==View.GONE){
                    wait.setVisibility(View.VISIBLE);
                    recylerview.setVisibility(View.INVISIBLE);
recylerview.setEnabled(false);}
                }
                else {
                    if (cancelling.getVisibility()==View.GONE){

                        wait.setVisibility(View.INVISIBLE);
                    recylerview.setVisibility(View.VISIBLE);
                    recylerview.setEnabled(true);
                    }
                    try {
                        JSONArray data=new JSONArray(response.body());

                        if (data!=null) {
                            branchmodels.clear();
                            for (int i=0;i<data.length();i++){

                                    branchmodel branchmodel = new branchmodel("", data.getJSONObject(i).optString("name"), data.getJSONObject(i).optString("rate"), data.getJSONObject(i).optString("time"), data.getJSONObject(i).optInt("order_id"), data.getJSONObject(i).optString("distance"), data.getJSONObject(i).optString("amount"),data.getJSONObject(i).optString("technican_id"),data.getJSONObject(i).optString("technican_location"),true,data.getJSONObject(i).optString("device_token"));
                                    branchmodels.add(branchmodel);

                            }
                            recyclerviewadapter.notifyDataSetChanged();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }}



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map));
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        setlocation(getIntent().getStringExtra("location"));
        // setlocationmarker();
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                get_tech_locations();

                handler.postDelayed(this,1000);

            }
        };
        handler.postDelayed(runnable,1000);
        currentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
                //  currentlocation.this.location.setText(getaddress(latLng));

            }
        });





    }

    private void get_tech_locations() {

   Call<String> get_tech_locations=apiService.get_tech_locations();
    get_tech_locations.enqueue(new Callback<String>() {
     @Override
      public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()){
            try {
                JSONArray data=new JSONArray(response.body());

                if (data!=null){

                    for (int i=0;i<data.length();i++){
                        if (data.optJSONObject(i).optString("provider_location")!=null && !data.optJSONObject(i).optString("provider_location").equals("null")) {

                            String latlangstr = data.optJSONObject(i).optString("provider_location").substring(data.optJSONObject(i).optString("provider_location").indexOf('(') + 1, data.optJSONObject(i).optString("provider_location").indexOf(')'));
                            String[] latlang = latlangstr.split(",");
                            LatLng latLng = new LatLng(Double.parseDouble(latlang[0]), Double.parseDouble(latlang[1]));
                            if (tech_marker.size() > i) {

                                if (tech_marker.get(i) != null) {
                                    tech_marker.get(i).setPosition(latLng);
                                } }
                            else {

                                    tech_marker.add(mMap.addMarker(new MarkerOptions().position(latLng).title("technician").icon(BitmapDescriptorFactory.fromResource(R.drawable.small_tech_loaction))));
                                }

                        }}
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
if (resultCode==RESULT_OK){
        if (requestCode==100){
            location_lat_lng=data.getStringExtra("location");
            address=data.getStringExtra("address");
            location.setText(address);
         String latlangstr=  location_lat_lng.substring(location_lat_lng.indexOf('(')+1,location_lat_lng.indexOf(')'));
         String[] latlang=latlangstr.split(",");
            LatLng latLng=new LatLng(Double.parseDouble(latlang[0]),Double.parseDouble(latlang[1]));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
            marker.setPosition(latLng);
            locationManager.removeUpdates(MainActivity2.this);
          //  circle.setCenter(latLng);
        }}
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


    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        setlocation(latLng.toString());

    }
}
