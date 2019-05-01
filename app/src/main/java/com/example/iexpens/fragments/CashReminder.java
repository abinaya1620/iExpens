package com.example.iexpens.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
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
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.iexpens.R;
import com.example.iexpens.activity.CashWallet;
import com.example.iexpens.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CashReminder extends IntentService {
    MediaPlayer mp;
    private static final String ACTION_UPDATE_NOTIFICATION ="com.android.example.notifyme.ACTION_UPDATE_NOTIFICATION";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;
    private static final String PRIMARY_CHANNEL_ID ="primary_notification_channel";
    private FirebaseAuth mAuth;
    private String CashAmount = "0";

    public CashReminder(){
        super("CashReminder");
    }
    public String getCashAmount() {
        return CashAmount;
    }

    public void setCashAmount(String cashAmount) {
        CashAmount = cashAmount;
    }
    //@Override
    public void onReceive(Context context, Intent intent) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String user_Id = user.getUid();
        DatabaseReference databaseWallet = FirebaseDatabase.getInstance().getReference().child(user_Id).child("WALLET");
        Log.i("Cash Notification","Amount before DB "+getCashAmount());
        databaseWallet.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot cashSnapshot : dataSnapshot.getChildren()) {
                    CashWallet cashWallet = cashSnapshot.getValue(CashWallet.class);
                    Log.i("Cash Notification","Amount from DB "+cashWallet.getCash());
                    setCashAmount(cashWallet.getCash());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        createNotificationChannel(context);
        sendNotification(context);
    }

    public void createNotificationChannel(Context context) {
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Cash Reminder",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Cash Channel Description");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    public void sendNotification(Context context) {
        int dbCashAmount = 0;
        Log.i("Cash Notification send",getCashAmount());
        try{
            dbCashAmount = Integer.parseInt(getCashAmount());
            Log.i("Cash Notification","Amount Fetched : "+dbCashAmount);
        }catch(Exception e){
            Log.i("Cash Notification","Exception in fetching");
            dbCashAmount = 0;
        }
        Log.i("Cash Notification","Check Cash : " + dbCashAmount);
        if(dbCashAmount <= 1000){
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(context, notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("Cash Notification","True");
            Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
            PendingIntent updatePendingIntent = PendingIntent.getBroadcast(context,NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notifyBuilder = getNotificationBuilder(context);
            notifyBuilder.addAction(R.drawable.gift,"Cash Reminder", updatePendingIntent);
            mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        }else{
            Log.i("Cash Notification","False");
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat
                .Builder(context, PRIMARY_CHANNEL_ID)
                .setContentTitle("Low Cash Reminder")
                .setContentText("Cash is low in the Wallet. Please replenish the resources.")
                .setSmallIcon(R.drawable.gift)
                .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        onReceive(getApplicationContext(),intent);
    }
}
