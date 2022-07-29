package com.example.eslah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.paymob.acceptsdk.IntentConstants;
import com.paymob.acceptsdk.PayActivity;
import com.paymob.acceptsdk.PayActivityIntentKeys;
import com.paymob.acceptsdk.PayResponseKeys;
import com.paymob.acceptsdk.SaveCardResponseKeys;
import com.paymob.acceptsdk.ThreeDSecureWebViewActivty;
import com.paymob.acceptsdk.ToastMaker;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.airbnb.lottie.L.TAG;

public class wallet extends AppCompatActivity {
Button addcard;
    apis_interface apiService;
    userdata userdata;
    ProgressBar progressBar_wallet;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        addcard=findViewById(R.id.add_card);
        back=findViewById(R.id.back_wallet);
        back.setOnClickListener(i->onBackPressed());
        progressBar_wallet=findViewById(R.id.progressBar_wallet);
        apiService = client.start().create(apis_interface.class);
        userdata= getuserdata();
            addcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getpayment_key();
                }
            });

            getcards();
            gettransactions();
        get_current_balance();
    }

    private void getpayment_key() {

        Call<String> generate_payment_key = apiService.generate_payment_key("100");

        generate_payment_key.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()){
                    generate_payment_sdk(response.body());

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onResponse:"+t.getMessage());

            }
        });
    }

    private void generate_payment_sdk(String payment_token) {

        Intent pay_intent = new Intent(this, PayActivity.class);
        putNormalExtras(pay_intent,payment_token);

        // this key is used to save the card by deafult.
        pay_intent.putExtra(PayActivityIntentKeys.SAVE_CARD_DEFAULT, true);

        // this key is used to display the savecard checkbox.
        pay_intent.putExtra(PayActivityIntentKeys.SHOW_SAVE_CARD, false);

        //this key is used to set the theme color(Actionbar, statusBar, button).
        pay_intent.putExtra(PayActivityIntentKeys.THEME_COLOR,getResources().getColor(R.color.black));

        // this key is to wether display the Actionbar or not.
        //   pay_intent.putExtra("ActionBar",true);

        // this key is used to define the language. takes for ex ("ar", "en") as inputs.
        pay_intent.putExtra("language","en");
        pay_intent.putExtra("ActionBar",false);
        startActivityForResult(pay_intent, 101);
        Intent secure_intent = new Intent(wallet.this, ThreeDSecureWebViewActivty.class);
        secure_intent.putExtra("ActionBar",false);
    }
    private void putNormalExtras(Intent intent,String payment_token) {
        intent.putExtra(PayActivityIntentKeys.PAYMENT_KEY, payment_token);
        intent.putExtra(PayActivityIntentKeys.THREE_D_SECURE_ACTIVITY_TITLE, "Verification");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 101) {
            Bundle extras = data.getExtras();
            Log.d(TAG, "onActivityResult: successfully");
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

                // Use the static keys declared in PayResponseKeys to extract the fields you want

            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_PARSING_ISSUE) {
                // User finished their payment successfully. An error occured while reading the returned JSON.
                ToastMaker.displayShortToast(this, "TRANSACTION_SUCCESSFUL - Parsing Issue");

                // ToastMaker.displayShortToast(this, extras.getString(IntentConstants.RAW_PAY_RESPONSE));
            } else if (resultCode == IntentConstants.TRANSACTION_SUCCESSFUL_CARD_SAVED) {
                // User finished their payment successfully and card was saved.

                // Use the static keys declared in PayResponseKeys to extract the fields you want
                // Use the static keys declared in SaveCardResponseKeys to extract the fields you want

             //   ToastMaker.displayLongToast(this, "Token == " + extras.getString(SaveCardResponseKeys.TOKEN));
                        savecard(extras.getString(SaveCardResponseKeys.CARD_SUBTYPE),extras.getString(SaveCardResponseKeys.TOKEN),extras.getString(SaveCardResponseKeys.MASKED_PAN));

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

    private void savecard(String type,String token,String masked_pan) {
        Call<String> savecard=apiService.add_card(type,userdata.getId(),token,masked_pan);
        savecard.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (response.body().equals("card has been added")){

                            getcards();

                    }



                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private userdata getuserdata() {
        return shared_preference.get_user_data(this);


    }

  public void getcards(){
      TextView credit_cards_txt=findViewById(R.id.credit_cards_txt);
      RecyclerView recyclerView=findViewById(R.id.cards_recycler);
      ArrayList<cards_model> cards_models=new ArrayList<>();
      cards_adpt cards_adpt=new cards_adpt(cards_models,this,true);
      recyclerView.setAdapter(cards_adpt);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Call<String> getcards=apiService.get_user_cards(userdata.getId());
        getcards.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: "+response);
                if (response.isSuccessful()){
                    if (!response.body().equals("Empty")){
                        recyclerView.setVisibility(View.VISIBLE);
                        credit_cards_txt.setVisibility(View.VISIBLE);
                        try {
                            JSONArray data=new JSONArray(response.body());

                            if (data!=null){
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
                    else {

                        recyclerView.setVisibility(View.GONE);
                        credit_cards_txt.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


  }
  void gettransactions(){
      RecyclerView recyclerView=findViewById(R.id.transcations);
      ArrayList<transactions_model> transactions_models=new ArrayList<>();
      transactions_adpt transactions_adpt=new transactions_adpt(transactions_models,this);
      recyclerView.setAdapter(transactions_adpt);
      LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,true);
      linearLayoutManager.setStackFromEnd(true);
      recyclerView.setLayoutManager(linearLayoutManager);
            Call<String> gettransactions=apiService.get_transactions(userdata.getId());
            gettransactions.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "onResponse: "+response);
                    if (response.isSuccessful()){
                        if (!response.body().equals("Empty")){
                            try {
                                JSONArray data=new JSONArray(response.body());

                                if (data!=null){
                                    for (int i=0;i<data.length();i++){
                                        transactions_model transactions_model=new transactions_model(data.optJSONObject(i).optString("order_name"),data.optJSONObject(i).optString("amount"),data.optJSONObject(i).optString("created_at"));
                                        transactions_models.add(transactions_model);

                                    }
                                    transactions_adpt.notifyDataSetChanged();


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });


  }

    void get_current_balance(){
        TextView balance=findViewById(R.id.my_balance);
        balance.setVisibility(View.INVISIBLE);
        progressBar_wallet.setVisibility(View.VISIBLE);
        Call<String> get_current_balance=apiService.get_current_balance(userdata.getId());
        get_current_balance.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    if (!response.body().equals("not_found")){
                            balance.setText(response.body()+" EGP");
                        balance.setVisibility(View.VISIBLE);
                        progressBar_wallet.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }
}