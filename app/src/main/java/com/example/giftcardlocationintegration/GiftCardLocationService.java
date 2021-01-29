package com.example.giftcardlocationintegration;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class GiftCardLocationService extends Service {
    public static String CHANNEL_ID = "giftcardlocation";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startForeground(1, buildNotification());
    }

    private Notification buildNotification() {

        Intent notificationIntent = new Intent(this, GiftCardListFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_normal_background)
                .setContentTitle("test")
                .setContentText("testContent")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build();

        return builder;
    }
}
