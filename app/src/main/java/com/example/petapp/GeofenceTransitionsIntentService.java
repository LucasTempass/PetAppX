package com.example.petapp;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.GeofencingEvent;

public class GeofenceTransitionsIntentService extends IntentService {
    
    protected static final String C_TAG = "GeofenceTransitions";
    
    public GeofenceTransitionsIntentService() {
        super(C_TAG);
        
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManagerCompat iNotificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String iName = getString(R.string.KeyChannelName);
            String iDescription = getString(R.string.TxtChannelDescription);
            int iImportance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel iChannel = new NotificationChannel(getString(R.string.KeyChannelID), iName, iImportance);
            iChannel.setDescription(iDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(iChannel);
        }
        Intent iNotifyIntent = new Intent(this, NewHome.class);
        iNotifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(this, 0, iNotifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError())
            return;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.KeyChannelID))
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(getString(R.string.app_name)).setContentIntent(notifyPendingIntent)
                .setContentText(getString(R.string.MsgNotification))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        iNotificationManager.notify(42, builder.build());
        
    }
    
}

