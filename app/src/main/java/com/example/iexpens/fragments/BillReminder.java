package com.example.iexpens.fragments;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.iexpens.R;
import com.example.iexpens.activity.MainActivity;

public class BillReminder extends BroadcastReceiver {
    MediaPlayer mp;
    private static final String ACTION_UPDATE_NOTIFICATION ="com.android.example.notifyme.ACTION_UPDATE_NOTIFICATION";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;
    private static final String PRIMARY_CHANNEL_ID ="primary_notification_channel";
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createNotificationChannel(context);
        sendNotification(context);
    }

    public void createNotificationChannel(Context context) {
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Bill Notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Bill Channel Description");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    public void sendNotification(Context context) {
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(context,
                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(context);
        notifyBuilder.addAction(R.drawable.gift,"Bill Notification", updatePendingIntent);
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat
                .Builder(context, PRIMARY_CHANNEL_ID)
                .setContentTitle("Bill Reminder")
                .setContentText("PLease Pay the Bill")
                .setSmallIcon(R.drawable.gift)
                .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }
}
