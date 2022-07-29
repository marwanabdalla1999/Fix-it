package com.example.eslah;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class myorder_adpter extends RecyclerView.Adapter<myorder_adpter.Holder>  {
    ArrayList<order_model> order_models;
    Context context;
    private Resources res = null;
    Handler handler;
    public myorder_adpter(ArrayList<order_model> order_models, Context context) {
        this.order_models = order_models;
        this.context = context;
        res=context.getResources();


    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_model,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.issue.setText(order_models.get(position).getIssue());
        holder.car.setText(order_models.get(position).getCar_id());
        holder.date.setText(order_models.get(position).getCreated_at().split("T")[0]);
        holder.address.setText(order_models.get(position).getAddress());
holder.payment_way.setText(order_models.get(position).getPayment_way());
        holder.cost.setText(order_models.get(position).getAmount()+" EGP");
        if (order_models.get(position).getState().equals("finished")){
            holder.state.setTextColor(Color.rgb(6,135,11));
            holder.state.setText("Finished");
        }
        else if (order_models.get(position).getState().equals("cancelled")){
            holder.state.setTextColor(Color.rgb(243,45,40));
            holder.state.setText("Cancelled");
        }
        else {
            holder.state.setTextColor(Color.rgb(35,124,204));
            holder.state.setText(order_models.get(position).getState());
        }

LatLng latLng=getlatlng(order_models.get(position).getLocation_lat_lng());


        GoogleMap thisMap = holder.mapCurrent;
        if (thisMap==null) {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    GoogleMap thisMap = holder.mapCurrent;
                    if (thisMap != null) {
                        thisMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        thisMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder2)));
                        handler.removeCallbacksAndMessages(this);

                    }
                    else {

                        handler.postDelayed(this, 1000);
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
        }
        else {
            thisMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            thisMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder2)));
        }
    }

    private LatLng getlatlng(String location_lat_lng) {

        String latlangstr=  location_lat_lng.substring(location_lat_lng.indexOf('(')+1,location_lat_lng.indexOf(')'));
        String[] latlang=latlangstr.split(",");
        LatLng latLng=new LatLng(Double.parseDouble(latlang[0]),Double.parseDouble(latlang[1]));

        return latLng;
    }

    @Override
    public int getItemCount() {
        return order_models.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }



    public class Holder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        TextView issue,car,date,cost,address,state;
        ImageView location;
        GoogleMap mapCurrent;
        MapView map;
        Boolean retval=false;
        TextView payment_way;
        public Holder(@NonNull View itemView) {
            super(itemView);
            issue=itemView.findViewById(R.id.issue);
            car=itemView.findViewById(R.id.car);
            date=itemView.findViewById(R.id.date);
            cost=itemView.findViewById(R.id.cost);
            address=itemView.findViewById(R.id.address);
            state=itemView.findViewById(R.id.state);
            payment_way=itemView.findViewById(R.id.payment_way);
            map = (MapView) itemView.findViewById(R.id.location_in_map);
            if (map != null)
            {
                map.onCreate(null);
                map.onResume();
                map.getMapAsync(this);

            }
           // location=itemView.findViewById(R.id.location_in_map);
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            mapCurrent = googleMap;
            mapCurrent.setMapStyle(MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.map));
            mapCurrent.getUiSettings().setAllGesturesEnabled(false);
            mapCurrent.getUiSettings().setScrollGesturesEnabled(false);
            mapCurrent.stopAnimation();
            mapCurrent.setBuildingsEnabled(false);
            mapCurrent.setTrafficEnabled(false);

            if (!retval){
           //     notifyDataSetChanged();
                retval=true;
            }

        }
    }


}
