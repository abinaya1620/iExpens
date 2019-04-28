package com.example.iexpens.fragments;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.iexpens.R;
import com.example.iexpens.activity.MainActivity;

public class BillReminder extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        mp= MediaPlayer.create(context,  R.raw.alarm);
        mp.start();
        Toast.makeText(context, "Bill Scheduled", Toast.LENGTH_LONG).show();
        /*NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.gift)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent.getBroadcast()
        mNotificationManager.notify(1, mBuilder.build());*/
        Notification(context, "Wifi Connection On");
    }

    public void Notification(Context context, String message) {
        // Set Notification Title
        String strtitle = "Notification Recieved";
        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(context, MainActivity.class);
        // Send data to NotificationView Class
        //intent.putExtra("title", strtitle);
        //intent.putExtra("text", message);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        // Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
                // Set Icon
                .setSmallIcon(R.drawable.gift)
                // Set Ticker Message
                .setTicker(message)
                // Set Title
                .setContentTitle("New Notification")
                // Set Text
                .setContentText(message)
                // Add an Action Button below Notification
                .addAction(R.drawable.gift, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }

    // Check for network availability
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
