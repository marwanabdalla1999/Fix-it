package com.example.eslah;

import com.example.eslah.car_data.car_data;
import com.example.eslah.car_data.car_type;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apis_interface {


    @POST("/api/register")
    Call<String> check_phone(@Query("phone") String phone);

    @POST("/api/change_name")
    Call<String> change_name(@Query("phone") String phone, @Query("name") String name);

    @POST("/api/verfiy_otp")
    Call<String> checkotp(@Query("phone") String phone, @Query("otp") String otp,@Query("device_token") String device_token);


    @POST("/api/session")
    Call<String> session(@Query("id") String id, @Query("token") String token,@Query("device_token") String device_token);


    @POST("/api/payment")
    Call<String> generate_payment_key(@Query("amount") String amount);

    @POST("/api/create_order")
    Call<String> create_order(@Query("location_lat_lng") String location_lat_lng,
                              @Query("user_id") String user_id, @Query("car_id") String car_id,
                              @Query("address") String address, @Query("payment_way") String payment_way,
                              @Query("issue") String issue, @Query("token") String token, @Query("card_id") String card_id);


    @GET("/api/getoffers")
    Call<String> getoffers(@Query("order_id") String order_id,
                           @Query("user_id") String user_id,
                           @Query("token") String token);

    @POST("/api/cancel_order")
    Call<String> cancel_order(@Query("user_id") String user_id, @Query("token") String token
            , @Query("order_id") int order_id);

    @GET("/api/get_cars_data")
    Call<car_data> get_cars_data(@Query("version") String version);


    @POST("/api/add_user_car")
    Call<String> add_user_car(@Query("id") String id, @Query("token") String token
            , @Query("type") String type
            , @Query("brand") String brand
            , @Query("model") String model
            , @Query("year") String year
            , @Query("color") String color

    );

    @POST("/api/get_user_data")
    Call<ArrayList<car_model>> get_user_data(@Query("id") String id, @Query("token") String token
    );

    @POST("/api/get_user_history")
    Call<ArrayList<order_model>> get_user_history(@Query("id") String id, @Query("token") String token
    );

    @POST("/api/update_order_data")
    Call<String> update_order_data(@Query("user_id") String user_id, @Query("token") String token,
                                             @Query("order_id") String order_id,
                                   @Query("state") String state,
                                   @Query("tech_id") String tech_id,
                                       @Query("amount") String amount,
                                   @Query("time") String time,
                                   @Query("distance") String distance,
                                   @Query("technican_location") String technican_location

                                   );


    @POST("/api/getOrder_data")
    Call<String> getOrder_data(@Query("user_id") String user_id, @Query("token") String token
    );
    @GET("/api/order_data")
    Call<String> update_Order_data(@Query("order_id") String order_id
    );

    @POST("/api/add_card")
    Call<String> add_card(@Query("type") String type,@Query("id") String user_id, @Query("token") String token,
                           @Query("masked_pan") String masked_pan
    );

    @GET("/api/get_user_cards")
    Call<String> get_user_cards(@Query("id") String user_id);
    @GET("/api/get_tech_locations")
    Call<String> get_tech_locations();

    @POST("/api/getcard_data")
    Call<String> get_card_data(@Query("card_id") String card_id);

    @POST("/api/payed_amount")
    Call<String> payed_amount(@Query("order_id") String order_id,
                              @Query("payed_amount") String payed_amount,@Query("amount_from_wallet") String amount_from_wallet,
                              @Query("remain_in_wallet") String remain_in_wallet,
                              @Query("voucher") String voucher,
                              @Query("payment_way") String payment_way);

    @GET("/api/delete_over_flow_offers")
    Call<String> reserve_tech(@Query("id") String id);

    @POST("/api/register_token")
    Call<String> send(@Query("id") String id,@Query("token") String token);

    @GET("/api/get_transactions")
    Call<String> get_transactions(@Query("id") String id);

    @GET("/api/get_current_balance")
    Call<String> get_current_balance(@Query("id") String id);


    @GET("/api/getvouter")
    Call<String> getvouter(@Query("id") String id);


    @POST("/api/delete_card")
    Call<String> delete_card(@Query("id") String id);

    @POST("/api/delete_car")
    Call<String> delete_car(@Query("id") String id);
}