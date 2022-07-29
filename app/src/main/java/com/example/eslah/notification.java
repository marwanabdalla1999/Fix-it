package com.example.eslah;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.*;
public class notification extends FirebaseMessagingService {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        String title=message.getData().get("title");
        String body=message.getData().get("body");
        String Channal_id="request";

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationChannel channel=new NotificationChannel(Channal_id,"request", NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder builder=new Notification.Builder(this,Channal_id);
        builder.setSmallIcon(R.drawable.logo1,2);
        builder.setWhen(Calendar.getInstance().getTimeInMillis());
        builder.setShowWhen(true);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setLargeIcon(BitmapFactory. decodeResource (getResources() , R.drawable.technician));
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setWhen(Calendar.getInstance().getTimeInMillis() );
        NotificationManagerCompat.from(this).notify(1,builder.build());
    }

}
