package com.example.eslah;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class FCMMessages {
    private Context context;

    public void sendMessageSingle(Context context, final String recipient, final String title, final String body, final Map<String, String> dataMap)
    {
        this.context = context;

        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("body", body);
        notificationMap.put("title", title);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("data", notificationMap);
        rootMap.put("to", recipient);
        if (dataMap != null)
            rootMap.put("data", dataMap);

        new SendFCM().setFcm(rootMap).execute();
    }


    public void sendMessageMulti(Context context, final String title, final String body, final Map<String, String> dataMap,String topic) {
        this.context = context;

        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("body", body);
        notificationMap.put("title", title);
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("data", notificationMap);
        rootMap.put("to","/topics/" + topic);
        if (dataMap != null)
            rootMap.put("data", dataMap);

        new SendFCM().setFcm(rootMap).execute();
    }

    @SuppressLint("StaticFieldLeak")
    class SendFCM extends AsyncTask<String, String, String> {

        private String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        private Map<String, Object> fcm;

        SendFCM setFcm(Map<String, Object> fcm) {
            this.fcm = fcm;
            return this;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, new JSONObject(fcm).toString());
                Request request = new Request.Builder()
                        .url(FCM_MESSAGE_URL)
                        .post(body)
                        .addHeader("Authorization","key=" + "AAAAkgV-CcI:APA91bGyljknM632OUb90S2qSS-skToqpDQkXryLCyd81RjRJbmdHbgtI2M9edt5YDGEh7sNTvixs8mMZ8BK8AQ-b9IEn2H3YJg7z13LP3fHUqS8lR-44Y5xNXrmsQf6j4s75VHXLumr")
                        .build();
                Response response = new OkHttpClient().newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultJson = new JSONObject(result);
                int success, failure;
                success = resultJson.getInt("success");
                failure = resultJson.getInt("failure");
                Log.d(TAG, "onPostExecute: "+resultJson.toString());
                //Toast.makeText(context, "Sent: " + success + "/" + (success + failure), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
//                Toast.makeText(context, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
            }
        }
    }

}